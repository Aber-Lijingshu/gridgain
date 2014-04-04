/* @java.file.header */

package org.gridgain.grid.kernal.processors.closure;

import org.gridgain.grid.compute.*;
import org.gridgain.grid.util.lang.*;
import org.gridgain.grid.util.typedef.internal.*;
import org.jetbrains.annotations.*;

/**
 * Peer deployment aware task adapter.
 */
public abstract class GridPeerDeployAwareTaskAdapter<T, R> extends GridComputeTaskAdapter<T, R>
    implements GridPeerDeployAware {
    /** Peer deploy aware class. */
    private transient GridPeerDeployAware pda;

    /**
     * Constructor that receives deployment information for task.
     *
     * @param pda Deployment information.
     */
    protected GridPeerDeployAwareTaskAdapter(@Nullable GridPeerDeployAware pda) {
        this.pda = pda;
    }

    /** {@inheritDoc} */
    @Override public Class<?> deployClass() {
        if (pda == null)
            pda = U.detectPeerDeployAware(this);

        return pda.deployClass();
    }

    /** {@inheritDoc} */
    @Override public ClassLoader classLoader() {
        if (pda == null)
            pda = U.detectPeerDeployAware(this);

        return pda.classLoader();
    }
}
