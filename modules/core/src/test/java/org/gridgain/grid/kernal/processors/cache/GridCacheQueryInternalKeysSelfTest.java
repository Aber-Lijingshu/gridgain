/* @java.file.header */

/*  _________        _____ __________________        _____
*  __  ____/___________(_)______  /__  ____/______ ____(_)_______
*  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
*  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
*  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
*/

package org.gridgain.grid.kernal.processors.cache;

import org.gridgain.grid.*;
import org.gridgain.grid.cache.*;
import org.gridgain.grid.kernal.*;
import org.gridgain.grid.kernal.managers.indexing.*;
import org.gridgain.grid.kernal.processors.cache.datastructures.*;
import org.gridgain.grid.kernal.processors.cache.distributed.dht.*;
import org.gridgain.grid.kernal.processors.cache.distributed.near.*;
import org.gridgain.grid.spi.indexing.*;
import org.gridgain.grid.util.typedef.*;
import org.jetbrains.annotations.*;

import java.util.*;

import static org.gridgain.grid.cache.GridCacheMode.*;
import static org.gridgain.grid.cache.GridCachePreloadMode.*;

/**
 * Test for http://gridgain.jira.com/browse/GG-3979.
 */
public class GridCacheQueryInternalKeysSelfTest extends GridCacheAbstractSelfTest {
    /** Grid count. */
    private static final int GRID_CNT = 2;

    /** Entry count. */
    private static final int ENTRY_CNT = 10;

    /** {@inheritDoc} */
    @Override protected int gridCount() {
        return GRID_CNT;
    }

    /** {@inheritDoc} */
    @Override protected GridCacheMode cacheMode() {
        return PARTITIONED;
    }

    /** {@inheritDoc} */
    @Override protected GridCacheConfiguration cacheConfiguration(String gridName) throws Exception {
        GridCacheConfiguration cc = super.cacheConfiguration(gridName);

        cc.setQueryIndexEnabled(false);
        cc.setPreloadMode(SYNC);

        return cc;
    }

    /**
     * @throws Exception If failed.
     */
    @SuppressWarnings("unchecked")
    public void testInternalKeysPreloading() throws Exception {
        try {
            GridCache<Object, Object> cache = grid(0).cache(null);

            for (int i = 0; i < ENTRY_CNT; i++)
                cache.dataStructures().queue("queue" + i, Integer.MAX_VALUE, false, true);

            startGrid(GRID_CNT); // Start additional node.

            for (int i = 0; i < ENTRY_CNT; i++) {
                GridCacheInternalKey internalKey = new GridCacheInternalKeyImpl("queue" + i);

                Collection<GridNode> nodes = cache.affinity().mapKeyToPrimaryAndBackups(internalKey);

                for (GridNode n : nodes) {
                    Grid g = findGridForNodeId(n.id());

                    assertNotNull(g);

                    assertTrue("Affinity node doesn't contain internal key [key=" + internalKey + ", node=" + n + ']',
                        ((GridNearCacheAdapter)((GridKernal)g).internalCache()).dht().containsKey(internalKey, null));
                }
            }

            GridDhtCacheAdapter dht = ((GridNearCacheAdapter)((GridKernal)grid(GRID_CNT)).internalCache()).dht();

            int cacheSize = dht.map().size();

            GridIndexingManager idxMgr = dht.context().kernalContext().indexing();

            Collection<GridIndexingTypeDescriptor> types = idxMgr.types(null);

            info("Types: " + types);

            int idxSize = 0;

            for (GridIndexingTypeDescriptor type : types)
                idxSize += idxMgr.size(null, null, type.valueClass());

            info("Index size: " + idxSize);

            assertTrue("Check failed [cacheSize=" + cacheSize + ", idxSize=" + idxSize + ']',
                cacheSize == idxSize || cacheSize - 1 == idxSize);
        }
        finally {
            stopGrid(GRID_CNT);
        }
    }

    /**
     * Finds the {@link Grid}, which has a local node
     * with given ID.
     *
     * @param nodeId ID for grid's local node.
     * @return A grid instance or {@code null}, if the grid
     * is not found.
     */
    @Nullable private Grid findGridForNodeId(final UUID nodeId) {
        return F.find(G.allGrids(), null, new P1<Grid>() {
            @Override public boolean apply(Grid e) {
                return nodeId.equals(e.localNode().id());
            }
        });
    }
}
