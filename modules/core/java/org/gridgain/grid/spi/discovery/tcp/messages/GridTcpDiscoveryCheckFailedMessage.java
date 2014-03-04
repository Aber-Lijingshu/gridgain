/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.spi.discovery.tcp.messages;

import org.gridgain.grid.util.typedef.internal.*;

import java.io.*;
import java.util.*;

/**
 * Message telling joining node that it failed coordinator's validation check.
 *
 * @author @java.author
 * @version @java.version
 */
public class GridTcpDiscoveryCheckFailedMessage extends GridTcpDiscoveryAbstractMessage {
    /** Coordinator version. */
    private String err;

    /**
     * Public default no-arg constructor for {@link Externalizable} interface.
     */
    public GridTcpDiscoveryCheckFailedMessage() {
        // No-op.
    }

    /**
     * Constructor.
     *
     * @param creatorNodeId Creator node ID.
     * @param err Error message from coordinator.
     */
    public GridTcpDiscoveryCheckFailedMessage(UUID creatorNodeId, String err) {
        super(creatorNodeId);

        this.err = err;
    }

    /**
     * @return Error message from coordinator.
     */
    public String error() {
        return err;
    }

    /** {@inheritDoc} */
    @Override public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);

        U.writeString(out, err);
    }

    /** {@inheritDoc} */
    @Override public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);

        err = U.readString(in);
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(GridTcpDiscoveryCheckFailedMessage.class, this, "super", super.toString());
    }
}
