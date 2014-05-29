package org.gridgain.grid.kernal.processors.rest.client.message;

import org.gridgain.grid.util.direct.*;
import org.gridgain.grid.util.typedef.internal.*;

import java.nio.*;

/**
 * Client handshake wrapper for direct marshalling.
 */
public class GridClientHandshakeResponseWrapper extends GridTcpCommunicationMessageAdapter {
    /** */
    private byte code;

    /**
     *
     */
    public GridClientHandshakeResponseWrapper() {
        // No-op.
    }

    /**
     * @param code Response code.
     */
    public GridClientHandshakeResponseWrapper(byte code) {
        this.code = code;
    }

    /** {@inheritDoc} */
    @Override public boolean writeTo(ByteBuffer buf) {
        commState.setBuffer(buf);

        if (!commState.typeWritten) {
            if (!commState.putByte(directType()))
                return false;

            commState.typeWritten = true;
        }

        return true;
    }

    /** {@inheritDoc} */
    @Override public boolean readFrom(ByteBuffer buf) {
        commState.setBuffer(buf);

        return true;
    }

    /** {@inheritDoc} */
    @Override public byte directType() {
        return code;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({"CloneDoesntCallSuperClone", "CloneCallsConstructors"})
    @Override public GridTcpCommunicationMessageAdapter clone() {
        GridClientHandshakeResponseWrapper _clone = new GridClientHandshakeResponseWrapper();

        clone0(_clone);

        return _clone;
    }

    /** {@inheritDoc} */
    @Override protected void clone0(GridTcpCommunicationMessageAdapter _msg) {
        GridClientHandshakeResponseWrapper _clone = (GridClientHandshakeResponseWrapper)_msg;

        _clone.code = code;
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(GridClientHandshakeResponseWrapper.class, this);
    }
}
