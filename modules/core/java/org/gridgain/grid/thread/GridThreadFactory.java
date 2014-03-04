/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.thread;

import org.jetbrains.annotations.*;

import java.util.concurrent.*;

/**
 * This class provides implementation of {@link ThreadFactory} factory
 * for creating grid threads.
 *
 * @author @java.author
 * @version @java.version
 */
public class GridThreadFactory implements ThreadFactory {
    /** Grid name. */
    private final String gridName;

    /** Thread name. */
    private final String threadName;

    /**
     * Constructs new thread factory for given grid. All threads will belong
     * to the same default thread group.
     *
     * @param gridName Grid name.
     */
    public GridThreadFactory(String gridName) {
        this(gridName, "gridgain");
    }

    /**
     * Constructs new thread factory for given grid. All threads will belong
     * to the same default thread group.
     *
     * @param gridName Grid name.
     * @param threadName Thread name.
     */
    public GridThreadFactory(String gridName, String threadName) {
        this.gridName = gridName;
        this.threadName = threadName;
    }

    /** {@inheritDoc} */
    @Override public Thread newThread(@NotNull Runnable r) {
        return new GridThread(gridName, threadName, r);
    }
}
