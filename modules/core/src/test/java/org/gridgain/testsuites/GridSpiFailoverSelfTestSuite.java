/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.testsuites;

import junit.framework.*;
import org.gridgain.grid.spi.failover.always.*;
import org.gridgain.grid.spi.failover.jobstealing.*;
import org.gridgain.grid.spi.failover.never.*;
import org.gridgain.testframework.*;

/**
 * Failover SPI self-test suite.
 */
public class GridSpiFailoverSelfTestSuite extends TestSuite {
    /**
     * @return Failover SPI tests suite.
     * @throws Exception If failed.
     */
    public static TestSuite suite() throws Exception {
        TestSuite suite = GridTestUtils.createDistributedTestSuite("Gridgain Failover SPI Test Suite");

        // Always failover.
        suite.addTest(new TestSuite(GridAlwaysFailoverSpiSelfTest.class));
        suite.addTest(new TestSuite(GridAlwaysFailoverSpiStartStopSelfTest.class));
        suite.addTest(new TestSuite(GridAlwaysFailoverSpiConfigSelfTest.class));

        // Never failover.
        suite.addTest(new TestSuite(GridNeverFailoverSpiSelfTest.class));
        suite.addTest(new TestSuite(GridNeverFailoverSpiStartStopSelfTest.class));

        // Job stealing failover.
        suite.addTest(new TestSuite(GridJobStealingFailoverSpiSelfTest.class));
        suite.addTest(new TestSuite(GridJobStealingFailoverSpiOneNodeSelfTest.class));
        suite.addTest(new TestSuite(GridJobStealingFailoverSpiStartStopSelfTest.class));
        suite.addTest(new TestSuite(GridJobStealingFailoverSpiConfigSelfTest.class));

        return suite;
    }
}
