/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.cache.eviction;

import org.gridgain.grid.*;
import org.gridgain.grid.cache.*;

/**
 * FIXDOC: Add class description.
 */
public class GridCacheEmptyEntriesLocalSelfTest extends GridCacheEmptyEntriesAbstractSelfTest {
    /** {@inheritDoc} */
    @Override protected Grid startGrids() throws Exception {
        return startGrid();
    }

    /** {@inheritDoc} */
    @Override protected GridCacheMode cacheMode() {
        return GridCacheMode.LOCAL;
    }

    @Override public void testFifo() throws Exception {
        super.testFifo();
    }
}
