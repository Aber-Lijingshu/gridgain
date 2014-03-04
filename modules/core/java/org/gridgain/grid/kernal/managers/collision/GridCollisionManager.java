/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.managers.collision;

import org.gridgain.grid.*;
import org.gridgain.grid.kernal.*;
import org.gridgain.grid.kernal.managers.*;
import org.gridgain.grid.spi.collision.*;
import org.gridgain.grid.util.typedef.internal.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.concurrent.atomic.*;

/**
 * This class defines a collision manager.
 *
 * @author @java.author
 * @version @java.version
 */
public class GridCollisionManager extends GridManagerAdapter<GridCollisionSpi> {
    /** Reference for external listener. */
    private final AtomicReference<GridCollisionExternalListener> extLsnr =
        new AtomicReference<>();

    /**
     * @param ctx Grid kernal context.
     */
    public GridCollisionManager(GridKernalContext ctx) {
        super(ctx, ctx.config().getCollisionSpi());
    }

    /** {@inheritDoc} */
    @Override public void start() throws GridException {
        if (ctx.config().isDaemon())
            return;

        startSpi();

        if (enabled()) {
            getSpi().setExternalCollisionListener(new GridCollisionExternalListener() {
                @Override public void onExternalCollision() {
                    GridCollisionExternalListener lsnr = extLsnr.get();

                    if (lsnr != null)
                        lsnr.onExternalCollision();
                }
            });
        }
        else
            U.warn(log, "Collision resolution is disabled (all jobs will be activated upon arrival).");

        if (log.isDebugEnabled())
            log.debug(startInfo());
    }

    /** {@inheritDoc} */
    @Override public void stop(boolean cancel) throws GridException {
        if (ctx.config().isDaemon())
            return;

        stopSpi();

        // Unsubscribe.
        if (enabled())
            getSpi().setExternalCollisionListener(null);

        if (log.isDebugEnabled())
            log.debug(stopInfo());
    }

    /**
     * Unsets external collision listener.
     */
    public void unsetCollisionExternalListener() {
        if (enabled())
            getSpi().setExternalCollisionListener(null);
    }

    /**
     * @param lsnr Listener to external collision events.
     */
    public void setCollisionExternalListener(@Nullable GridCollisionExternalListener lsnr) {
        if (enabled()) {
            if (lsnr != null && !extLsnr.compareAndSet(null, lsnr))
                assert false : "Collision external listener has already been set " +
                    "(perhaps need to add support for multiple listeners)";
            else if (log.isDebugEnabled())
                log.debug("Successfully set external collision listener: " + lsnr);
        }
    }

    /**
     * @param waitJobs List of waiting jobs.
     * @param activeJobs List of active jobs.
     * @param heldJobs List of held jobs.
     */
    public void onCollision(
        final Collection<GridCollisionJobContext> waitJobs,
        final Collection<GridCollisionJobContext> activeJobs,
        final Collection<GridCollisionJobContext> heldJobs) {
        if (enabled()) {
            if (log.isDebugEnabled())
                log.debug("Resolving job collisions [waitJobs=" + waitJobs + ", activeJobs=" + activeJobs + ']');

            getSpi().onCollision(new GridCollisionContext() {
                @Override public Collection<GridCollisionJobContext> activeJobs() {
                    return activeJobs;
                }

                @Override public Collection<GridCollisionJobContext> waitingJobs() {
                    return waitJobs;
                }

                @Override public Collection<GridCollisionJobContext> heldJobs() {
                    return heldJobs;
                }
            });
        }
    }
}
