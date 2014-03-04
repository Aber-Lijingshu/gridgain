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
 * Exception thrown when parent supposed to be a directory is a file.
 *
 * @author @java.author
 * @version @java.version
 */
public class GridGgfsParentNotDirectoryException extends GridGgfsInvalidPathException {
    /**
     * @param msg Error message.
     */
    public GridGgfsParentNotDirectoryException(String msg) {
        super(msg);
    }

    /**
     * @param cause Exception cause.
     */
    public GridGgfsParentNotDirectoryException(Throwable cause) {
        super(cause);
    }

    /**
     * @param msg Error message.
     * @param cause Exception cause.
     */
    public GridGgfsParentNotDirectoryException(String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
