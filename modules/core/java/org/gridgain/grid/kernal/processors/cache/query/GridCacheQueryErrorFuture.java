/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.cache.query;

import org.gridgain.grid.*;
import org.gridgain.grid.cache.query.*;
import org.gridgain.grid.kernal.*;
import org.gridgain.grid.util.future.*;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * Error future.
 *
 * @author @java.author
 * @version @java.version
 */
public class GridCacheQueryErrorFuture<T> extends GridFinishedFuture<Collection<T>> implements GridCacheQueryFuture<T> {
    /**
     * @param ctx Context.
     * @param err Error.
     */
    public GridCacheQueryErrorFuture(GridKernalContext ctx, Throwable err) {
        super(ctx, err);
    }

    /** {@inheritDoc} */
    @Override public int available() throws GridException {
        return 0;
    }

    /** {@inheritDoc} */
    @Nullable @Override public T next() throws GridException {
        return null;
    }
}
