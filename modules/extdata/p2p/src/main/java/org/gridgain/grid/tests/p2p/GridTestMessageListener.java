/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.tests.p2p;

import org.gridgain.grid.*;
import org.gridgain.grid.resources.*;
import org.gridgain.grid.util.typedef.*;

import java.util.*;
import java.util.concurrent.atomic.*;

/**
 * Test message listener.
 */
public class GridTestMessageListener implements P2<UUID, Object> {
    /** */
    @GridInstanceResource
    private Grid grid;

    /** {@inheritDoc} */
    @Override public boolean apply(UUID nodeId, Object msg) {
        grid.log().info("Received message [nodeId=" + nodeId + ", locNodeId=" + grid.localNode().id() +
            ", msg=" + msg + ']');

        GridNodeLocalMap<String, AtomicInteger> map = grid.nodeLocalMap();

        AtomicInteger cnt = map.get("msgCnt");

        if (cnt == null) {
            AtomicInteger old = map.putIfAbsent("msgCnt", cnt = new AtomicInteger(0));

            if (old != null)
                cnt = old;
        }

        cnt.incrementAndGet();

        return true;
    }
}
