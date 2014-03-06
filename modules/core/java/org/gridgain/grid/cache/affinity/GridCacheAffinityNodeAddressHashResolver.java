/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.cache.affinity;

import org.gridgain.grid.*;
import org.gridgain.grid.util.typedef.internal.*;

/**
 * Node hash resolver which uses {@link GridNode#consistentId()} as alternate hash value.
 */
public class GridCacheAffinityNodeAddressHashResolver implements GridCacheAffinityNodeHashResolver {
    /** {@inheritDoc} */
    @Override public Object resolve(GridNode node) {
        return node.consistentId();
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(GridCacheAffinityNodeAddressHashResolver.class, this);
    }
}
