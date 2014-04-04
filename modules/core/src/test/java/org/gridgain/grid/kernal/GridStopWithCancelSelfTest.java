/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal;

import org.gridgain.grid.*;
import org.gridgain.grid.compute.*;
import org.gridgain.grid.resources.*;
import org.gridgain.testframework.junits.common.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Tests grid stop with jobs canceling.
 */
@GridCommonTest(group = "Kernal Self")
public class GridStopWithCancelSelfTest extends GridCommonAbstractTest {
    /** */
    private static CountDownLatch cnt;

    /** */
    private static volatile boolean cancelCorrect;

    /**
     * Constructor.
     */
    public GridStopWithCancelSelfTest() {
        super(false);
    }

    /** {@inheritDoc} */
    @Override protected long getTestTimeout() {
        return 10000;
    }

    /**
     * @throws Exception If an error occurs.
     */
    public void testStopGrid() throws Exception {
        cancelCorrect = false;

        cnt = new CountDownLatch(1);

        try {
            Grid grid = startGrid("testGrid");

            grid.compute().execute(CancelledTask.class, null);

            cnt.await();
        }
        finally {
            stopGrid("testGrid", true);
        }

        assert cancelCorrect;
    }

    /**
     * Test task that will be canceled.
     */
    @SuppressWarnings({"PublicInnerClass"})
    public static final class CancelledTask extends GridComputeTaskAdapter<String, Object> {
        /** */
        @GridLocalNodeIdResource private UUID locId;

        /** {@inheritDoc} */
        @Override public Map<? extends GridComputeJob, GridNode> map(List<GridNode> subgrid,
            @Nullable String arg) throws GridException {
            for (GridNode node : subgrid) {
                if (node.id().equals(locId)) {
                    return Collections.singletonMap(new GridComputeJobAdapter() {
                        @GridInstanceResource
                        private Grid grid;

                        @Override public void cancel() {
                            cancelCorrect = true;
                        }

                        @Override public Serializable execute() throws GridException {
                            cnt.countDown();

                            try {
                                Thread.sleep(Long.MAX_VALUE);
                            }
                            catch (InterruptedException e) {
                                throw new GridException(e);
                            }

                            return null;
                        }
                    }, node);
                }
            }

            throw new GridException("Local node not found");
        }

        /** {@inheritDoc} */
        @Override public Object reduce(List<GridComputeJobResult> results) {
            return null;
        }
    }
}
