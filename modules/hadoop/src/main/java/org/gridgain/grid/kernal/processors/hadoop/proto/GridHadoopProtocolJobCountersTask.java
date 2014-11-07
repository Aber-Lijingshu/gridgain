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
 * Task to get job counters.
 */
public class GridHadoopProtocolJobCountersTask extends GridHadoopProtocolTaskAdapter<GridHadoopCounters> {
    /** */
    private static final long serialVersionUID = 0L;

    /** {@inheritDoc} */
    @Override public GridHadoopCounters run(GridComputeJobContext jobCtx, GridHadoop hadoop,
        GridHadoopProtocolTaskArguments args) throws GridException {

        UUID nodeId = UUID.fromString(args.<String>get(0));
        Integer id = args.get(1);

        assert nodeId != null;
        assert id != null;

        return hadoop.counters(new GridHadoopJobId(nodeId, id));
    }
}
