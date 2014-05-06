/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.hadoop.proto;

import org.gridgain.grid.*;
import org.gridgain.grid.hadoop.*;
import org.gridgain.grid.kernal.processors.hadoop.*;

/**
 * Task to get the next job ID.
 */
public class GridHadoopProtocolNextTaskIdTask extends GridHadoopProtocolTaskAdapter<GridHadoopJobId> {
    /** {@inheritDoc} */
    @Override public GridHadoopJobId run(GridHadoopProcessorAdapter proc, GridHadoopProtocolTaskArguments args)
        throws GridException {
        return proc.nextJobId();
    }
}
