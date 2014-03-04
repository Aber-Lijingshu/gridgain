/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.cache.extras;

import org.gridgain.grid.kernal.processors.cache.*;
import org.gridgain.grid.util.*;

/**
 * Cache extras adapter.
 *
 * @author @java.author
 * @version @java.version
 */
public abstract class GridCacheEntryExtrasAdapter<K> implements GridCacheEntryExtras<K> {
    /** {@inheritDoc} */
    @Override public GridLeanMap<String, Object> attributesData() {
        return null;
    }

    /** {@inheritDoc} */
    @Override public GridCacheMvcc<K> mvcc() {
        return null;
    }

    /** {@inheritDoc} */
    @Override public GridCacheVersion obsoleteVersion() {
        return null;
    }

    /** {@inheritDoc} */
    @Override public long ttl() {
        return 0;
    }

    /** {@inheritDoc} */
    @Override public long expireTime() {
        return 0;
    }
}
