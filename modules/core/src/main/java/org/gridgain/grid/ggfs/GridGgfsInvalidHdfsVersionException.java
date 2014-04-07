/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.ggfs;

/**
 * Exception thrown when GridGain detects that remote HDFS version differs from version of HDFS libraries
 * in GridGain classpath.
 */
public class GridGgfsInvalidHdfsVersionException extends GridGgfsException {
    /**
     * @param msg Error message.
     */
    public GridGgfsInvalidHdfsVersionException(String msg) {
        super(msg);
    }

    /**
     * @param msg Error message.
     * @param cause Error cause.
     */
    public GridGgfsInvalidHdfsVersionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
