/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.streamer;

import org.gridgain.grid.*;
import org.gridgain.grid.util.*;
import org.gridgain.grid.util.typedef.*;

import java.util.*;

/**
 * Streamer adapter for event routers.
 *
 * @author @java.author
 * @version @java.version
 */
public abstract class GridStreamerEventRouterAdapter implements GridStreamerEventRouter {
    /** {@inheritDoc} */
    @Override public <T> Map<GridNode, Collection<T>> route(GridStreamerContext ctx, String stageName,
        Collection<T> evts) {
        if (evts.size() == 1) {
            GridNode route = route(ctx, stageName, F.first(evts));

            if (route == null)
                return null;

            return Collections.singletonMap(route, evts);
        }

        Map<GridNode, Collection<T>> map = new GridLeanMap<>();

        for (T e : evts) {
            GridNode n = route(ctx, stageName, e);

            if (n == null)
                return null;

            Collection<T> mapped = map.get(n);

            if (mapped == null)
                map.put(n, mapped = new ArrayList<>());

            mapped.add(e);
        }

        return map;
    }
}
