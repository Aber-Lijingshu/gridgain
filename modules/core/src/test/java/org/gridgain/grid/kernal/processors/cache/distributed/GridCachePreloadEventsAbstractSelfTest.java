/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.cache.distributed;

import org.gridgain.grid.*;
import org.gridgain.grid.cache.*;
import org.gridgain.grid.cache.affinity.consistenthash.*;
import org.gridgain.grid.events.*;
import org.gridgain.grid.lang.*;
import org.gridgain.grid.spi.discovery.tcp.*;
import org.gridgain.grid.spi.discovery.tcp.ipfinder.*;
import org.gridgain.grid.spi.discovery.tcp.ipfinder.vm.*;
import org.gridgain.grid.spi.eventstorage.memory.*;
import org.gridgain.grid.util.typedef.internal.*;
import org.gridgain.testframework.junits.common.*;

import java.util.*;

import static org.gridgain.grid.events.GridEventType.*;
import static org.gridgain.grid.cache.GridCacheAtomicityMode.*;
import static org.gridgain.grid.cache.GridCacheMode.*;
import static org.gridgain.grid.cache.GridCacheDistributionMode.*;
import static org.gridgain.grid.cache.GridCachePreloadMode.*;

/**
 *
 */
public abstract class GridCachePreloadEventsAbstractSelfTest extends GridCommonAbstractTest {
    /** */
    private GridTcpDiscoveryIpFinder ipFinder = new GridTcpDiscoveryVmIpFinder(true);

    /** {@inheritDoc} */
    @Override protected GridConfiguration getConfiguration(String gridName) throws Exception {
        GridConfiguration cfg = super.getConfiguration(gridName);

        GridTcpDiscoverySpi disco = new GridTcpDiscoverySpi();
        disco.setIpFinder(ipFinder);
        cfg.setDiscoverySpi(disco);

        cfg.setCacheConfiguration(cacheConfiguration());

        GridMemoryEventStorageSpi evtStorageSpi = new GridMemoryEventStorageSpi();

        evtStorageSpi.setExpireCount(50_000);

        cfg.setEventStorageSpi(evtStorageSpi);

        return cfg;
    }

    /**
     * @return Cache mode.
     */
    protected abstract GridCacheMode getCacheMode();

    /**
     * @return Cache configuration.
     */
    protected GridCacheConfiguration cacheConfiguration() {
        GridCacheConfiguration cacheCfg = defaultCacheConfiguration();

        cacheCfg.setCacheMode(getCacheMode());
        cacheCfg.setPreloadMode(SYNC);
        cacheCfg.setAtomicityMode(TRANSACTIONAL);
        cacheCfg.setDistributionMode(NEAR_PARTITIONED);

        if (getCacheMode() == PARTITIONED)
            cacheCfg.setBackups(1);

        return cacheCfg;
    }

    /** {@inheritDoc} */
    @Override protected void afterTest() throws Exception {
        stopAllGrids();
    }

    /**
     * @throws Exception if failed.
     */
    public void testPreloadEvents() throws Exception {
        Grid g1 = startGrid("g1");

        GridCache<Integer, String> cache = g1.cache(null);

        cache.put(1, "val1");
        cache.put(2, "val2");
        cache.put(3, "val3");

        Grid g2 = startGrid("g2");

        Collection<GridEvent> evts = g2.events().localQuery(new GridPredicate<GridEvent>() {
            @Override
            public boolean apply(GridEvent e) {
                return e.type() == EVT_CACHE_PRELOAD_OBJECT_LOADED;
            }
        });

        checkPreloadEvents(evts, g2, U.toIntList(new int[]{1, 2, 3}));
    }

    /**
     * @param evts Events.
     * @param g Grid.
     * @param keys Keys.
     */
    protected void checkPreloadEvents(Collection<GridEvent> evts, Grid g, Collection<? extends Object> keys) {
        assertEquals(keys.size(), evts.size());

        for (GridEvent evt : evts) {
            GridCacheEvent cacheEvt = (GridCacheEvent)evt;
            assertEquals(EVT_CACHE_PRELOAD_OBJECT_LOADED, cacheEvt.type());
            assertEquals(g.cache(null).name(), cacheEvt.cacheName());
            assertEquals(g.localNode().id(), cacheEvt.nodeId());
            assertEquals(g.localNode().id(), cacheEvt.eventNodeId());
            assertTrue(cacheEvt.hasNewValue());
            assertNotNull(cacheEvt.newValue());
            assertTrue("Unexpected key: " + cacheEvt.key(), keys.contains(cacheEvt.key()));
        }
    }
}
