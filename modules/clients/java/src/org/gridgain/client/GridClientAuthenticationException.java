/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.client;

/**
 * Exception that represents client authentication failure.
 *
 * @author @java.author
 * @version @java.version
 */
public class GridClientAuthenticationException extends GridClientException {
    /**
     * Creates authentication exception with given error message.
     *
     * @param msg Error message.
     */
    public GridClientAuthenticationException(String msg) {
        super(msg);
    }
}
