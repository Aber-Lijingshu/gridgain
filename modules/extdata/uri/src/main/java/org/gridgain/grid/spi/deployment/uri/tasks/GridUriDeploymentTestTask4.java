/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.spi.deployment.uri.tasks;

import org.gridgain.grid.*;
import org.gridgain.grid.compute.*;

import java.util.*;

/**
 * Simple URI deployment test task.
 */
public class GridUriDeploymentTestTask4 extends GridComputeTaskSplitAdapter<Object, Object> {
    /**
     * {@inheritDoc}
     */
    @Override public Collection<? extends GridComputeJob> split(int gridSize, Object arg) throws GridException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override public Object reduce(List<GridComputeJobResult> results) throws GridException {
        return null;
    }
}
