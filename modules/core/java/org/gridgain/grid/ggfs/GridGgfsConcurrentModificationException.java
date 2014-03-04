/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.ggfs;

/**
 * {@code GGFS} exception indicating that file system structure was modified concurrently. This error
 * indicates that an operation performed in DUAL mode cannot proceed due to these changes.
 *
 * @author @java.author
 * @version @java.version
 */
public class GridGgfsConcurrentModificationException extends GridGgfsException {
    /**
     * Creates new exception.
     */
    public GridGgfsConcurrentModificationException() {
        super("File system structure was concurrently modified.", null);
    }
}
