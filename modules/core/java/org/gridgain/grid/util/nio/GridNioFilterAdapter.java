/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.util.nio;

import org.gridgain.grid.*;

/**
 * Class that defines the piece for application-to-network and vice-versa data conversions
 * (protocol transformations, encryption, etc.)
 *
 * @author @java.author
 * @version @java.version
 */
public abstract class GridNioFilterAdapter implements GridNioFilter {
    /** Filter name. */
    private String name;

    /** Next filter in filter chain. */
    protected GridNioFilter nextFilter;

    /** Previous filter in filter chain. */
    protected GridNioFilter prevFilter;

    /**
     * Assigns filter name to a filter.
     *
     * @param name Filter name. Used in filter chain.
     */
    protected GridNioFilterAdapter(String name) {
        assert name != null;

        this.name = name;
    }

    /** {@inheritDoc} */
    public String toString() {
        return name;
    }

    /** {@inheritDoc} */
    @Override public void start() {
        // No-op.
    }

    /** {@inheritDoc} */
    @Override public void stop() {
        // No-op.
    }

    /** {@inheritDoc} */
    @Override public GridNioFilter nextFilter() {
        return nextFilter;
    }

    /** {@inheritDoc} */
    @Override public GridNioFilter previousFilter() {
        return prevFilter;
    }

    /** {@inheritDoc} */
    @Override public void nextFilter(GridNioFilter filter) {
        nextFilter = filter;
    }

    /** {@inheritDoc} */
    @Override public void previousFilter(GridNioFilter filter) {
        prevFilter = filter;
    }

    /** {@inheritDoc} */
    @Override public void proceedSessionOpened(GridNioSession ses) throws GridException {
        checkPrevious();

        prevFilter.onSessionOpened(ses);
    }

    /** {@inheritDoc} */
    @Override public void proceedSessionClosed(GridNioSession ses) throws GridException {
        checkPrevious();

        prevFilter.onSessionClosed(ses);
    }

    /** {@inheritDoc} */
    @Override public void proceedExceptionCaught(GridNioSession ses, GridException e) throws GridException {
        checkPrevious();

        prevFilter.onExceptionCaught(ses, e);
    }

    /** {@inheritDoc} */
    @Override public void proceedMessageReceived(GridNioSession ses, Object msg) throws GridException {
        checkPrevious();

        prevFilter.onMessageReceived(ses, msg);
    }

    /** {@inheritDoc} */
    @Override public GridNioFuture<?> proceedSessionWrite(GridNioSession ses, Object msg) throws GridException {
        checkNext();

        return nextFilter.onSessionWrite(ses, msg);
    }

    /** {@inheritDoc} */
    @Override public GridNioFuture<Boolean> proceedSessionClose(GridNioSession ses) throws GridException {
        checkNext();

        return nextFilter.onSessionClose(ses);
    }

    /** {@inheritDoc} */
    @Override public void proceedSessionIdleTimeout(GridNioSession ses) throws GridException {
        checkPrevious();

        prevFilter.onSessionIdleTimeout(ses);
    }

    /** {@inheritDoc} */
    @Override public void proceedSessionWriteTimeout(GridNioSession ses) throws GridException {
        checkPrevious();

        prevFilter.onSessionWriteTimeout(ses);
    }

    /** {@inheritDoc} */
    @Override public GridNioFuture<?> proceedPauseReads(GridNioSession ses) throws GridException {
        checkNext();

        return nextFilter.onPauseReads(ses);
    }

    /** {@inheritDoc} */
    @Override public GridNioFuture<?> proceedResumeReads(GridNioSession ses) throws GridException {
        checkNext();

        return nextFilter.onResumeReads(ses);
    }

    /** {@inheritDoc} */
    @Override public GridNioFuture<?> onPauseReads(GridNioSession ses) throws GridException {
        return proceedPauseReads(ses);
    }

    /** {@inheritDoc} */
    @Override public GridNioFuture<?> onResumeReads(GridNioSession ses) throws GridException {
        return proceedResumeReads(ses);
    }

    /**
     * Checks that previous filter is set.
     *
     * @throws GridNioException If previous filter is not set.
     */
    private void checkPrevious() throws GridNioException {
        if (prevFilter == null)
            throw new GridNioException("Failed to proceed with filter call since previous filter is not set " +
                "(do you use filter outside the filter chain?): " + getClass().getName());
    }

    /**
     * Checks that next filter is set.
     *
     * @throws GridNioException If next filter is not set.
     */
    private void checkNext() throws GridNioException {
        if (nextFilter == null)
            throw new GridNioException("Failed to proceed with filter call since previous filter is not set " +
                "(do you use filter outside the filter chain?): " + getClass().getName());
    }
}
