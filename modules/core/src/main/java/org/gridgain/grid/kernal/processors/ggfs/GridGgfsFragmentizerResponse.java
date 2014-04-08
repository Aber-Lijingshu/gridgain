/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.ggfs;

import org.gridgain.grid.*;
import org.gridgain.grid.util.direct.*;

import java.io.*;
import java.nio.*;

/**
 * Fragmentizer response.
 */
public class GridGgfsFragmentizerResponse extends GridGgfsCommunicationMessage {
    /** File ID. */
    private GridUuid fileId;

    /**
     * Empty constructor required for {@link Externalizable}.
     */
    public GridGgfsFragmentizerResponse() {
        // No-op.
    }

    /**
     * @param fileId File ID.
     */
    public GridGgfsFragmentizerResponse(GridUuid fileId) {
        this.fileId = fileId;
    }

    /**
     * @return File ID.
     */
    public GridUuid fileId() {
        return fileId;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({"CloneDoesntCallSuperClone", "CloneCallsConstructors"})
    @Override public GridTcpCommunicationMessageAdapter clone() {
        GridGgfsFragmentizerResponse _clone = new GridGgfsFragmentizerResponse();

        clone0(_clone);

        return _clone;
    }

    /** {@inheritDoc} */
    @Override protected void clone0(GridTcpCommunicationMessageAdapter _msg) {
        super.clone0(_msg);

        GridGgfsFragmentizerResponse _clone = (GridGgfsFragmentizerResponse)_msg;

        _clone.fileId = fileId;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("all")
    @Override public boolean writeTo(ByteBuffer buf) {
        commState.setBuffer(buf);

        if (!super.writeTo(buf))
            return false;

        if (!commState.typeWritten) {
            if (!commState.putByte(directType()))
                return false;

            commState.typeWritten = true;
        }

        switch (commState.idx) {
            case 0:
                if (!commState.putGridUuid(fileId))
                    return false;

                commState.idx++;

        }

        return true;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("all")
    @Override public boolean readFrom(ByteBuffer buf) {
        commState.setBuffer(buf);

        if (!super.readFrom(buf))
            return false;

        switch (commState.idx) {
            case 0:
                GridUuid fileId0 = commState.getGridUuid();

                if (fileId0 == GRID_UUID_NOT_READ)
                    return false;

                fileId = fileId0;

                commState.idx++;

        }

        return true;
    }

    /** {@inheritDoc} */
    @Override public byte directType() {
        return 71;
    }
}
