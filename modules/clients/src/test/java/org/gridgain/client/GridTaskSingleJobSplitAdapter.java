/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.client;

import org.gridgain.grid.*;
import org.gridgain.grid.compute.*;

import java.util.*;

/**
 * Adapter for {@link GridComputeTaskSplitAdapter}
 * overriding {@code split(...)} method to return singleton with self instance.
 * This adapter should be used for tasks that always splits to a single task.
 * @param <T> Type of the task execution argument.
 * @param <R> Type of the task result returning from {@link GridComputeTask#reduce(List)} method.
 */
public abstract class GridTaskSingleJobSplitAdapter<T, R> extends GridComputeTaskSplitAdapter<T, R> {
    /** Empty constructor. */
    protected GridTaskSingleJobSplitAdapter() {
        // No-op.
    }

    /**
     * Constructor that receives deployment information for task.
     *
     * @param p Deployment information.
     */
    protected GridTaskSingleJobSplitAdapter(GridPeerDeployAware p) {
        super(p);
    }

    /** {@inheritDoc} */
    @Override protected Collection<? extends GridComputeJob> split(final int gridSize, final T arg) throws GridException {
        return Collections.singleton(new GridComputeJobAdapter() {
            @Override public Object execute() throws GridException {
                return executeJob(gridSize, arg);
            }
        });
    }

    /** {@inheritDoc} */
    @Override public R reduce(List<GridComputeJobResult> results) throws GridException {
        assert results.size() == 1;

        GridComputeJobResult res = results.get(0);

        if (res.isCancelled())
            throw new GridException("Reduce receives failed job.");

        return res.getData();
    }

    /**
     * Executes this task's job.
     *
     * @param gridSize Number of available grid nodes. Note that returned number of
     *      jobs can be less, equal or greater than this grid size.
     * @param arg Task execution argument. Can be {@code null}.
     * @return Job execution result (possibly {@code null}). This result will be returned
     *      in {@link GridComputeJobResult#getData()} method passed into
     *      {@link GridComputeTask#result(GridComputeJobResult, List)} method into task on caller node.
     * @throws GridException If job execution caused an exception. This exception will be
     *      returned in {@link GridComputeJobResult#getException()} method passed into
     *      {@link GridComputeTask#result(GridComputeJobResult, List)} method into task on caller node.
     *      If execution produces a {@link RuntimeException} or {@link Error}, then
     *      it will be wrapped into {@link GridException}.
     */
    protected abstract Object executeJob(int gridSize, T arg) throws GridException;
}
