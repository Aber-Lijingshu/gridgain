/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.events;

import org.gridgain.grid.*;
import org.gridgain.grid.lang.*;
import org.gridgain.grid.util.tostring.*;
import org.gridgain.grid.util.typedef.internal.*;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * Cache query read event.
 */
public class GridCacheQueryReadEvent extends GridCacheQueryEvent {
    /** Key. */
    @GridToStringInclude
    private final Object key;

    /** Value. */
    @GridToStringInclude
    private final Object val;

    /** Old value. */
    @GridToStringInclude
    private final Object oldVal;

    /**
     * @param node Node where event was fired.
     * @param msg Event message.
     * @param type Event type.
     * @param cacheName Cache name.
     * @param clsName Class name.
     * @param clause Clause.
     * @param scanFilter Scan query filter.
     * @param args Query arguments.
     * @param subjId Security subject ID.
     * @param key Key.
     * @param val Value.
     * @param oldVal Old value.
     */
    public GridCacheQueryReadEvent(GridNode node, String msg, int type, @Nullable String cacheName,
        @Nullable String clsName, @Nullable String clause, @Nullable GridBiPredicate<?, ?> scanFilter,
        @Nullable Object[] args, @Nullable UUID subjId, Object key, @Nullable Object val, @Nullable Object oldVal) {
        super(node, msg, type, cacheName, clsName, clause, scanFilter, args, subjId);

        assert key != null;

        this.key = key;
        this.val = val;
        this.oldVal = oldVal;
    }

    /**
     * Gets read entry key.
     *
     * @return Key.
     */
    public <K> K key() {
        return (K)key;
    }

    /**
     * Gets read entry value.
     *
     * @return Value.
     */
    @Nullable public <V> V value() {
        return (V)val;
    }

    /**
     * Gets read entry old value (applicable for continuous queries).
     *
     * @return Old value.
     */
    @Nullable public <V> V oldValue() {
        return (V)oldVal;
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(GridCacheQueryReadEvent.class, this, super.toString());
    }
}
