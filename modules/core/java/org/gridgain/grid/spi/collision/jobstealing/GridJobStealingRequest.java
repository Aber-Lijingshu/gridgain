/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.spi.collision.jobstealing;

import org.gridgain.grid.util.direct.*;
import org.gridgain.grid.util.typedef.internal.*;

import java.io.*;
import java.nio.*;

/**
 * Job stealing request.
 *
 * @author @java.author
 * @version @java.version
 */
public class GridJobStealingRequest extends GridTcpCommunicationMessageAdapter {
    /** Delta. */
    private int delta;

    /**
     * Required by {@link Externalizable}.
     */
    public GridJobStealingRequest() {
        // No-op.
    }

    /**
     * @param delta Delta.
     */
    GridJobStealingRequest(int delta) {
        this.delta = delta;
    }

    /**
     * @return Delta.
     */
    int delta() {
        return delta;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({"CloneDoesntCallSuperClone", "CloneCallsConstructors"})
    @Override public GridTcpCommunicationMessageAdapter clone() {
        GridJobStealingRequest _clone = new GridJobStealingRequest();

        clone0(_clone);

        return _clone;
    }

    /** {@inheritDoc} */
    @Override protected void clone0(GridTcpCommunicationMessageAdapter _msg) {
        GridJobStealingRequest _clone = (GridJobStealingRequest)_msg;

        _clone.delta = delta;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("all")
    @Override public boolean writeTo(ByteBuffer buf) {
        commState.setBuffer(buf);

        if (!commState.typeWritten) {
            if (!commState.putByte(directType()))
                return false;

            commState.typeWritten = true;
        }

        switch (commState.idx) {
            case 0:
                if (!commState.putInt(delta))
                    return false;

                commState.idx++;

        }

        return true;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("all")
    @Override public boolean readFrom(ByteBuffer buf) {
        commState.setBuffer(buf);

        switch (commState.idx) {
            case 0:
                if (buf.remaining() < 4)
                    return false;

                delta = commState.getInt();

                commState.idx++;

        }

        return true;
    }

    /** {@inheritDoc} */
    @Override public byte directType() {
        return 78;
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(GridJobStealingRequest.class, this);
    }
}
