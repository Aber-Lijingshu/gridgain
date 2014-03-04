/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.util.lang;

import org.gridgain.grid.*;

/**
 * Closure that takes argument, returns result and do not implement {@code GridPeerDeployAware}.
 *
 * @author @java.author
 * @version @java.version
 */
public interface GridPlainClosure<T, R> {
    /**
     * @param arg Closure argument.
     * @return Closure execution result.
     * @throws GridException If error occurred.
     */
    public R apply(T arg) throws GridException;
}
