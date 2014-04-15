package org.gridgain.grid.marshaller.optimized;

import org.apache.commons.io.*;
import org.gridgain.grid.*;
import org.gridgain.grid.compute.*;
import org.gridgain.grid.marshaller.*;
import org.gridgain.grid.spi.discovery.tcp.ipfinder.*;
import org.gridgain.grid.spi.discovery.tcp.ipfinder.vm.*;
import org.gridgain.grid.util.typedef.internal.*;
import org.gridgain.testframework.junits.common.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.lang.reflect.*;
import java.lang.reflect.Proxy;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 *
 */
public class GridOptimizedMarshallerTest extends GridCommonAbstractTest {
    /**
     * Tests ability to marshal non-serializable objects.
     *
     * @throws GridException If marshalling failed.
     */
    public void testNonSerializable() throws GridException {
        GridOptimizedMarshaller marsh = new GridOptimizedMarshaller();

        marsh.setRequireSerializable(false);

        NonSerializable outObj = marsh.unmarshal(marsh.marshal(new NonSerializable(null)), null);

        outObj.checkAfterUnmarshalled();
    }

    /**
     * Tests ability to marshal non-serializable objects.
     *
     * @throws GridException If marshalling failed.
     */
    public void testNonSerializable1() throws GridException {
        GridOptimizedMarshaller marsh = new GridOptimizedMarshaller();

        marsh.setRequireSerializable(false);

        byte[] bytes = marsh.marshal(new GridTcpDiscoveryVmIpFinder());

        GridTcpDiscoveryIpFinder ipFinder = marsh.unmarshal(bytes, null);

        assertFalse(ipFinder.isShared());

        ipFinder = marsh.unmarshal(marsh.marshal(new GridTcpDiscoveryVmIpFinder(true)), null);

        assertTrue(ipFinder.isShared());
    }

    /**
     * Tests ability to marshal non-serializable objects.
     *
     * @throws GridException If marshalling failed.
     */
    public void testNonSerializable2() throws GridException {
        GridOptimizedMarshaller marsh = new GridOptimizedMarshaller();

        marsh.setRequireSerializable(false);

        GridTcpDiscoveryIpFinderAdapter ipFinder = new GridTcpDiscoveryIpFinderAdapter() {
            @Override public Collection<InetSocketAddress> getRegisteredAddresses() {
                return null;
            }

            @Override public void registerAddresses(Collection<InetSocketAddress> addrs) {
                //No-op.
            }

            @Override public void unregisterAddresses(Collection<InetSocketAddress> addrs) {
                //No-op.
            }
        };

        ipFinder.setShared(false);

        byte[] bytes = marsh.marshal(ipFinder);

        ipFinder = marsh.unmarshal(bytes, null);

        assertFalse(ipFinder.isShared());
    }

    /**
     * Tests ability to marshal non-serializable objects.
     *
     * @throws GridException If marshalling failed.
     */
    public void testNonSerializable3() throws GridException {
        GridOptimizedMarshaller marsh = new GridOptimizedMarshaller();

        marsh.setRequireSerializable(false);

        byte[] bytes = marsh.marshal(new GridTestTcpDiscoveryIpFinderAdapter());

        GridTcpDiscoveryIpFinder ipFinder = marsh.unmarshal(bytes, null);

        assertFalse(ipFinder.isShared());
    }

     /**
     * Tests ability to marshal non-serializable objects.
     *
     * @throws GridException If marshalling failed.
     */
    public void testNonSerializable4() throws GridException {
        GridOptimizedMarshaller marsh = new GridOptimizedMarshaller();

        marsh.setRequireSerializable(false);

        byte[] bytes = marsh.marshal(new GridMarshallerTestInheritedBean());

        info(Arrays.toString(bytes));

        GridMarshallerTestInheritedBean bean = marsh.unmarshal(bytes, null);

        assertTrue(bean.isFlag());
    }

     /**
     * Tests ability to marshal non-serializable objects.
     *
     * @throws GridException If marshalling failed.
     */
    public void testNonSerializable5() throws GridException {
        GridMarshaller marsh = new GridOptimizedMarshaller();

        byte[] bytes = marsh.marshal(true);

        Boolean val = marsh.unmarshal(bytes, null);

        assertTrue(val);
    }

    /**
     * Tests ability to marshal serializable objects.
     *
     * @throws GridException If marshalling failed.
     */
    public void testSerializable() throws GridException {
        GridMarshaller marsh = new GridOptimizedMarshaller();

        SomeSerializable outObj = marsh.unmarshal(marsh.marshal(new SomeSerializable(null)), null);

        outObj.checkAfterUnmarshalled();
    }

    /**
     * @throws GridException If failed.
     */
    public void testSerializableAfterChangingValue() throws GridException {
        GridMarshaller marsh = new GridOptimizedMarshaller();

        SomeSimpleSerializable newObj = new SomeSimpleSerializable();

        assert(newObj.flag);

        newObj.setFlagValue(false);

        assert(! newObj.flag);

        SomeSimpleSerializable outObj = marsh.unmarshal(marsh.marshal(newObj), null);

        assert (! outObj.flag);
    }

    /**
     * Tests ability to marshal externalizable objects.
     *
     * @throws GridException If marshalling failed.
     */
    public void testExternalizable() throws GridException {
        GridMarshaller marsh = new GridOptimizedMarshaller();

        ExternalizableA outObj = marsh.unmarshal(marsh.marshal(new ExternalizableA(null, true)), null);
        ExternalizableA outObj1 = marsh.unmarshal(marsh.marshal(new ExternalizableA(null, false)), null);

        assertNotNull(outObj);
        assertNotNull(outObj1);
    }

    /**
     * Tests {@link GridOptimizedMarshaller#setRequireSerializable(boolean)}.
     */
    public void testRequireSerializable() {
        GridOptimizedMarshaller marsh = new GridOptimizedMarshaller();

        marsh.setRequireSerializable(true);

        try {
            marsh.marshal(new NonSerializable(null));

            fail();
        }
        catch (GridException ignore) {
            // No-op.
        }
    }

    /**
     * Tests {@link GridOptimizedMarshaller#setClassNames(List)}.
     *
     * @throws GridException If marshalling failed.
     */
    public void testUserPreregisteredNames() throws GridException {
        Object obj = new SomeSerializable(null);

        // Clear caches.
        ((Map)U.staticField(GridOptimizedMarshallerUtils.class, "CLS_DESC_CACHE")).clear();
        GridOptimizedClassResolver.userClasses(null, null);

        GridMarshaller marsh = new GridOptimizedMarshaller();

        int size1 = marsh.marshal(obj).length;

        // Clear caches.
        ((Map)U.staticField(GridOptimizedMarshallerUtils.class, "CLS_DESC_CACHE")).clear();
        GridOptimizedClassResolver.userClasses(null, null);

        GridOptimizedMarshaller marshPreregistered = new GridOptimizedMarshaller();

        marshPreregistered.setClassNames(Arrays.asList(SomeSerializable.class.getName()));

        int size2 = marshPreregistered.marshal(obj).length;

        assertTrue(size1 > size2);
    }

    /**
     * Tests {@link GridOptimizedMarshaller#setClassNames(List)}.
     *
     * @throws GridException If marshalling failed.
     * @throws IOException If an I/O error occurs.
     */
    public void testUserPreregisteredNamesPath() throws GridException, IOException {
        Object obj = new SomeSerializable(null);

        // Clear caches.
        ((Map)U.staticField(GridOptimizedMarshallerUtils.class, "CLS_DESC_CACHE")).clear();
        GridOptimizedClassResolver.userClasses(null, null);

        GridMarshaller marsh = new GridOptimizedMarshaller();

        int size1 = marsh.marshal(obj).length;

        // Clear caches.
        ((Map)U.staticField(GridOptimizedMarshallerUtils.class, "CLS_DESC_CACHE")).clear();
        GridOptimizedClassResolver.userClasses(null, null);

        GridOptimizedMarshaller marshPreregistered = new GridOptimizedMarshaller();

        File namesFile = File.createTempFile("gg-", null);

        FileUtils.writeStringToFile(namesFile, SomeSerializable.class.getName(), "UTF-8");

        marshPreregistered.setClassNamesPath(namesFile.getAbsolutePath());

        int size2 = marshPreregistered.marshal(obj).length;

        assertTrue(size1 > size2);
    }

    /**
     * Tests {@link Proxy}.
     *
     * @throws GridException If marshalling failed.
     */
    public void testProxy() throws GridException {
        GridOptimizedMarshaller marsh = new GridOptimizedMarshaller();

        marsh.setRequireSerializable(false);

        SomeItf inItf = (SomeItf)Proxy.newProxyInstance(
            GridOptimizedMarshallerTest.class.getClassLoader(), new Class[] {SomeItf.class},
            new InvocationHandler() {
                private NonSerializable obj = new NonSerializable(null);

                @Override public Object invoke(Object proxy, Method mtd, Object[] args) throws Throwable {
                    obj.checkAfterUnmarshalled();

                    return 17;
                }
            }
        );

        SomeItf outItf = marsh.unmarshal(marsh.marshal(inItf), null);

        assertEquals(outItf.checkAfterUnmarshalled(), 17);
    }

    /**
     * @throws Exception If failed.
     */
    public void testDescriptorCache() throws Exception {
        try {
            Grid grid = startGridsMultiThreaded(2);

            String taskClsName = "org.gridgain.grid.tests.p2p.GridSingleSplitTestTask";
            String jobClsName = "org.gridgain.grid.tests.p2p.GridSingleSplitTestTask$GridSingleSplitTestJob";

            ClassLoader ldr = getExternalClassLoader();

            Class<? extends GridComputeTask<?, ?>> taskCls = (Class<? extends GridComputeTask<?, ?>>)ldr.loadClass(taskClsName);
            Class<? extends GridComputeTask<?, ?>> jobCls = (Class<? extends GridComputeTask<?, ?>>)ldr.loadClass(jobClsName);

            grid.compute().localDeployTask(taskCls, ldr);

            grid.compute().execute(taskClsName, 2).get();

            ConcurrentMap<Class<?>, GridOptimizedClassDescriptor> cache =
                U.staticField(GridOptimizedMarshallerUtils.class, "CLS_DESC_CACHE");

            assertTrue(cache.containsKey(jobCls));

            grid.compute().undeployTask(taskClsName);

            // Wait for undeploy.
            Thread.sleep(1000);

            assertFalse(cache.containsKey(jobCls));
        }
        finally {
            stopAllGrids();
        }
    }

    /**
     * @throws Exception If failed.
     */
    public void testPerformance() throws Exception {
        System.gc();

        checkPerformance(10000, 4);
    }

    /**
     * @param cnt Number of marshalling attempts.
     * @param tries Number of retries.
     * @throws Exception If failed.
     */
    private void checkPerformance(int cnt, int tries) throws Exception {
        GridMarshaller marsh = new GridOptimizedMarshaller();

        for (int j = 0; j < tries; j++) {
            System.gc();

            long start = System.currentTimeMillis();

            for (int i = 0; i < cnt; i++) {
                TestCacheKey key = new TestCacheKey("key", "id");

                TestCacheKey outKey = marsh.unmarshal(marsh.marshal(key), null);

                assert key.equals(outKey);
                assert key.hashCode() == outKey.hashCode();
            }

            info("Time non-serializable: " + (System.currentTimeMillis() - start));

            System.gc();

            start = System.currentTimeMillis();

            for (int i = 0; i < cnt; i++) {
                TestCacheKeySerializable key1 = new TestCacheKeySerializable("key", "id");

                TestCacheKeySerializable outKey = marsh.unmarshal(marsh.marshal(key1), null);

                assert key1.equals(outKey);
                assert key1.hashCode() == outKey.hashCode();
            }

            info("Time serializable: " + (System.currentTimeMillis() - start));

            System.gc();

            start = System.currentTimeMillis();

            for (int i = 0; i < cnt; i++) {
                TestCacheKeyExternalizable key2 = new TestCacheKeyExternalizable("key", "id");

                TestCacheKeyExternalizable outKey = marsh.unmarshal(marsh.marshal(key2), null);

                assert key2.equals(outKey);
                assert key2.hashCode() == outKey.hashCode();
            }

            info("Time externalizable: " + (System.currentTimeMillis() - start));

            info(">>>");
        }

        info(">>> Finished performance check <<<");
    }

    /**
     * Some non-serializable class.
     */
    @SuppressWarnings( {"PublicField","TransientFieldInNonSerializableClass","FieldMayBeStatic"})
    private static class NonSerializableA {
        /** */
        private final long longVal = 0x33445566778899AAL;

        /** */
        protected Short shortVal = (short)0xAABB;

        /** */
        public String[] strArr = {"AA","BB"};

        /** */
        public boolean flag1 = true;

        /** */
        public boolean flag2;

        /** */
        public Boolean flag3;

        /** */
        public Boolean flag4 = true;

        /** */
        public Boolean flag5 = false;

        /** */
        private transient int intVal = 0xAABBCCDD;

        /**
         * @param strArr Array.
         * @param shortVal Short value.
         */
        @SuppressWarnings( {"UnusedDeclaration"})
        private NonSerializableA(@Nullable String[] strArr, @Nullable Short shortVal) {
            // No-op.
        }

        /**
         * Checks correctness of the state after unmarshalling.
         */
        void checkAfterUnmarshalled() {
            assertEquals(longVal, 0x33445566778899AAL);

            assertEquals(shortVal.shortValue(), (short)0xAABB);

            assertTrue(Arrays.equals(strArr, new String[] {"AA","BB"}));

            assertEquals(intVal, 0);

            assertTrue(flag1);
            assertFalse(flag2);
            assertNull(flag3);
            assertTrue(flag4);
            assertFalse(flag5);
        }
    }

    /**
     * Some non-serializable class.
     */
    @SuppressWarnings( {"PublicField","TransientFieldInNonSerializableClass","PackageVisibleInnerClass"})
    static class NonSerializableB extends NonSerializableA {
        /** */
        public Short shortVal = 0x1122;

        /** */
        public long longVal = 0x8877665544332211L;

        /** */
        private transient NonSerializableA[] aArr = {
            new NonSerializableA(null, null),
            new NonSerializableA(null, null),
            new NonSerializableA(null, null)
        };

        /** */
        protected Double doubleVal = 123.456;

        /**
         * Just to eliminate the default constructor.
         */
        private NonSerializableB() {
            super(null, null);
        }

        /**
         * Checks correctness of the state after unmarshalling.
         */
        @Override void checkAfterUnmarshalled() {
            super.checkAfterUnmarshalled();

            assertEquals(shortVal.shortValue(), 0x1122);

            assertEquals(longVal, 0x8877665544332211L);

            assertNull(aArr);

            assertEquals(doubleVal, 123.456);
        }
    }

    /**
     * Some non-serializable class.
     */
    @SuppressWarnings( {"TransientFieldInNonSerializableClass","PublicField"})
    private static class NonSerializable extends NonSerializableB {
        /** */
        private int idVal = -17;

        /** */
        private final NonSerializableA aVal = new NonSerializableB();

        /** */
        private transient NonSerializableB bVal = new NonSerializableB();

        /** */
        private NonSerializableA[] bArr = new NonSerializableA[] {
            new NonSerializableB(),
            new NonSerializableA(null, null)
        };

        /** */
        public float floatVal = 567.89F;

        /**
         * Just to eliminate the default constructor.
         *
         * @param aVal Unused.
         */
        @SuppressWarnings( {"UnusedDeclaration"})
        private NonSerializable(NonSerializableA aVal) {
        }

        /**
         * Checks correctness of the state after unmarshalling.
         */
        @Override void checkAfterUnmarshalled() {
            super.checkAfterUnmarshalled();

            assertEquals(idVal, -17);

            aVal.checkAfterUnmarshalled();

            assertNull(bVal);

            for (NonSerializableA a : bArr) {
                a.checkAfterUnmarshalled();
            }

            assertEquals(floatVal, 567.89F);
        }
    }

    /**
     * Some serializable class.
     */
    @SuppressWarnings( {"PublicField","TransientFieldInNonSerializableClass","PackageVisibleInnerClass"})
    static class ForSerializableB {
        /** */
        public Short shortVal = 0x1122;

        /** */
        public long longVal = 0x8877665544332211L;

        /** */
        private transient NonSerializableA[] aArr;

        /** */
        private transient String strVal = "abc";

        /** */
        protected Double doubleVal = 123.456;

        /**
         */
        protected void init() {
            shortVal = 0x1122;

            longVal = 0x8877665544332211L;

            aArr = new NonSerializableA[] {
                new NonSerializableA(null, null),
                new NonSerializableA(null, null),
                new NonSerializableA(null, null)
            };
        }

        /**
         * Checks correctness of the state after unmarshalling.
         */
        void checkAfterUnmarshalled() {
            assertEquals(shortVal.shortValue(), 0x1122);

            assertEquals(longVal, 0x8877665544332211L);

            assertNull(aArr);

            assertNull(strVal);

            assertEquals(doubleVal, 123.456);
        }
    }

    /**
     * Some serializable class.
     */
    private static class SomeSimpleSerializable extends GridComputeJobAdapter {
        /** */
        private boolean flag = true;

        /**
         * @param newFlagVal - The new value of flag field.
         */
        public void setFlagValue(boolean newFlagVal) {
            flag = newFlagVal;
        }

        /** {@inheritDoc} */
        @Override public Object execute() throws GridException {
            assert false;

            return null;
        }
    }
    /**
     * Some serializable class.
     */
    private static class SomeSerializable extends ForSerializableB implements Serializable {
        /**
         * Just to eliminate the default constructor.
         *
         * @param id Unused.
         */
        @SuppressWarnings( {"UnusedDeclaration"})
        private SomeSerializable(Long id) {
            init();
        }
    }

    /**
     */
    private static interface SomeItf {
        /**
         * @return Check result.
         */
        int checkAfterUnmarshalled();
    }

    /**
     * Some externalizable class.
     */
    @SuppressWarnings( {"UnusedDeclaration", "PublicField"})
    private static class ExternalizableA implements Externalizable {
        /** */
        private boolean boolVal;

        /** */
        public String[] strArr;

        /** No-arg constructor is required by externalization.  */
        public ExternalizableA() {
            // No-op.
        }

        /**
         *
         * @param strArr String array.
         * @param boolVal Boolean value.
         */
        private ExternalizableA(String[] strArr, boolean boolVal) {
            this.strArr = strArr;
            this.boolVal = boolVal;
        }

        /** {@inheritDoc} */
        @Override public void writeExternal(ObjectOutput out) throws IOException {
            out.writeBoolean(false);
            out.writeBoolean(false);
            out.writeBoolean(false);
            out.writeBoolean(false);
            out.writeBoolean(false);
            out.writeBoolean(false);
        }

        /** {@inheritDoc} */
        @Override public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            boolVal = in.readBoolean();
            in.readBoolean();
            in.readBoolean();
            in.readBoolean();
            in.readBoolean();
            in.readBoolean();
        }
    }

    /**
     *
     */
    private static class TestCacheKey implements Serializable {
        /** */
        private String key;

        /** */
        @SuppressWarnings({"UnusedDeclaration"})
        private String terminalId;

        /**
         * @param key Key.
         * @param terminalId Some ID.
         */
        TestCacheKey(String key, String terminalId) {
            this.key = key;
            this.terminalId = terminalId;
        }

        /** {@inheritDoc} */
        @Override public int hashCode() {
            return key.hashCode();
        }

        /** {@inheritDoc} */
        @Override public boolean equals(Object obj) {
            return obj instanceof TestCacheKey && key.equals(((TestCacheKey)obj).key);
        }
    }

    /**
     *
     */
    private static class TestCacheKeySerializable implements Serializable {
        /** */
        private String key;

        /** */
        @SuppressWarnings({"UnusedDeclaration"})
        private String terminalId;

        /**
         * @param key Key.
         * @param terminalId Some ID.
         */
        TestCacheKeySerializable(String key, String terminalId) {
            this.key = key;
            this.terminalId = terminalId;
        }

        /** {@inheritDoc} */
        @Override public int hashCode() {
            return key.hashCode();
        }

        /** {@inheritDoc} */
        @Override public boolean equals(Object obj) {
            return obj instanceof TestCacheKeySerializable && key.equals(((TestCacheKeySerializable)obj).key);
        }
    }

    /**
     *
     */
    private static class TestCacheKeyExternalizable implements Externalizable {
        /** */
        private String key;

        /** */
        private String terminalId;

        /**
         *
         */
        public TestCacheKeyExternalizable() {
            // No-op.
        }

        /**
         * @param key Key.
         * @param terminalId Some ID.
         */
        TestCacheKeyExternalizable(String key, String terminalId) {
            this.key = key;
            this.terminalId = terminalId;
        }

        /** {@inheritDoc} */
        @Override public int hashCode() {
            return key.hashCode();
        }

        /** {@inheritDoc} */
        @Override public boolean equals(Object obj) {
            return obj instanceof TestCacheKeyExternalizable && key.equals(((TestCacheKeyExternalizable)obj).key);
        }

        /** {@inheritDoc} */
        @Override public void writeExternal(ObjectOutput out) throws IOException {
            U.writeString(out, key);
            U.writeString(out, terminalId);
        }

        /** {@inheritDoc} */
        @Override public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            key = U.readString(in);
            terminalId = U.readString(in);
        }
    }
}
