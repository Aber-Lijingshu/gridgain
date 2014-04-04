/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.client;

import org.gridgain.grid.compute.*;
import org.gridgain.grid.*;

import java.util.*;

import static org.gridgain.grid.compute.GridComputeJobResultPolicy.*;

/**
 * Test task summarizes length of all strings in the arguments list.
 * <p>
 * The argument of the task is a collection of objects to calculate string length sum of.
 */
public class GridClientTcpTask extends GridComputeTaskSplitAdapter<List<Object>, Integer> {
    /** {@inheritDoc} */
    @Override protected Collection<? extends GridComputeJob> split(int gridSize, List<Object> list)
        throws GridException {
        Collection<GridComputeJobAdapter> jobs = new ArrayList<>();

        if (list != null)
            for (final Object val : list)
                jobs.add(new GridComputeJobAdapter() {
                    @Override public Object execute() {
                        try {
                            Thread.sleep(5);
                        }
                        catch (InterruptedException ignored) {
                            Thread.currentThread().interrupt();
                        }

                        return val == null ? 0 : val.toString().length();
                    }
                });

        return jobs;
    }

    /** {@inheritDoc} */
    @Override public Integer reduce(List<GridComputeJobResult> results) throws GridException {
        int sum = 0;

        for (GridComputeJobResult res : results)
            sum += res.<Integer>getData();

        return sum;
    }

    /** {@inheritDoc} */
    @Override public GridComputeJobResultPolicy result(GridComputeJobResult res, List<GridComputeJobResult> rcvd) throws GridException {
        if (res.getException() != null)
            return FAILOVER;

        return WAIT;
    }
}
