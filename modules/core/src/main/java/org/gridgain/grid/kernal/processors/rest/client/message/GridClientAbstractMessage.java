/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */
package org.gridgain.grid.kernal.processors.rest.client.message;

import org.gridgain.grid.util.typedef.internal.*;
import org.gridgain.portable.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.*;

/**
 * This class provides implementation for commit message fields and cannot be used directly.
 */
public abstract class GridClientAbstractMessage implements GridClientMessage, Externalizable, GridPortable {
    /** */
    private static final long serialVersionUID = 0L;

    /** */
    private static AtomicInteger typeIdGen = new AtomicInteger();

    /**
     * @return Portable type identifier for internal client messages classes.
     */
    static int nextSystemTypeId() {
        return typeIdGen.decrementAndGet();
    }

    /** Request ID (transient). */
    private transient long reqId;

    /** Client ID (transient). */
    private transient UUID id;

    /** Node ID (transient). */
    private transient UUID destId;

    /** Session token. */
    private byte[] sesTok;

    /** {@inheritDoc} */
    @Override public long requestId() {
        return reqId;
    }

    /** {@inheritDoc} */
    @Override public void requestId(long reqId) {
        this.reqId = reqId;
    }

    /** {@inheritDoc} */
    @Override public UUID clientId() {
        return id;
    }

    /** {@inheritDoc} */
    @Override public void clientId(UUID id) {
        this.id = id;
    }

    /** {@inheritDoc} */
    @Override public UUID destinationId() {
        return destId;
    }

    /** {@inheritDoc} */
    @Override public void destinationId(UUID destId) {
        this.destId = destId;
    }

    /**
     * @return Session token
     */
    @Override public byte[] sessionToken() {
        return sesTok;
    }

    /**
     * @param sesTok Session token.
     */
    @Override public void sessionToken(byte[] sesTok) {
        this.sesTok = sesTok;
    }

    /** {@inheritDoc} */
    @Override public void writeExternal(ObjectOutput out) throws IOException {
        U.writeByteArray(out, sesTok);
    }

    /** {@inheritDoc} */
    @Override public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        sesTok = U.readByteArray(in);
    }

    /** {@inheritDoc} */
    @Override public void writePortable(GridPortableWriter writer) throws GridPortableException {
        GridPortableRawWriter raw = writer.rawWriter();

        raw.writeByteArray(sesTok);
    }

    /** {@inheritDoc} */
    @Override public void readPortable(GridPortableReader reader) throws GridPortableException {
        GridPortableRawReader raw = reader.rawReader();

        sesTok = raw.readByteArray();
    }
}
