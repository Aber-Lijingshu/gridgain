/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.cache.datastructures;

import org.gridgain.grid.cache.datastructures.*;

/**
 * Grid cache count down latch ({@code 'Ex'} stands for external).
 *
 * @author @java.author
 * @version @java.version
 */
public interface GridCacheCountDownLatchEx extends GridCacheCountDownLatch, GridCacheRemovable {
    /**
     * Get current count down latch key.
     *
     * @return Latch key.
     */
    public GridCacheInternalKey key();

    /**
     * Callback to notify latch on changes.
     *
     * @param count New count.
     */
    public void onUpdate(int count);
}
