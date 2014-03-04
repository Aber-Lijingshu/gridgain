/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.ggfs;

import org.apache.hadoop.fs.*;
import org.gridgain.grid.*;

/**
 * Descriptor of an output stream opened to the secondary file system.
 *
 * @author @java.author
 * @version @java.version
 */
public class GridGgfsSecondaryOutputStreamDescriptor {
    /** Parent ID in the primary file system. */
    private final GridUuid parentId;

    /** File info in the primary file system. */
    private final GridGgfsFileInfo info;

    /** Output stream to the secondary file system. */
    private final FSDataOutputStream out;

    /**
     * Constructor.
     *
     * @param parentId Parent ID in the primary file system.
     * @param info File info in the primary file system.
     * @param out Output stream to the secondary file system.
     */
    GridGgfsSecondaryOutputStreamDescriptor(GridUuid parentId, GridGgfsFileInfo info, FSDataOutputStream out) {
        assert parentId != null;
        assert info != null;
        assert out != null;

        this.parentId = parentId;
        this.info = info;
        this.out = out;
    }

    /**
     * @return Parent ID in the primary file system.
     */
    GridUuid parentId() {
        return parentId;
    }

    /**
     * @return File info in the primary file system.
     */
    GridGgfsFileInfo info() {
        return info;
    }

    /**
     * @return Output stream to the secondary file system.
     */
    FSDataOutputStream out() {
        return out;
    }
}
