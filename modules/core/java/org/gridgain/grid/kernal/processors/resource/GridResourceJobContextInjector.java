/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.resource;

import org.gridgain.grid.compute.*;
import org.gridgain.grid.*;
import org.gridgain.grid.kernal.managers.deployment.*;

/**
 * Simple injector which wraps GridComputeJobContext resource object.
 *
 * @author @java.author
 * @version @java.version
 */
public class GridResourceJobContextInjector extends GridResourceBasicInjector<GridComputeJobContext> {
    /**
     * Creates GridComputeJobContext injector.
     *
     * @param rsrc GridComputeJobContext resource to inject.
     */
    GridResourceJobContextInjector(GridComputeJobContext rsrc) {
        super(rsrc);
    }

    /** {@inheritDoc} */
    @Override public void inject(GridResourceField field, Object target, Class<?> depCls, GridDeployment dep)
        throws GridException {
        assert target != null;

        if (!(target instanceof GridComputeTask))
            super.inject(field, target, depCls, dep);
    }

    /** {@inheritDoc} */
    @Override public void inject(GridResourceMethod mtd, Object target, Class<?> depCls, GridDeployment dep)
        throws GridException {
        assert target != null;

        if (!(target instanceof GridComputeTask))
            super.inject(mtd, target, depCls, dep);
    }
}
