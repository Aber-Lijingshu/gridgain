/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.ggfs.ggfs;

import junit.framework.*;
import org.gridgain.grid.ggfs.*;
import org.gridgain.grid.kernal.processors.ggfs.*;
import org.gridgain.grid.util.typedef.*;

import java.util.*;

import static org.gridgain.grid.ggfs.GridGgfsMode.*;

/**
 *
 */
public class GridGgfsModeResolverSelfTest extends TestCase {
    /** */
    private GridGgfsModeResolver resolver;

    /** {@inheritDoc} */
    @Override protected void setUp() throws Exception {
        resolver = new GridGgfsModeResolver(DUAL_SYNC, Arrays.asList(
            new T2<>(new GridGgfsPath("/a/b/"), PRIMARY),
            new T2<>(new GridGgfsPath("/a/b/c/d"), PROXY)));
    }

    /**
     * @throws Exception If failed.
     */
    public void testResolve() throws Exception {
        assertEquals(DUAL_SYNC, resolver.resolveMode(new GridGgfsPath("/")));
        assertEquals(DUAL_SYNC, resolver.resolveMode(new GridGgfsPath("/a")));
        assertEquals(DUAL_SYNC, resolver.resolveMode(new GridGgfsPath("/a/1")));
        assertEquals(PRIMARY, resolver.resolveMode(new GridGgfsPath("/a/b")));
        assertEquals(PRIMARY, resolver.resolveMode(new GridGgfsPath("/a/b/c")));
        assertEquals(PRIMARY, resolver.resolveMode(new GridGgfsPath("/a/b/c/2")));
        assertEquals(PROXY, resolver.resolveMode(new GridGgfsPath("/a/b/c/d")));
        assertEquals(PROXY, resolver.resolveMode(new GridGgfsPath("/a/b/c/d/e")));
    }

    /**
     * @throws Exception If failed.
     */
    public void testResolveChildren() throws Exception {
        assertEquals(new HashSet<GridGgfsMode>(){{add(DUAL_SYNC); add(PRIMARY); add(PROXY);}},
            resolver.resolveChildrenModes(new GridGgfsPath("/")));
        assertEquals(new HashSet<GridGgfsMode>(){{add(DUAL_SYNC); add(PRIMARY); add(PROXY);}},
            resolver.resolveChildrenModes(new GridGgfsPath("/a")));
        assertEquals(new HashSet<GridGgfsMode>(){{add(DUAL_SYNC);}},
            resolver.resolveChildrenModes(new GridGgfsPath("/a/1")));
        assertEquals(new HashSet<GridGgfsMode>(){{add(PRIMARY); add(PROXY);}},
            resolver.resolveChildrenModes(new GridGgfsPath("/a/b")));
        assertEquals(new HashSet<GridGgfsMode>(){{add(PRIMARY); add(PROXY);}},
            resolver.resolveChildrenModes(new GridGgfsPath("/a/b/c")));
        assertEquals(new HashSet<GridGgfsMode>(){{add(PRIMARY);}},
            resolver.resolveChildrenModes(new GridGgfsPath("/a/b/c/2")));
        assertEquals(new HashSet<GridGgfsMode>(){{add(PROXY);}},
            resolver.resolveChildrenModes(new GridGgfsPath("/a/b/c/d")));
        assertEquals(new HashSet<GridGgfsMode>(){{add(PROXY);}},
            resolver.resolveChildrenModes(new GridGgfsPath("/a/b/c/d/e")));
    }
}
