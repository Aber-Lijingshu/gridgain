/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.ggfs.mapreduce;

import org.gridgain.grid.ggfs.*;
import org.gridgain.grid.util.typedef.internal.*;

/**
 * Entity representing part of GGFS file identified by file path, start position, and length.
 *
 * @author @java.author
 * @version @java.version
 */
public class GridGgfsFileRange {
    /** File path. */
    private GridGgfsPath path;

    /** Start position. */
    private long start;

    /** Length. */
    private long len;

    /**
     * Creates file range.
     *
     * @param path File path.
     * @param start Start position.
     * @param len Length.
     */
    public GridGgfsFileRange(GridGgfsPath path, long start, long len) {
        this.path = path;
        this.start = start;
        this.len = len;
    }

    /**
     * Gets file path.
     *
     * @return File path.
     */
    public GridGgfsPath path() {
        return path;
    }

    /**
     * Gets range start position.
     *
     * @return Start position.
     */
    public long start() {
        return start;
    }

    /**
     * Gets range length.
     *
     * @return Length.
     */
    public long length() {
        return len;
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(GridGgfsFileRange.class, this);
    }
}
