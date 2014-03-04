/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.rest;

import org.gridgain.grid.*;
import org.gridgain.grid.lang.*;

import java.util.*;

/**
 * REST protocol.
 *
 * @author @java.author
 * @version @java.version
 */
public interface GridRestProtocol {
    /**
     * @return Protocol name.
     */
    public abstract String name();

    /**
     * Returns protocol properties for setting node attributes. Has meaningful result
     * only after protocol start.
     *
     * @return Protocol properties.
     */
    public abstract Collection<GridBiTuple<String, Object>> getProperties();

    /**
     * Starts protocol.
     *
     * @param hnd Command handler.
     * @throws GridException If failed.
     */
    public abstract void start(GridRestProtocolHandler hnd) throws GridException;

    /**
     * Stops protocol.
     */
    public abstract void stop();
}
