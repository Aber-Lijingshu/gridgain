/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.streamer;

import org.gridgain.grid.util.mbean.*;

/**
 * Streamer window MBean.
 *
 * @author @java.author
 * @version @java.version
 */
@GridMBeanDescription("MBean that provides access to streamer window description.")
public interface GridStreamerWindowMBean {
    /**
     * Gets window name.
     *
     * @return Window name.
     */
    @GridMBeanDescription("Window name.")
    public String getName();

    /**
     * Gets window class name.
     *
     * @return Window class name.
     */
    @GridMBeanDescription("Window class name.")
    public String getClassName();

    /**
     * Gets current window size.
     *
     * @return Current window size.
     */
    @GridMBeanDescription("Window size.")
    public int getSize();

    /**
     * Gets estimate for window eviction queue size.
     *
     * @return Eviction queue size estimate.
     */
    @GridMBeanDescription("Eviction queue size estimate.")
    public int getEvictionQueueSize();
}
