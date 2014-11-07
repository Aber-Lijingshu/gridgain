/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.spi.swapspace.noop;

import org.gridgain.grid.*;
import org.gridgain.grid.spi.discovery.tcp.*;
import org.gridgain.grid.spi.discovery.tcp.ipfinder.vm.*;
import org.gridgain.grid.spi.swapspace.*;
import org.gridgain.testframework.junits.common.*;

/**
 * Tests for "noop" realization of {@link GridSwapSpaceSpi}.
 */
public class GridNoopSwapSpaceSpiSelfTest extends GridCommonAbstractTest {
    /** {@inheritDoc} */
    @Override protected GridConfiguration getConfiguration(String gridName) throws Exception {
        GridConfiguration cfg = super.getConfiguration(gridName);

        GridTcpDiscoverySpi disco = new GridTcpDiscoverySpi();

        disco.setIpFinder(new GridTcpDiscoveryVmIpFinder(true));

        cfg.setDiscoverySpi(disco);

        return cfg;
    }

    /**
     * @throws Exception If test failed.
     */
    public void testWithoutCacheUseNoopSwapSapce() throws Exception {
        try {
            Grid grid = startGrid(1);

            GridSwapSpaceSpi spi = grid.configuration().getSwapSpaceSpi();

            assertNotNull(spi);

            assertTrue(spi instanceof GridNoopSwapSpaceSpi);
        }
        finally {
            stopAllGrids();
        }
    }
}
