/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.ggfs;

/**
 * {@code GGFS} exception indicating that target resource is not found.
 *
 * @author @java.author
 * @version @java.version
 */
public class GridGgfsFileNotFoundException extends GridGgfsInvalidPathException {
    /**
     * Creates exception with error message specified.
     *
     * @param msg Error message.
     */
    public GridGgfsFileNotFoundException(String msg) {
        super(msg);
    }
}
