/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.cache.query.jdbc;

import org.gridgain.grid.compute.*;
import org.gridgain.grid.*;
import org.gridgain.grid.kernal.processors.license.*;
import org.gridgain.grid.resources.*;
import org.gridgain.grid.util.typedef.*;
import org.gridgain.grid.util.typedef.internal.*;
import org.jetbrains.annotations.*;

import java.util.*;

import static org.gridgain.grid.product.GridProductEdition.*;

/**
 * Task to validate connection. Checks that cache with provided name exists in grid.
 */
public class GridCacheQueryJdbcValidationTask extends GridComputeTaskSplitAdapter<String, Boolean> {
    /** {@inheritDoc} */
    @Override protected Collection<? extends GridComputeJob> split(int gridSize,
        @Nullable final String cacheName) throws GridException {
        // Register big data usage.
        GridLicenseUseRegistry.onUsage(DATA_GRID, getClass());

        return F.asSet(new GridComputeJobAdapter() {
            @GridInstanceResource
            private Grid grid;

            @Override public Object execute() {
                for (GridNode n : grid.nodes())
                    if (U.hasCache(n, cacheName))
                        return true;

                return false;
            }
        });
    }

    /** {@inheritDoc} */
    @Override public Boolean reduce(List<GridComputeJobResult> results) throws GridException {
        return F.first(results).getData();
    }
}
