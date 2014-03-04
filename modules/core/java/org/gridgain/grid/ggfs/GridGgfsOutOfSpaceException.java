/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.ggfs;

import org.jetbrains.annotations.*;

/**
 * {@code GGFS} exception that is thrown when it detected out-of-space condition.
 * It is thrown when number of writes written to a {@code GGFS} data nodes exceeds
 * its maximum value (that is configured per-node).
 *
 * @author @java.author
 * @version @java.version
 */
public class GridGgfsOutOfSpaceException extends GridGgfsException {
    /**
     * Creates exception with given error message.
     *
     * @param msg Error message.
     */
    public GridGgfsOutOfSpaceException(String msg) {
        super(msg);
    }

    /**
     * Creates an instance of exception with given exception cause.
     *
     * @param cause Exception cause.
     */
    public GridGgfsOutOfSpaceException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates an instance of GGFS exception with given error message and given exception cause.
     *
     * @param msg Error message.
     * @param cause Exception cause.
     */
    public GridGgfsOutOfSpaceException(String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
