/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.compute.gridify;

import org.gridgain.grid.compute.*;

import java.util.*;

/**
 * Convenience adapter for tasks that work with {@link Gridify} annotation
 * for grid-enabling methods. It enhances the regular {@link GridComputeTaskAdapter}
 * by enforcing the argument type of {@link GridifyArgument}. All tasks
 * that work with {@link Gridify} annotation receive an argument of this type.
 * <p>
 * Please refer to {@link GridComputeTaskAdapter} documentation for more information
 * on additional functionality this adapter provides.
 * @param <R> Return value of the task (see {@link GridComputeTask#reduce(List)} method).
 */
public abstract class GridifyTaskAdapter<R> extends GridComputeTaskAdapter<GridifyArgument, R> {
    // No-op.
}
