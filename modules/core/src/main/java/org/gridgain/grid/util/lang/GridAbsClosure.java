/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.util.lang;

import org.gridgain.grid.compute.*;

import java.io.*;

/**
 * Defines a convenient absolute, i.e. {@code no-arg} and {@code no return value} closure. This closure
 * that has {@code void} return type and no arguments (free variables).
 * <h2 class="header">Thread Safety</h2>
 * Note that this interface does not impose or assume any specific thread-safety by its
 * implementations. Each implementation can elect what type of thread-safety it provides,
 * if any.
 * <p>
 * Note that this class implements {@link GridComputeJob} interface for convenience and can be
 * used in {@link GridComputeTask} implementations directly, if needed, as an alternative to
 * {@link GridComputeJobAdapter}.
 * @see GridFunc
 */
public abstract class GridAbsClosure implements Runnable, Serializable {
    /**
     * Absolute closure body.
     */
    public abstract void apply();

    /**
     * Delegates to {@link #apply()} method.
     * <p>
     * {@inheritDoc}
     */
    @Override public final void run() {
        apply();
    }
}
