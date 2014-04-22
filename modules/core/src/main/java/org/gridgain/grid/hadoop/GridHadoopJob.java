/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.hadoop;

import org.gridgain.grid.*;

import java.util.*;

/**
 * Hadoop job.
 */
public interface GridHadoopJob {
    /**
     * Gets job ID.
     *
     * @return Job ID.
     */
    public GridHadoopJobId id();

    /**
     * Gets job information.
     *
     * @return Job information.
     */
    public GridHadoopJobInfo info();

    /**
     * Gets collection of input blocks.
     *
     * @return Input blocks.
     */
    public Collection<GridHadoopFileBlock> input() throws GridException;

    /**
     * Gets number of reducers for this job.
     *
     * @return Number of reducers.
     */
    public int reducers();

    /**
     * Gets partitioner for the job.
     *
     * @return Partitioner.
     */
    public GridHadoopPartitioner partitioner() throws GridException;

    /**
     * Creates new instance of mapper output serialization object.
     *
     * @return Serialization facility.
     * @throws GridException if failed.
     */
    public GridHadoopSerialization serialization() throws GridException;

    /**
     * Creates task to be executed.
     *
     * @param taskInfo Task info.
     * @return Task.
     */
    public GridHadoopTask createTask(GridHadoopTaskInfo taskInfo);

    /**
     * Checks if combiner is set for this job.
     *
     * @return {@code true} If combiner is set for this job.
     */
    public boolean hasCombiner();
}
