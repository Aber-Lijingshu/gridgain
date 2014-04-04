/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.thread;

import org.gridgain.testframework.junits.common.*;

import java.util.*;

/**
 * Test for {@link GridThread}.
 */
@GridCommonTest(group = "Utils")
public class GridThreadTest extends GridCommonAbstractTest {
    /** Thread count. */
    private static final int THREAD_CNT = 3;

    /**
     * @throws Exception If failed.
     */
    public void testAssertion() throws Exception {
        Collection<GridThread> ts = new ArrayList<>();

        for (int i = 0; i < THREAD_CNT; i++) {
            ts.add(new GridThread("test-grid-" + i, "test-thread", new Runnable() {
                @Override public void run() {
                    assert false : "Expected assertion.";
                }
            }));
        }

        for (GridThread t : ts)
            t.start();

        for (GridThread t : ts)
            t.join();
    }
}
