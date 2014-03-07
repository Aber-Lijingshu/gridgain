/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.spi.discovery.tcp;

import org.gridgain.grid.spi.discovery.*;
import org.gridgain.grid.spi.discovery.tcp.ipfinder.*;
import org.gridgain.grid.spi.discovery.tcp.ipfinder.vm.*;
import org.gridgain.testframework.junits.spi.*;

/**
 * TCP discovery spi test.
 */
@SuppressWarnings({"JUnitTestCaseWithNoTests"})
@GridSpiTest(spi = GridTcpDiscoverySpi.class, group = "Discovery SPI")
public class GridTcpDiscoverySpiSelfTest extends GridAbstractDiscoverySelfTest<GridTcpDiscoverySpi> {
    /** */
    private GridTcpDiscoveryIpFinder ipFinder =  new GridTcpDiscoveryVmIpFinder(true);

    /** {@inheritDoc} */
    @Override protected GridDiscoverySpi getSpi(int idx) {
        GridTcpDiscoverySpi spi = new GridTcpDiscoverySpi();

        spi.setMetricsProvider(createMetricsProvider());
        spi.setIpFinder(ipFinder);

        return spi;
    }
}
