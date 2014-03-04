/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.cache.distributed;

import org.gridgain.grid.kernal.processors.cache.*;
import org.jetbrains.annotations.*;

/**
 * Interface mostly for Near to DHT version mapping.
 *
 * @author @java.author
 * @version @java.version
 */
public interface GridCacheMappedVersion {
    /**
     * Mapping should occur only if this interface returns a non-null value.
     * 
     * @return Mapped version.
     */
    @Nullable public GridCacheVersion mappedVersion();
}
