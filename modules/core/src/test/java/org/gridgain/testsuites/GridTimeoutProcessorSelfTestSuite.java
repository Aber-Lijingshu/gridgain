/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.testsuites;

import junit.framework.*;
import org.gridgain.grid.kernal.processors.timeout.*;

/**
 * Suite for {@link GridTimeoutProcessor} tests.
 */
public class GridTimeoutProcessorSelfTestSuite extends TestSuite {
    /**
     * @return Job metrics test suite.
     * @throws Exception If failed.
     */
    public static TestSuite suite() throws Exception {
        TestSuite suite = new TestSuite("Gridgain Timeout Processor Test Suite");

        suite.addTest(new TestSuite(GridTimeoutProcessorSelfTest.class));

        return suite;
    }
}
