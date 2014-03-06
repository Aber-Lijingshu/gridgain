/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal;

import org.gridgain.grid.*;
import org.jetbrains.annotations.*;

/**
 * When log debug mode is disabled this exception should be logged in short form - without stack trace.
 */
public class GridInternalException extends GridException {
    /**
     * Creates new internal exception with given error message.
     *
     * @param msg Error message.
     */
    public GridInternalException(String msg) {
        super(msg);
    }

    /**
     * Creates new internal exception given throwable as a cause and
     * source of error message.
     *
     * @param cause Non-null throwable cause.
     */
    public GridInternalException(Throwable cause) {
        super(cause.getMessage(), cause);
    }


    /**
     * Creates new internal exception with given error message and
     * optional nested exception.
     *
     * @param msg Exception message.
     * @param cause Optional cause.
     */
    public GridInternalException(String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
