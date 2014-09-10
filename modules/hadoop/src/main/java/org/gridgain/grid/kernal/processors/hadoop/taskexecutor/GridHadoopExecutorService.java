/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.hadoop.taskexecutor;


import org.gridgain.grid.GridException;
import org.gridgain.grid.GridInterruptedException;
import org.gridgain.grid.hadoop.GridHadoopTaskInfo;
import org.gridgain.grid.logger.GridLogger;
import org.gridgain.grid.thread.GridThread;
import org.gridgain.grid.util.worker.GridWorker;
import org.gridgain.grid.util.worker.GridWorkerListener;
import org.gridgain.grid.util.worker.GridWorkerListenerAdapter;
import org.jdk8.backport.ConcurrentHashMap8;

import java.util.Collection;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Collections.*;

/**
 * Executor service.
 */
public class GridHadoopExecutorService {
    /** */
    private final LinkedBlockingQueue<Callable<?>> queue;

    /** */
    private final Collection<GridWorker> workers = newSetFromMap(new ConcurrentHashMap8<GridWorker, Boolean>());

    /** */
    private final AtomicInteger active = new AtomicInteger();

    /** */
    private final int maxTasks;

    /** */
    private final String gridName;

    /** */
    private final GridLogger log;

    /** */
    private volatile boolean shutdown;

    /** */
    private final GridWorkerListener lsnr = new GridWorkerListenerAdapter() {
            @Override public void onStopped(GridWorker w) {
                workers.remove(w);

                if (shutdown) {
                    active.decrementAndGet();

                    return;
                }

                Callable<?> task = queue.poll();

                if (task != null)
                    startThread(task);
                else {
                    active.decrementAndGet();

                    if (!queue.isEmpty())
                        startFromQueue();
                }
            }
        };

    /**
     * @param log Logger.
     * @param gridName Grid name.
     * @param maxTasks Max number of tasks.
     * @param maxQueue Max queue length.
     */
    public GridHadoopExecutorService(GridLogger log, String gridName, int maxTasks, int maxQueue) {
        assert maxTasks > 0 : maxTasks;
        assert maxQueue > 0 : maxQueue;

        this.maxTasks = maxTasks;
        this.queue = new LinkedBlockingQueue<>(maxQueue);
        this.gridName = gridName;
        this.log = log.getLogger(GridHadoopExecutorService.class);
    }

    /**
     * @return Number of active workers.
     */
    public int active() {
        return workers.size();
    }

    /**
     * Submit task.
     *
     * @param task Task.
     */
    public void submit(Callable<?> task) {
        while (queue.isEmpty()) {
            int active0 = active.get();

            if (active0 == maxTasks)
                break;

            if (active.compareAndSet(active0, active0 + 1)) {
                startThread(task);

                return; // Started in new thread bypassing queue.
            }
        }

        try {
            while (!queue.offer(task, 100, TimeUnit.MILLISECONDS)) {
                if (shutdown)
                    return; // Rejected due to shutdown.
            }
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            return;
        }

        startFromQueue();
    }

    /**
     * Attempts to start task from queue.
     */
    private void startFromQueue() {
        do {
            int active0 = active.get();

            if (active0 == maxTasks)
                break;

            if (active.compareAndSet(active0, active0 + 1)) {
                Callable<?> task = queue.poll();

                if (task == null) {
                    int res = active.decrementAndGet();

                    assert res >= 0 : res;

                    break;
                }

                startThread(task);
            }
        }
        while (!queue.isEmpty());
    }

    /**
     * @param task Task.
     */
    private void startThread(final Callable<?> task) {
        String workerName;

        if (task instanceof GridHadoopRunnableTask) {
            final GridHadoopTaskInfo i = ((GridHadoopRunnableTask)task).taskInfo();

            workerName = "Hadoop-task-" + i.jobId() + "-" + i.type() + "-" + i.taskNumber() + "-" + i.attempt();
        }
        else
            workerName = task.toString();

        GridWorker w = new GridWorker(gridName, workerName, log, lsnr) {
            @Override protected void body() throws InterruptedException, GridInterruptedException {
                try {
                    task.call();
                }
                catch (Exception e) {
                    log.error("Failed to execute task: " + task, e);
                }
            }
        };

        workers.add(w);

        if (shutdown)
            w.cancel();

        new GridThread(w).start();
    }

    /**
     * Shuts down this executor service.
     *
     * @param awaitTimeMillis Time in milliseconds to wait for tasks completion.
     * @return {@code true} If all tasks completed.
     */
    public boolean shutdown(long awaitTimeMillis) {
        shutdown = true;

        for (GridWorker w : workers)
            w.cancel();

        while (awaitTimeMillis > 0 && !workers.isEmpty()) {
            try {
                Thread.sleep(100);

                awaitTimeMillis -= 100;
            }
            catch (InterruptedException e) {
                break;
            }
        }

        return workers.isEmpty();
    }

    /**
     * @return {@code true} If method {@linkplain #shutdown(long)} was already called.
     */
    public boolean isShutdown() {
        return shutdown;
    }
}
