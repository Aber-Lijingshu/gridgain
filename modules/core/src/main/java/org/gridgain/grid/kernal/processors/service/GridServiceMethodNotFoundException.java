// @java.file.header

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.service;

import org.gridgain.grid.*;

import java.util.*;

/**
 * Exception thrown if service is not found.
 *
 * @author @java.author
 * @version @java.version
 */
public class GridServiceMethodNotFoundException extends GridRuntimeException {
    /**
     * @param svcName Service name.
     */
    public GridServiceMethodNotFoundException(String svcName, String mtdName, Class<?>[] argTypes) {
        super("Service method node found on deployed service [svcName=" + svcName + ", mtdName=" + mtdName + ", argTypes=" +
            Arrays.toString(argTypes) + ']');
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return getClass() + ": " + getMessage();
    }
}
