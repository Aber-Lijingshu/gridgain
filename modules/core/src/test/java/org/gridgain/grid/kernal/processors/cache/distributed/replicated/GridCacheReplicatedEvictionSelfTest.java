/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.cache.distributed.replicated;

import org.gridgain.grid.*;
import org.gridgain.grid.cache.*;
import org.gridgain.grid.events.*;
import org.gridgain.grid.kernal.processors.cache.*;
import org.gridgain.grid.lang.*;
import org.gridgain.grid.util.typedef.*;

import java.util.*;

import static org.gridgain.grid.cache.GridCacheDistributionMode.*;
import static org.gridgain.grid.cache.GridCacheMode.*;
import static org.gridgain.grid.events.GridEventType.*;

/**
 * Tests synchronous eviction for replicated cache.
 */
public class GridCacheReplicatedEvictionSelfTest extends GridCacheAbstractSelfTest {
    /** {@inheritDoc} */
    @Override protected int gridCount() {
        return 4;
    }

    /** {@inheritDoc} */
    @Override protected GridCacheMode cacheMode() {
        return REPLICATED;
    }

    /** {@inheritDoc} */
    @Override protected GridCacheConfiguration cacheConfiguration(String gridName) throws Exception {
        GridCacheConfiguration ccfg = super.cacheConfiguration(gridName);

        ccfg.setEvictSynchronized(true);

        return ccfg;
    }

    /** {@inheritDoc} */
    @Override protected GridCacheDistributionMode distributionMode() {
        return PARTITIONED_ONLY;
    }

    /** {@inheritDoc} */
    @Override protected boolean swapEnabled() {
        return false;
    }

    /**
     * @throws Exception If failed.
     */
    public void testEvictSynchronized() throws Exception {
        final int KEYS = 10;

        for (int i = 0; i < KEYS; i++)
            cache(0).put(String.valueOf(i), i);

        for (int g = 0 ; g < gridCount(); g++) {
            for (int i = 0; i < KEYS; i++)
                assertNotNull(cache(g).peek(String.valueOf(i)));
        }

        Collection<GridFuture<GridEvent>> futs = new ArrayList<>();

        for (int g = 0 ; g < gridCount(); g++)
            futs.add(grid(g).events().waitForLocal(nodeEvent(grid(g).localNode().id()), EVT_CACHE_ENTRY_EVICTED));

        for (int i = 0; i < KEYS; i++)
            assertTrue(cache(0).evict(String.valueOf(i)));

        for (GridFuture<GridEvent> fut : futs)
            fut.get(3000);

        Thread.sleep(3000);

        for (int g = 0 ; g < gridCount(); g++) {
            for (int i = 0; i < KEYS; i++)
                assertNull(cache(g).peek(String.valueOf(i)));
        }
    }

    /**
     * @param nodeId Node id.
     * @return Predicate for events belonging to specified node.
     */
    private GridPredicate<GridEvent> nodeEvent(final UUID nodeId) {
        assert nodeId != null;

        return new P1<GridEvent>() {
            @Override public boolean apply(GridEvent e) {
                info("Predicate called [e.nodeId()=" + e.node().id() + ", nodeId=" + nodeId + ']');

                return e.node().id().equals(nodeId);
            }
        };
    }
}
