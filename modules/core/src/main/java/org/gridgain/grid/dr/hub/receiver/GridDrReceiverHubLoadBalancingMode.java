/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.dr.hub.receiver;

/**
 * Data center replication receiver hub load balancing mode. Determines to which receiver hub from the given remote
 * data center the next batch will be sent from the sender hub.
 */
public enum GridDrReceiverHubLoadBalancingMode {
    /**
     * Balance receiver hubs in random fashion.
     */
    DR_RANDOM,

    /**
     * Balance receiver hubs in round-robin fashion.
     */
    DR_ROUND_ROBIN
}
