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
 * Exception thrown when target file's block is not found in data cache.
 */
public class GridGgfsCorruptedFileException extends GridGgfsException {
    /**
     * @param msg Error message.
     */
    public GridGgfsCorruptedFileException(String msg) {
        super(msg);
    }

    /**
     * @param cause Error cause.
     */
    public GridGgfsCorruptedFileException(Throwable cause) {
        super(cause);
    }

    /**
     * @param msg Error message.
     * @param cause Error cause.
     */
    public GridGgfsCorruptedFileException(String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
