/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.cache.eviction.random;

import org.gridgain.grid.util.mbean.*;

/**
 * MBean for {@code random} eviction policy.
 */
@GridMBeanDescription("MBean for random cache eviction policy.")
public interface GridCacheRandomEvictionPolicyMBean {
    /**
     * Gets maximum allowed cache size.
     *
     * @return Maximum allowed cache size.
     */
    @GridMBeanDescription("Maximum allowed cache size.")
    public int getMaxSize();

    /**
     * Sets maximum allowed cache size.
     *
     * @param max Maximum allowed cache size.
     */
    @GridMBeanDescription("Sets maximum allowed cache size.")
    public void setMaxSize(int max);
}
