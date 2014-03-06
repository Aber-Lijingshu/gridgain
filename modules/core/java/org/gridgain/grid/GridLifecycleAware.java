/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid;

/**
 * All components provided in GridGain configuration can implement this interface.
 * If a component implements this interface, then method {@link #start()} will be called
 * during grid startup and {@link #stop()} will be called during stop.
 */
public interface GridLifecycleAware {
    /**
     * Starts grid component, called on grid start.
     *
     * @throws GridException If failed.
     */
    public void start() throws GridException;

    /**
     * Stops grid component, called on grid shutdown.
     *
     * @throws GridException If failed.
     */
    public void stop() throws GridException;
}
