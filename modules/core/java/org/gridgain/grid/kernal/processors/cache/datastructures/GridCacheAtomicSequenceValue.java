/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.cache.datastructures;

import org.gridgain.grid.kernal.processors.cache.*;
import org.gridgain.grid.util.typedef.internal.*;

import java.io.*;

/**
 * Sequence value.
 */
public final class GridCacheAtomicSequenceValue implements GridCacheInternal, Externalizable, Cloneable {
    /** Counter. */
    private long val;

    /**
     * Empty constructor required for {@link Externalizable}.
     */
    public GridCacheAtomicSequenceValue() {
        // No-op.
    }

    /**
     * Default constructor.
     *
     * @param val Initial value.
     */
    public GridCacheAtomicSequenceValue(long val) {
        this.val = val;
    }

    /**
     * @param val New value.
     */
    public void set(long val) {
        this.val = val;
    }

    /**
     * @return val Current value.
     */
    public long get() {
        return val;
    }

    /** {@inheritDoc} */
    @Override public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /** {@inheritDoc} */
    @Override public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(val);
    }

    /** {@inheritDoc} */
    @Override public void readExternal(ObjectInput in) throws IOException {
        val = in.readLong();
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(GridCacheAtomicSequenceValue.class, this);
    }
}
