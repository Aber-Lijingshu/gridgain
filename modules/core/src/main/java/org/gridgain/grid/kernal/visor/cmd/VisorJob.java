/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.visor.cmd;

import org.gridgain.grid.*;
import org.gridgain.grid.compute.*;
import org.gridgain.grid.kernal.*;
import org.gridgain.grid.logger.*;
import org.gridgain.grid.resources.*;
import org.jetbrains.annotations.*;

/**
 * Basde class for Visor jobs.
 */
public abstract class VisorJob<A, R> extends GridComputeJobAdapter {
    /**
     * Create job with specified argument.
     *
     * @param arg Job argument.
     */
    protected VisorJob(A arg) {
        super(arg);
    }

    @GridInstanceResource
    protected GridEx g;

    /** */
    @GridLoggerResource
    protected GridLogger log;


    @Nullable @Override public Object execute() throws GridException {
        A arg = argument(0);

        return run(arg);
    }

    /**
     * Execution logic of concrete task.
     *
     * @return Result.
     */
    protected abstract R run(A arg) throws GridException;
}
