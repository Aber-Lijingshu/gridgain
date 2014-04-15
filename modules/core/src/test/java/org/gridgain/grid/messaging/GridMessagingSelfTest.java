package org.gridgain.grid.messaging;

import org.gridgain.grid.*;
import org.gridgain.grid.marshaller.optimized.*;
import org.gridgain.grid.resources.*;
import org.gridgain.grid.spi.discovery.tcp.*;
import org.gridgain.grid.spi.discovery.tcp.ipfinder.*;
import org.gridgain.grid.spi.discovery.tcp.ipfinder.vm.*;
import org.gridgain.grid.util.typedef.*;
import org.gridgain.grid.util.typedef.internal.*;
import org.gridgain.testframework.*;
import org.gridgain.testframework.config.*;
import org.gridgain.testframework.junits.common.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

import static org.gridgain.testframework.GridTestUtils.*;

/**
 * Various tests for Messaging public API.
 */
public class GridMessagingSelfTest extends GridCommonAbstractTest {
    /** */
    private static final String MSG_1 = "MSG-1";

    /** */
    private static final String MSG_2 = "MSG-2";

    /** */
    private static final String MSG_3 = "MSG-3";

    /** */
    private static final String S_TOPIC_1 = "TOPIC-1";

    /** */
    private static final String S_TOPIC_2 = "TOPIC-2";

    /** */
    private static final Integer I_TOPIC_1 = 1;

    /** */
    private static final Integer I_TOPIC_2 = 2;

    /** */
    public static final String EXT_RESOURCE_CLS_NAME = "org.gridgain.grid.tests.p2p.GridTestUserResource";

    /** Shared IP finder. */
    private final GridTcpDiscoveryIpFinder ipFinder = new GridTcpDiscoveryVmIpFinder(true);

    /**
     * A test message topic.
     */
    private enum TestTopic {
        /** */
        TOPIC_1,

        /** */
        TOPIC_2
    }

    /**
     * A test message with a hack for delay
     * emulation.
     */
    private static class TestMessage implements Externalizable {
        /** */
        private Object body;

        /** */
        private long delayMs;

        /**
         * No-arg constructor for {@link Externalizable}.
         */
        public TestMessage() {
            // No-op.
        }

        /**
         * @param body Message body.
         */
        TestMessage(Object body) {
            this.body = body;
        }

        /**
         * @param body Message body.
         * @param delayMs Message send delay in milliseconds.
         */
        TestMessage(Object body, long delayMs) {
            this.body = body;
            this.delayMs = delayMs;
        }

        /** {@inheritDoc} */
        @Override public String toString() {
            return "TestMessage [body=" + body + "]";
        }

        /** {@inheritDoc} */
        @Override public int hashCode() {
            return body.hashCode();
        }

        /** {@inheritDoc} */
        @Override public boolean equals(Object obj) {
            return obj instanceof TestMessage && body.equals(((TestMessage)obj).body);
        }

        /** {@inheritDoc} */
        @Override public void writeExternal(ObjectOutput out) throws IOException {
            if (delayMs > 0) {
                try {
                    Thread.sleep(delayMs);
                }
                catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            }

            out.writeObject(body);
        }

        /** {@inheritDoc} */
        @Override public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            body = in.readObject();
        }
    }

    /** */
    protected Grid grid1;

    /** */
    protected Grid grid2;

    /** {@inheritDoc} */
    @Override protected void beforeTest() throws Exception {
        grid1 = startGrid(1);
        grid2 = startGrid(2);
    }

    /** {@inheritDoc} */
    @Override protected void afterTest() throws Exception {
        stopAllGrids();
    }

    /** {@inheritDoc} */
    @Override protected GridConfiguration getConfiguration(String gridName) throws Exception {
        GridConfiguration cfg = super.getConfiguration(gridName);

        ((GridOptimizedMarshaller)cfg.getMarshaller()).setRequireSerializable(false);

        GridTcpDiscoverySpi discoSpi = new GridTcpDiscoverySpi();

        discoSpi.setIpFinder(ipFinder);

        cfg.setDiscoverySpi(discoSpi);

        return cfg;
    }

    /**
     * Tests simple message sending-receiving.
     *
     * @throws Exception If error occurs.
     */
    public void testSendReceiveMessage() throws Exception {
        final Collection<Object> rcvMsgs = new HashSet<>();

        final AtomicBoolean error = new AtomicBoolean(false); //to make it modifiable

        final CountDownLatch rcvLatch = new CountDownLatch(3);

        grid1.message().localListen(null, new P2<UUID, Object>() {
            @Override public boolean apply(UUID nodeId, Object msg) {
                try {
                    log.info("Received new message [msg=" + msg + ", senderNodeId=" + nodeId + ']');

                    if (!nodeId.equals(grid2.localNode().id())) {
                        log.error("Unexpected sender node: " + nodeId);

                        error.set(true);

                        return false;
                    }

                    rcvMsgs.add(msg);

                    return true;
                }
                finally {
                    rcvLatch.countDown();
                }
            }
        });

        GridProjection rNode1 = grid2.forRemotes();

        rNode1.message().send(null, MSG_1);
        rNode1.message().send(null, MSG_2);
        rNode1.message().send(null, MSG_3);

        assertTrue(rcvLatch.await(3, TimeUnit.SECONDS));

        assertFalse(error.get());

        assertTrue(rcvMsgs.contains(MSG_1));
        assertTrue(rcvMsgs.contains(MSG_2));
        assertTrue(rcvMsgs.contains(MSG_3));
    }

    /**
     * @throws Exception If error occurs.
     */
    @SuppressWarnings("TooBroadScope")
    public void testStopLocalListen() throws Exception {
        final AtomicInteger msgCnt1 = new AtomicInteger();

        final AtomicInteger msgCnt2 = new AtomicInteger();

        final AtomicInteger msgCnt3 = new AtomicInteger();

        P2<UUID, Object> lsnr1 = new P2<UUID, Object>() {
            @Override public boolean apply(UUID nodeId, Object msg) {
                log.info("Listener1 received new message [msg=" + msg + ", senderNodeId=" + nodeId + ']');

                msgCnt1.incrementAndGet();

                return true;
            }
        };

        P2<UUID, Object> lsnr2 = new P2<UUID, Object>() {
            @Override public boolean apply(UUID nodeId, Object msg) {
                log.info("Listener2 received new message [msg=" + msg + ", senderNodeId=" + nodeId + ']');

                msgCnt2.incrementAndGet();

                return true;
            }
        };

        P2<UUID, Object> lsnr3 = new P2<UUID, Object>() {
            @Override public boolean apply(UUID nodeId, Object msg) {
                log.info("Listener3 received new message [msg=" + msg + ", senderNodeId=" + nodeId + ']');

                msgCnt3.incrementAndGet();

                return true;
            }
        };

        final String topic1 = null;
        final String topic2 = "top1";
        final String topic3 = "top3";

        grid1.message().localListen(topic1, lsnr1);
        grid1.message().localListen(topic2, lsnr2);
        grid1.message().localListen(topic3, lsnr3);

        GridProjection rNode1 = grid2.forRemotes();

        rNode1.message().send(topic1, "msg1-1");
        rNode1.message().send(topic2, "msg1-2");
        rNode1.message().send(topic3, "msg1-3");

        GridTestUtils.waitForCondition(new PA() {
            @Override public boolean apply() {
                return msgCnt1.get() > 0 && msgCnt2.get() > 0 && msgCnt3.get() > 0;
            }
        }, 5000);

        assertEquals(1, msgCnt1.get());
        assertEquals(1, msgCnt2.get());
        assertEquals(1, msgCnt3.get());

        grid1.message().stopLocalListen(topic2, lsnr2);

        rNode1.message().send(topic1, "msg2-1");
        rNode1.message().send(topic2, "msg2-2");
        rNode1.message().send(topic3, "msg2-3");

        GridTestUtils.waitForCondition(new PA() {
            @Override public boolean apply() {
                return msgCnt1.get() > 1 && msgCnt3.get() > 1;
            }
        }, 5000);

        assertEquals(2, msgCnt1.get());
        assertEquals(1, msgCnt2.get());
        assertEquals(2, msgCnt3.get());

        grid1.message().stopLocalListen(topic2, lsnr1); // Try to use wrong topic for lsnr1 removing.

        rNode1.message().send(topic1, "msg3-1");
        rNode1.message().send(topic2, "msg3-2");
        rNode1.message().send(topic3, "msg3-3");

        GridTestUtils.waitForCondition(new PA() {
            @Override public boolean apply() {
                return msgCnt1.get() > 2 && msgCnt3.get() > 2;
            }
        }, 5000);

        assertEquals(3, msgCnt1.get());
        assertEquals(1, msgCnt2.get());
        assertEquals(3, msgCnt3.get());

        grid1.message().stopLocalListen(topic1, lsnr1);
        grid1.message().stopLocalListen(topic3, lsnr3);

        rNode1.message().send(topic1, "msg4-1");
        rNode1.message().send(topic2, "msg4-2");
        rNode1.message().send(topic3, "msg4-3");

        U.sleep(1000);

        assertEquals(3, msgCnt1.get());
        assertEquals(1, msgCnt2.get());
        assertEquals(3, msgCnt3.get());
    }

    /**
     * Tests simple message sending-receiving with string topic.
     *
     * @throws Exception If error occurs.
     */
    public void testSendReceiveMessageWithStringTopic() throws Exception {
        final Collection<Object> rcvMsgs = new HashSet<>();

        final AtomicBoolean error = new AtomicBoolean(false); //to make it modifiable

        final CountDownLatch rcvLatch = new CountDownLatch(3);

        grid1.message().localListen(S_TOPIC_1, new P2<UUID, Object>() {
            @Override public boolean apply(UUID nodeId, Object msg) {
                try {
                    log.info("Received new message [msg=" + msg + ", senderNodeId=" + nodeId +
                        ", topic=" + S_TOPIC_1 + ']');

                    if (!nodeId.equals(grid1.localNode().id())) {
                        log.error("Unexpected sender node: " + nodeId);

                        error.set(true);

                        return false;
                    }

                    if (!MSG_1.equals(msg)) {
                        log.error("Unexpected message " + msg + " for topic: " + S_TOPIC_1);

                        error.set(true);

                        return false;
                    }

                    rcvMsgs.add(msg);

                    return true;
                }
                finally {
                    rcvLatch.countDown();
                }
            }
        });

        grid1.message().localListen(S_TOPIC_2, new P2<UUID, Object>() {
            @Override public boolean apply(UUID nodeId, Object msg) {
                try {
                    log.info("Received new message [msg=" + msg + ", senderNodeId=" + nodeId +
                        ", topic=" + S_TOPIC_2 + ']');

                    if (!nodeId.equals(grid1.localNode().id())) {
                        log.error("Unexpected sender node: " + nodeId);

                        error.set(true);

                        return false;
                    }

                    if (!MSG_2.equals(msg)) {
                        log.error("Unexpected message " + msg + " for topic: " + S_TOPIC_2);

                        error.set(true);

                        return false;
                    }

                    rcvMsgs.add(msg);

                    return true;
                }
                finally {
                    rcvLatch.countDown();
                }
            }
        });

        grid1.message().localListen(null, new P2<UUID, Object>() {
            @Override public boolean apply(UUID nodeId, Object msg) {
                try {
                    log.info("Received new message [msg=" + msg + ", senderNodeId=" + nodeId +
                        ", topic=default]");

                    if (!nodeId.equals(grid1.localNode().id())) {
                        log.error("Unexpected sender node: " + nodeId);

                        error.set(true);

                        return false;
                    }

                    if (!MSG_3.equals(msg)) {
                        log.error("Unexpected message " + msg + " for topic: default");

                        error.set(true);

                        return false;
                    }

                    rcvMsgs.add(msg);

                    return true;
                }
                finally {
                    rcvLatch.countDown();
                }
            }
        });

        GridProjection rNode1 = grid1.forLocal();

        rNode1.message().send(S_TOPIC_1, MSG_1);
        rNode1.message().send(S_TOPIC_2, MSG_2);
        rNode1.message().send(null, MSG_3);

        assertTrue(rcvLatch.await(3, TimeUnit.SECONDS));

        assertFalse(error.get());

        assertTrue(rcvMsgs.contains(MSG_1));
        assertTrue(rcvMsgs.contains(MSG_2));
        assertTrue(rcvMsgs.contains(MSG_3));
    }

    /**
     * Tests simple message sending-receiving with enumerated topic.
     *
     * @throws Exception If error occurs.
     */
    public void testSendReceiveMessageWithEnumTopic() throws Exception {
        final Collection<Object> rcvMsgs = new HashSet<>();

        final AtomicBoolean error = new AtomicBoolean(false); //to make it modifiable

        final CountDownLatch rcvLatch = new CountDownLatch(3);

        grid1.message().localListen(TestTopic.TOPIC_1, new P2<UUID, Object>() {
            @Override public boolean apply(UUID nodeId, Object msg) {
                try {
                    log.info("Received new message [msg=" + msg + ", senderNodeId=" + nodeId +
                        ", topic=" + TestTopic.TOPIC_1 + ']');

                    if (!nodeId.equals(grid1.localNode().id())) {
                        log.error("Unexpected sender node: " + nodeId);

                        error.set(true);

                        return false;
                    }

                    if (!MSG_1.equals(msg)) {
                        log.error("Unexpected message " + msg + " for topic: " + TestTopic.TOPIC_1);

                        error.set(true);

                        return false;
                    }

                    rcvMsgs.add(msg);

                    return true;
                }
                finally {
                    rcvLatch.countDown();
                }
            }
        });

        grid1.message().localListen(TestTopic.TOPIC_2, new P2<UUID, Object>() {
            @Override public boolean apply(UUID nodeId, Object msg) {
                try {
                    log.info("Received new message [msg=" + msg + ", senderNodeId=" + nodeId +
                        ", topic=" + TestTopic.TOPIC_2 + ']');

                    if (!nodeId.equals(grid1.localNode().id())) {
                        log.error("Unexpected sender node: " + nodeId);

                        error.set(true);

                        return false;
                    }

                    if (!MSG_2.equals(msg)) {
                        log.error("Unexpected message " + msg + " for topic: " + TestTopic.TOPIC_2);

                        error.set(true);

                        return false;
                    }

                    rcvMsgs.add(msg);

                    return true;
                }
                finally {
                    rcvLatch.countDown();
                }
            }
        });

        grid1.message().localListen(null, new P2<UUID, Object>() {
            @Override public boolean apply(UUID nodeId, Object msg) {
                try {
                    log.info("Received new message [msg=" + msg + ", senderNodeId=" + nodeId +
                        ", topic=default]");

                    if (!nodeId.equals(grid1.localNode().id())) {
                        log.error("Unexpected sender node: " + nodeId);

                        error.set(true);

                        return false;
                    }

                    if (!MSG_3.equals(msg)) {
                        log.error("Unexpected message " + msg + " for topic: default");

                        error.set(true);

                        return false;
                    }

                    rcvMsgs.add(msg);

                    return true;
                }
                finally {
                    rcvLatch.countDown();
                }
            }
        });

        GridProjection rNode1 = grid1.forLocal();

        rNode1.message().send(TestTopic.TOPIC_1, MSG_1);
        rNode1.message().send(TestTopic.TOPIC_2, MSG_2);
        rNode1.message().send(null, MSG_3);

        assertTrue(rcvLatch.await(3, TimeUnit.SECONDS));

        assertFalse(error.get());

        assertTrue(rcvMsgs.contains(MSG_1));
        assertTrue(rcvMsgs.contains(MSG_2));
        assertTrue(rcvMsgs.contains(MSG_3));
    }

    /**
     * Tests simple message sending-receiving with the use of
     * remoteListenAsync() method.
     *
     * @throws Exception If error occurs.
     */
    public void testRemoteListenAsync() throws Exception {
        final Collection<Object> rcvMsgs = new HashSet<>();

        final CountDownLatch rcvLatch = new CountDownLatch(4);

        grid2.message().remoteListen(null, new P2<UUID, Object>() {
            @Override public boolean apply(UUID nodeId, Object msg) {
                try {
                    log.info("Received new message [msg=" + msg + ", senderNodeId=" + nodeId + ']');

                    rcvMsgs.add(msg);

                    return true;
                }
                finally {
                    rcvLatch.countDown();
                }
            }
        }).get();

        GridProjection prj2 = grid1.forRemotes(); // Includes node from grid2.

        prj2.message().send(null, MSG_1);
        prj2.message().send(null, MSG_2);
        grid2.forLocal().message().send(null, MSG_3);

        assertFalse(rcvLatch.await(3, TimeUnit.SECONDS)); // We should get only 3 message.

        assertTrue(rcvMsgs.contains(MSG_1));
        assertTrue(rcvMsgs.contains(MSG_2));
        assertTrue(rcvMsgs.contains(MSG_3));
    }

    /**
     * @throws Exception If failed.
     */
    @SuppressWarnings("TooBroadScope")
    public void testStopRemoteListen() throws Exception {
        final AtomicInteger msgCnt1 = new AtomicInteger();

        final AtomicInteger msgCnt2 = new AtomicInteger();

        final AtomicInteger msgCnt3 = new AtomicInteger();

        final String topic1 = null;
        final String topic2 = "top2";
        final String topic3 = "top3";

        UUID id1 = grid2.message().remoteListen(topic1, new P2<UUID, Object>() {
            @Override public boolean apply(UUID nodeId, Object msg) {
                System.out.println(Thread.currentThread().getName() + " Listener1 received new message [msg=" + msg + ", senderNodeId=" + nodeId + ']');

                msgCnt1.incrementAndGet();

                return true;
            }
        }).get();

        UUID id2 = grid2.message().remoteListen(topic2, new P2<UUID, Object>() {
            @Override public boolean apply(UUID nodeId, Object msg) {
                System.out.println(Thread.currentThread().getName() + " Listener2 received new message [msg=" + msg + ", senderNodeId=" + nodeId + ']');

                msgCnt2.incrementAndGet();

                return true;
            }
        }).get();

        UUID id3 = grid2.message().remoteListen(topic3, new P2<UUID, Object>() {
            @Override public boolean apply(UUID nodeId, Object msg) {
                System.out.println(Thread.currentThread().getName() + " Listener3 received new message [msg=" + msg + ", senderNodeId=" + nodeId + ']');

                msgCnt3.incrementAndGet();

                return true;
            }
        }).get();

        grid1.forRemotes().message().send(topic1, "msg1-1");
        grid1.forRemotes().message().send(topic2, "msg1-2");
        grid1.forRemotes().message().send(topic3, "msg1-3");

        GridTestUtils.waitForCondition(new PA() {
            @Override public boolean apply() {
                return msgCnt1.get() > 0 && msgCnt2.get() > 0 && msgCnt3.get() > 0;
            }
        }, 5000);

        assertEquals(1, msgCnt1.get());
        assertEquals(1, msgCnt2.get());
        assertEquals(1, msgCnt3.get());

        grid2.message().stopRemoteListen(id2).get();

        grid1.forRemotes().message().send(topic1, "msg2-1");
        grid1.forRemotes().message().send(topic2, "msg2-2");
        grid1.forRemotes().message().send(topic3, "msg2-3");

        GridTestUtils.waitForCondition(new PA() {
            @Override public boolean apply() {
                return msgCnt1.get() > 1 && msgCnt3.get() > 1;
            }
        }, 5000);

        assertEquals(2, msgCnt1.get());
        assertEquals(1, msgCnt2.get());
        assertEquals(2, msgCnt3.get());

        grid2.message().stopRemoteListen(id2).get(); // Try remove one more time.

        grid2.message().stopRemoteListen(id1).get();
        grid2.message().stopRemoteListen(id3).get();

        grid1.forRemotes().message().send(topic1, "msg3-1");
        grid1.forRemotes().message().send(topic2, "msg3-2");
        grid1.forRemotes().message().send(topic3, "msg3-3");

        U.sleep(1000);

        assertEquals(2, msgCnt1.get());
        assertEquals(1, msgCnt2.get());
        assertEquals(2, msgCnt3.get());
    }

    /**
     * Tests simple message sending-receiving with the use of
     * remoteListenAsync() method.
     *
     * @throws Exception If error occurs.
     */
    public void testRemoteListenAsyncOrderedMessages() throws Exception {
        List<TestMessage> msgs = Arrays.asList(
            new TestMessage(MSG_1),
            new TestMessage(MSG_2, 3000),
            new TestMessage(MSG_3));

        final Collection<Object> rcvMsgs = new ArrayList<>(msgs.size());

        final AtomicBoolean error = new AtomicBoolean(false); //to make it modifiable

        final CountDownLatch rcvLatch = new CountDownLatch(3);

        grid2.message().remoteListen(S_TOPIC_1, new P2<UUID, Object>() {
            @Override
            public boolean apply(UUID nodeId, Object msg) {
                try {
                    log.info("Received new message [msg=" + msg + ", senderNodeId=" + nodeId + ']');

                    if (!nodeId.equals(grid1.localNode().id())) {
                        log.error("Unexpected sender node: " + nodeId);

                        error.set(true);

                        return false;
                    }

                    rcvMsgs.add(msg);

                    return true;
                }
                finally {
                    rcvLatch.countDown();
                }
            }
        }).get();

        GridProjection prj2 = grid1.forRemotes(); // Includes node from grid2.

        for (TestMessage msg : msgs)
            prj2.message().sendOrdered(S_TOPIC_1, msg, 15000);

        assertTrue(rcvLatch.await(6, TimeUnit.SECONDS));

        assertFalse(error.get());

        //noinspection AssertEqualsBetweenInconvertibleTypes
        assertEquals(msgs, Arrays.asList(rcvMsgs.toArray()));
    }

    /**
     * Tests simple message sending-receiving with the use of
     * remoteListenAsync() method and topics.
     *
     * @throws Exception If error occurs.
     */
    public void testRemoteListenAsyncWithIntTopic() throws Exception {
        final Collection<Object> rcvMsgs = new HashSet<>();

        final AtomicBoolean error = new AtomicBoolean(false); //to make it modifiable

        final CountDownLatch rcvLatch = new CountDownLatch(3);

        grid2.message().remoteListen(I_TOPIC_1, new P2<UUID, Object>() {
            @GridInstanceResource
            private transient Grid g;

            @Override public boolean apply(UUID nodeId, Object msg) {
                assertEquals(grid2, g);

                try {
                    log.info("Received new message [msg=" + msg + ", senderNodeId=" + nodeId +
                        ", topic=" + I_TOPIC_1 + ']');

                    if (!nodeId.equals(grid1.localNode().id())) {
                        log.error("Unexpected sender node: " + nodeId);

                        error.set(true);

                        return false;
                    }

                    if (!MSG_1.equals(msg)) {
                        log.error("Unexpected message " + msg + " for topic: " + I_TOPIC_1);

                        error.set(true);

                        return false;
                    }

                    rcvMsgs.add(msg);

                    return true;
                }
                finally {
                    rcvLatch.countDown();
                }
            }
        }).get();

        grid2.message().remoteListen(I_TOPIC_2, new P2<UUID, Object>() {
            @GridInstanceResource
            private transient Grid g;

            @Override public boolean apply(UUID nodeId, Object msg) {
                assertEquals(grid2, g);

                try {
                    log.info("Received new message [msg=" + msg + ", senderNodeId=" + nodeId +
                        ", topic=" + I_TOPIC_2 + ']');

                    if (!nodeId.equals(grid1.localNode().id())) {
                        log.error("Unexpected sender node: " + nodeId);

                        error.set(true);

                        return false;
                    }

                    if (!MSG_2.equals(msg)) {
                        log.error("Unexpected message " + msg + " for topic: " + I_TOPIC_2);

                        error.set(true);

                        return false;
                    }

                    rcvMsgs.add(msg);

                    return true;
                }
                finally {
                    rcvLatch.countDown();
                }
            }
        }).get();

        grid2.message().remoteListen(null, new P2<UUID, Object>() {
            @GridInstanceResource
            private transient Grid g;

            @Override public boolean apply(UUID nodeId, Object msg) {
                assertEquals(grid2, g);

                try {
                    log.info("Received new message [msg=" + msg + ", senderNodeId=" + nodeId +
                        ", topic=default]");

                    if (!nodeId.equals(grid1.localNode().id())) {
                        log.error("Unexpected sender node: " + nodeId);

                        error.set(true);

                        return false;
                    }

                    if (!MSG_3.equals(msg)) {
                        log.error("Unexpected message " + msg + " for topic: default");

                        error.set(true);

                        return false;
                    }

                    rcvMsgs.add(msg);

                    return true;
                }
                finally {
                    rcvLatch.countDown();
                }
            }
        }).get();

        GridProjection prj2 = grid1.forRemotes(); // Includes node from grid2.

        prj2.message().send(I_TOPIC_1, MSG_1);
        prj2.message().send(I_TOPIC_2, MSG_2);
        prj2.message().send(null, MSG_3);

        assertTrue(rcvLatch.await(3, TimeUnit.SECONDS));

        assertFalse(error.get());

        assertTrue(rcvMsgs.contains(MSG_1));
        assertTrue(rcvMsgs.contains(MSG_2));
        assertTrue(rcvMsgs.contains(MSG_3));
    }

    /**
     * Checks, if it is OK to send the message, loaded with external
     * class loader.
     *
     * @throws Exception If error occurs.
     */
    public void testSendMessageWithExternalClassLoader() throws Exception {
        URL[] urls = new URL[] { new URL(GridTestProperties.getProperty("p2p.uri.cls")) };

        ClassLoader extLdr = new URLClassLoader(urls);

        Class rcCls = extLdr.loadClass(EXT_RESOURCE_CLS_NAME);

        final AtomicBoolean error = new AtomicBoolean(false); //to make it modifiable

        final CountDownLatch rcvLatch = new CountDownLatch(1);

        grid2.message().remoteListen(S_TOPIC_1, new P2<UUID, Object>() {
            @Override public boolean apply(UUID nodeId, Object msg) {
                try {
                    log.info("Received new message [msg=" + msg + ", senderNodeId=" + nodeId + ']');

                    if (!nodeId.equals(grid1.localNode().id())) {
                        log.error("Unexpected sender node: " + nodeId);

                        error.set(true);

                        return false;
                    }

                    return true;
                }
                finally {
                    rcvLatch.countDown();
                }
            }
        }).get();

        grid1.forRemotes().message().send(S_TOPIC_1, Collections.singleton(rcCls.newInstance()));

        assertTrue(rcvLatch.await(3, TimeUnit.SECONDS));

        assertFalse(error.get());
    }

    /**
     * Test case for {@code null} messages.
     *
     * @throws Exception If failed.
     */
    @SuppressWarnings("ConstantConditions")
    public void testNullMessages() throws Exception {
        assertThrows(log, new Callable<Object>() {
            @Override public Object call() throws Exception {
                grid1.message().send(null, null);

                return null;
            }
        }, IllegalArgumentException.class, "Ouch! Argument is invalid: msgs cannot be null or empty");

        assertThrows(log, new Callable<Object>() {
            @Override public Object call() throws Exception {
                grid1.message().send(null, Collections.emptyList());

                return null;
            }
        }, IllegalArgumentException.class, "Ouch! Argument is invalid: msgs cannot be null or empty");

        assertThrows(log, new Callable<Object>() {
            @Override public Object call() throws Exception {
                grid1.message().send(null, (Object)null);

                return null;
            }
        }, NullPointerException.class, "Ouch! Argument cannot be null: msg");

        assertThrows(log, new Callable<Object>() {
            @Override public Object call() throws Exception {
                grid1.message().send(null, Arrays.asList(null, new Object()));

                return null;
            }
        }, NullPointerException.class, "Ouch! Argument cannot be null: msg");
    }
}
