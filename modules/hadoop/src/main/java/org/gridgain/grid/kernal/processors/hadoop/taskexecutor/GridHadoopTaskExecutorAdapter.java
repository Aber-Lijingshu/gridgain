/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.hadoop.taskexecutor;

import org.gridgain.grid.hadoop.*;
import org.gridgain.grid.kernal.processors.hadoop.*;
import org.gridgain.grid.kernal.processors.hadoop.jobtracker.*;

import java.util.*;

/**
 * Common superclass for task executor.
 */
public abstract class GridHadoopTaskExecutorAdapter extends GridHadoopComponent {
    /**
     * Runs tasks.
     *
     * @param job Job.
     * @param tasks Tasks.
     */
    public abstract void run(final GridHadoopJob job, Collection<GridHadoopTaskInfo> tasks);

    /**
     * Cancels all currently running tasks for given job ID and cancels scheduled execution of tasks
     * for this job ID.
     * <p>
     * It is guaranteed that this method will not be called concurrently with
     * {@link #run(GridHadoopJob, Collection)} method. No more job submissions will be performed via
     * {@link #run(GridHadoopJob, Collection)} method for given job ID after this method is called.
     *
     * @param jobId Job ID to cancel.
     */
    public abstract void cancelTasks(GridHadoopJobId jobId);

    /**
     * On job state change callback;
     *
     * @param job Job instance.
     * @param meta Job metadata.
     */
    public abstract void onJobStateChanged(GridHadoopJob job, GridHadoopJobMetadata meta);
}
