/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.streamer.router;

import org.gridgain.grid.*;
import org.gridgain.grid.streamer.*;

import java.util.*;
import java.util.concurrent.atomic.*;

/**
 * Round robin router.
 *
 * @author @java.author
 * @version @java.version
 */
public class GridStreamerRoundRobinEventRouter extends GridStreamerEventRouterAdapter {
    /** */
    private final AtomicLong lastOrder = new AtomicLong();

    /** {@inheritDoc} */
    @Override public GridNode route(GridStreamerContext ctx, String stageName, Object evt) {
        Collection<GridNode> nodes = ctx.projection().nodes();

        int idx = (int)(lastOrder.getAndIncrement() % nodes.size());

        int i = 0;

        Iterator<GridNode> iter = nodes.iterator();

        while (true) {
            if (!iter.hasNext())
                iter = nodes.iterator();

            GridNode node = iter.next();

            if (idx == i++)
                return node;
        }
    }
}
