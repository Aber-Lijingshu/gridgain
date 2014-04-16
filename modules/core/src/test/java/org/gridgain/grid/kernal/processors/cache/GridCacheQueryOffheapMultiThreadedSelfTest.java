/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.cache;

/**
 * Queries over off-heap indexes.
 */
public class GridCacheQueryOffheapMultiThreadedSelfTest extends GridCacheQueryMultiThreadedSelfTest {
    /** {@inheritDoc} */
    @Override protected boolean offheapEnabled() {
        return true;
    }

    /** {@inheritDoc} */
    @Override protected boolean evictsEnabled() {
        return true;
    }
}
