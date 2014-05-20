/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.hadoop.proto;

import org.gridgain.grid.*;
import org.gridgain.grid.compute.*;
import org.gridgain.grid.hadoop.*;

import java.util.*;

/**
 * Submit job task.
 */
public class GridHadoopProtocolSubmitJobTask extends GridHadoopProtocolTaskAdapter<GridHadoopJobStatus> {
    /** {@inheritDoc} */
    @Override public GridHadoopJobStatus run(GridComputeJobContext jobCtx, GridHadoop hadoop,
        GridHadoopProtocolTaskArguments args) throws GridException {
        UUID nodeId = UUID.fromString(args.<String>get(0));
        Integer id = args.get(1);
        GridHadoopProtocolConfigurationWrapper conf = args.get(2);

        assert nodeId != null;
        assert id != null;
        assert conf != null;

        GridHadoopJobId jobId = new GridHadoopJobId(nodeId, id);

        GridHadoopDefaultJobInfo info = new GridHadoopDefaultJobInfo(conf.get());

        hadoop.submit(jobId, info);

        GridHadoopJobStatus res = hadoop.status(jobId);

        if (res == null) // Submission failed.
            res = new GridHadoopJobStatus(jobId, GridHadoopJobState.STATE_FAILED, info.configuration().getJobName(),
                info.configuration().getUser(), 0, 0, 0, 0, GridHadoopJobPhase.PHASE_CANCELLING, 1);

        return res;
    }
}
