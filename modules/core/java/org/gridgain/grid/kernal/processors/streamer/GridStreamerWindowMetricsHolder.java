/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.streamer;

import org.gridgain.grid.streamer.*;

/**
 * Streamer window metrics holder.
 *
 * @author @java.author
 * @version @java.version
 */
public class GridStreamerWindowMetricsHolder implements GridStreamerWindowMetrics {
    /** Window instance. */
    private GridStreamerWindow window;

    /**
     * @param window Streamer window.
     */
    public GridStreamerWindowMetricsHolder(GridStreamerWindow window) {
        this.window = window;
    }

    /** {@inheritDoc} */
    @Override public String name() {
        return window.name();
    }

    /** {@inheritDoc} */
    @Override public int size() {
        return window.size();
    }

    /** {@inheritDoc} */
    @Override public int evictionQueueSize() {
        return window.evictionQueueSize();
    }
}
