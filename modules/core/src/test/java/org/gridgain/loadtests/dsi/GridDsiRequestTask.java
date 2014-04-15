/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.loadtests.dsi;

import org.gridgain.grid.*;
import org.gridgain.grid.compute.*;
import org.gridgain.grid.util.typedef.*;

import java.util.*;

/**
 *
 */
public class GridDsiRequestTask extends GridComputeTaskSplitAdapter<GridDsiMessage, T3<Long, Integer, Integer>> {
    /** {@inheritDoc} */
    @Override protected Collection<? extends GridComputeJob> split(int arg0, GridDsiMessage msg) throws GridException {
        return Collections.singletonList(new GridDsiPerfJob(msg));
    }

    /** {@inheritDoc} */
    @Override public T3<Long, Integer, Integer> reduce(List<GridComputeJobResult> results) throws GridException {
        assert results.size() == 1;

        return results.get(0).getData();
    }
}
