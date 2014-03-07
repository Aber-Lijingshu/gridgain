/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.p2p;

import org.gridgain.testframework.junits.common.*;

import static org.gridgain.grid.GridDeploymentMode.*;

/**
 * Isolated deployment mode test.
 */
@GridCommonTest(group = "P2P")
public class GridMultinodeRedeployIsolatedModeSelfTest extends GridAbstractMultinodeRedeployTest {
    /**
     * Test GridDeploymentMode.ISOLATED mode.
     *
     * @throws Throwable if error occur.
     */
    public void testIsolatedMode() throws Throwable {
        processTest(ISOLATED);
    }
}
