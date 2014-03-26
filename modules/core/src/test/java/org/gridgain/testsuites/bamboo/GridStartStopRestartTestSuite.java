/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.testsuites.bamboo;

import junit.framework.*;
import org.gridgain.grid.kernal.*;
import org.gridgain.grid.util.nodestart.*;

/**
 * Test suite for remote node start.
 */
public class GridStartStopRestartTestSuite extends TestSuite {
    /**
     * @return Remote node start suite.
     * @throws Exception In case of error.
     */
    public static TestSuite suite() throws Exception {
        TestSuite suite = new TestSuite("Remote node start suite.");

        suite.addTestSuite(GridProjectionStartStopRestartSelfTest.class);
        suite.addTestSuite(GridNodeStartUtilsSelfTest.class);

        return suite;
    }
}
