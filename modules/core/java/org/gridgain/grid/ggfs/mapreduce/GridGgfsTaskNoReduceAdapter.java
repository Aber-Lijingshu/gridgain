/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.ggfs.mapreduce;

import org.gridgain.grid.compute.*;

import java.util.*;

/**
 * Convenient {@link GridGgfsTask} adapter with empty reduce step. Use this adapter in case you are not interested in
 * results returned by jobs.
 *
 * @author @java.author
 * @version @java.version
 */
public abstract class GridGgfsTaskNoReduceAdapter<T, R> extends GridGgfsTask<T, R> {
    /**
     * Default implementation which will ignore all results sent from execution nodes.
     *
     * @param results Received results of broadcasted remote executions. Note that if task class has
     *      {@link GridComputeTaskNoResultCache} annotation, then this list will be empty.
     * @return Will always return {@code null}.
     */
    @Override public R reduce(List<GridComputeJobResult> results) {
        return null;
    }
}
