/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.hadoop.v1;

import org.apache.hadoop.mapred.*;
import org.gridgain.grid.*;
import org.gridgain.grid.hadoop.*;
import org.gridgain.grid.kernal.processors.hadoop.v2.*;

import java.io.*;

/**
 * Hadoop setup task implementation for v1 API.
 */
public class GridHadoopV1SetupTask extends GridHadoopV1Task {
    /**
     * Constructor.
     *
     * @param taskInfo Task info.
     */
    public GridHadoopV1SetupTask(GridHadoopTaskInfo taskInfo) {
        super(taskInfo);
    }

    /** {@inheritDoc} */
    @Override public void run(GridHadoopTaskContext taskCtx) throws GridException {
        GridHadoopV2Job jobImpl = (GridHadoopV2Job)taskCtx.job();

        JobContext jobCtx = jobImpl.hadoopJobContext();

        try {
            OutputCommitter committer = jobCtx.getJobConf().getOutputCommitter();

            committer.setupJob(jobCtx);
        }
        catch (IOException e) {
            throw new GridException(e);
        }
    }
}
