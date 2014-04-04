/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.cache.distributed.replicated;

import org.gridgain.grid.cache.*;
import org.gridgain.grid.kernal.processors.cache.distributed.*;

import static org.gridgain.grid.cache.GridCacheAtomicityMode.*;
import static org.gridgain.grid.cache.GridCacheMemoryMode.*;
import static org.gridgain.grid.cache.GridCacheMode.*;
import static org.gridgain.grid.cache.GridCacheWriteSynchronizationMode.*;

/**
 * Tests for byte array values in REPLICATED caches.
 */
public abstract class GridCacheAbstractReplicatedByteArrayValuesSelfTest extends
    GridCacheAbstractDistributedByteArrayValuesSelfTest {
    /** {@inheritDoc} */
    @Override protected GridCacheConfiguration cacheConfiguration0() {
        GridCacheConfiguration cfg = new GridCacheConfiguration();

        cfg.setCacheMode(REPLICATED);
        cfg.setAtomicityMode(TRANSACTIONAL);
        cfg.setWriteSynchronizationMode(FULL_SYNC);
        cfg.setTxSerializableEnabled(true);
        cfg.setSwapEnabled(true);
        cfg.setEvictSynchronized(false);
        cfg.setEvictNearSynchronized(false);

        return cfg;
    }

    /** {@inheritDoc} */
    @Override protected GridCacheConfiguration offheapCacheConfiguration0() {
        GridCacheConfiguration cfg = new GridCacheConfiguration();

        cfg.setCacheMode(REPLICATED);
        cfg.setAtomicityMode(TRANSACTIONAL);
        cfg.setWriteSynchronizationMode(FULL_SYNC);
        cfg.setTxSerializableEnabled(true);
        cfg.setMemoryMode(OFFHEAP_VALUES);
        cfg.setOffHeapMaxMemory(100 * 1024 * 1024);
        cfg.setQueryIndexEnabled(false);

        return cfg;
    }
}
