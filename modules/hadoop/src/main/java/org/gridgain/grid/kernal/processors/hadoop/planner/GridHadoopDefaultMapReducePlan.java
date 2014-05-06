/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.hadoop.planner;

import org.gridgain.grid.hadoop.*;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * Map-reduce plan.
 */
public class GridHadoopDefaultMapReducePlan implements GridHadoopMapReducePlan {
    /** Mappers map. */
    private Map<UUID, Collection<GridHadoopFileBlock>> mappers;

    /** Reducers map. */
    private Map<UUID, int[]> reducers;

    /** */
    private int reducersCnt;

    /**
     * @param mappers Mappers map.
     * @param reducers Reducers map.
     */
    public GridHadoopDefaultMapReducePlan(Map<UUID, Collection<GridHadoopFileBlock>> mappers,
        Map<UUID, int[]> reducers) {
        this.mappers = mappers;
        this.reducers = reducers;

        if (reducers != null) {
            for (int[] rdcrs : reducers.values())
                reducersCnt += rdcrs.length;
        }
    }

    /** {@inheritDoc} */
    @Override public int reducers() {
        return reducersCnt;
    }

    /** {@inheritDoc} */
    @Override public UUID nodeForReducer(int reducer) {
        assert reducer >= 0 && reducer < reducersCnt : reducer;

        for (Map.Entry<UUID, int[]> entry : reducers.entrySet()) {
            for (int r : entry.getValue()) {
                if (r == reducer)
                    return entry.getKey();
            }
        }

        throw new IllegalStateException("Not found reducer index: " + reducer);
    }

    /** {@inheritDoc} */
    @Override @Nullable public Collection<GridHadoopFileBlock> mappers(UUID nodeId) {
        return mappers.get(nodeId);
    }

    /** {@inheritDoc} */
    @Override @Nullable public int[] reducers(UUID nodeId) {
        return reducers.get(nodeId);
    }

    /** {@inheritDoc} */
    @Override public Collection<UUID> mapperNodeIds() {
        return mappers.keySet();
    }

    /** {@inheritDoc} */
    @Override public Collection<UUID> reducerNodeIds() {
        return reducers.keySet();
    }
}
