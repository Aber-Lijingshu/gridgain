/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal;

import org.gridgain.grid.*;
import org.gridgain.grid.util.typedef.*;
import org.gridgain.testframework.junits.common.*;

import java.io.*;
import java.util.*;

/**
 * Grid basic communication test.
 */
@GridCommonTest(group = "Kernal Self")
public class GridCommunicationSelfTest extends GridCommonAbstractTest {
    /** */
    private static Grid grid;

    /** */
    public GridCommunicationSelfTest() {
        super(/*start grid*/true);
    }

    /** {@inheritDoc} */
    @Override protected void beforeTestsStarted() throws Exception {
        grid = G.grid(getTestGridName());
    }

    /**
     * @throws Exception If failed.
     */
    public void testSendMessageToEmptyNodes() throws Exception {
        Collection<GridNode> empty = Collections.emptyList();

        try {
            sendMessage(empty, 1);
        }
        catch (IllegalArgumentException ignored) {
            // No-op.
        }
    }

    /**
     * @param nodes Nodes to send message to.
     * @param cntr Counter.
     */
    private void sendMessage(Collection<GridNode> nodes, int cntr) {
        try {
            grid.forNodes(nodes).message().send(null,
                new GridTestCommunicationMessage(cntr, grid.localNode().id()));
        }
        catch (GridException e) {
            error("Failed to send message.", e);
        }
    }

    /**
     * Test message.
     */
    @SuppressWarnings({"PublicInnerClass"})
    public static class GridTestCommunicationMessage implements Serializable {
        /** */
        private final int msgId;

        /** */
        private final UUID sndId;

        /**
         * @param msgId Message id.
         * @param sndId Sender id.
         */
        public GridTestCommunicationMessage(int msgId, UUID sndId) {
            assert sndId != null;

            this.msgId = msgId;
            this.sndId = sndId;
        }

        /**
         * @return Message id.
         */
        public int getMessageId() {
            return msgId;
        }

        /**
         * @return Sender id.
         */
        public UUID getSenderId() {
            return sndId;
        }

        /** {@inheritDoc} */
        @Override public String toString() {
            StringBuilder buf = new StringBuilder();

            buf.append(getClass().getSimpleName());
            buf.append(" [msgId=").append(msgId);
            buf.append(']');

            return buf.toString();
        }
    }
}
