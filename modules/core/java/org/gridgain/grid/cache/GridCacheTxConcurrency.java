/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.cache;

import org.jetbrains.annotations.Nullable;

/**
 * Transaction concurrency control. See {@link GridCacheTx} for more information
 * on transaction concurrency controls.
 *
 * @author @java.author
 * @version @java.version
 */
public enum GridCacheTxConcurrency {
    /** Optimistic concurrency control. */
    OPTIMISTIC,

    /** Pessimistic concurrency control. */
    PESSIMISTIC;

    /** Enum values. */
    private static final GridCacheTxConcurrency[] VALS = values();

    /**
     * Efficiently gets enumerated value from its ordinal.
     *
     * @param ord Ordinal value.
     * @return Enumerated value or {@code null} if ordinal out of range.
     */
    @Nullable public static GridCacheTxConcurrency fromOrdinal(int ord) {
        return ord >= 0 && ord < VALS.length ? VALS[ord] : null;
    }
}
