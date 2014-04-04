package org.gridgain.grid.tests.p2p;

import org.gridgain.grid.*;
import org.gridgain.grid.lang.*;
import org.gridgain.grid.util.typedef.internal.*;

import java.util.*;

/**
 * This node filter excludes the node with the given UUID
 * from the topology.
 */
public class GridExcludeNodeFilter implements GridPredicate<GridNode> {
    /** Node ID to exclude. */
    private final UUID excludeId;

    /**
     * @param excludeId Excluded node UUID.
     */
    public GridExcludeNodeFilter(UUID excludeId) {
        assert excludeId != null;

        this.excludeId = excludeId;
    }

    /** {@inheritDoc} */
    @Override public boolean apply(GridNode e) {
        return !excludeId.equals(e.id());
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(GridExcludeNodeFilter.class, this);
    }
}
