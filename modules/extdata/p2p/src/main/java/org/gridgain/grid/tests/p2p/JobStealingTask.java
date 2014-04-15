package org.gridgain.grid.tests.p2p;

import org.gridgain.grid.*;
import org.gridgain.grid.compute.*;
import org.gridgain.grid.logger.*;
import org.gridgain.grid.resources.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;

/**
 * Task for testing job stealing.
 */
public class JobStealingTask extends GridComputeTaskAdapter<Object, Map<UUID, Integer>> {
    /** Number of jobs to spawn from task. */
    private static final int N_JOBS = 4;

    /** Grid. */
    @GridInstanceResource
    private Grid grid;

    /** Logger. */
    @GridLoggerResource
    private GridLogger log;

    /** {@inheritDoc} */
    @SuppressWarnings("ForLoopReplaceableByForEach")
    @Override public Map<? extends GridComputeJob, GridNode> map(List<GridNode> subgrid,
        @Nullable Object arg) throws GridException {
        assert !subgrid.isEmpty();

        Map<GridComputeJobAdapter, GridNode> map = new HashMap<>(subgrid.size());

        // Put all jobs onto one node.
        for (int i = 0; i < N_JOBS; i++)
            map.put(new GridJobStealingJob(5000L), subgrid.get(0));

        return map;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("SuspiciousMethodCalls")
    @Override public Map<UUID, Integer> reduce(List<GridComputeJobResult> results) throws GridException {
        Map<UUID, Integer> ret = new HashMap<>(results.size());

        for (GridComputeJobResult res : results) {
            log.info("Job result: " + res.getData());

            UUID resUuid = (UUID)res.getData();

            ret.put(resUuid,
                ret.containsKey(resUuid) ? ret.get(resUuid) + 1 : 1);
        }

        return ret;
    }

    /**
     * Job stealing job.
     */
    private static final class GridJobStealingJob extends GridComputeJobAdapter {
        /** Injected grid. */
        @GridInstanceResource
        private Grid grid;

        /** Logger. */
        @GridLoggerResource
        private GridLogger log;

        /**
         * @param arg Job argument.
         */
        GridJobStealingJob(Long arg) {
            super(arg);
        }

        /** {@inheritDoc} */
        @Override public Serializable execute() throws GridException {
            log.info("Started job on node: " + grid.localNode().id());

            try {
                Long sleep = argument(0);

                assert sleep != null;

                Thread.sleep(sleep);
            }
            catch (InterruptedException e) {
                log.info("Job got interrupted on node: " + grid.localNode().id());

                throw new GridException("Job got interrupted.", e);
            }
            finally {
                log.info("Job finished on node: " + grid.localNode().id());
            }

            return grid.localNode().id();
        }
    }
}
