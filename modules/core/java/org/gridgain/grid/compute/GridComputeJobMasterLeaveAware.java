package org.gridgain.grid.compute;

import org.gridgain.grid.*;

/**
 * Annotation for handling master node leave during job execution.
 * <p>
 * If {@link GridComputeJob} concrete class implements this interface then in case when master node leaves topology
 * during job execution the callback method {@link #onMasterNodeLeft(GridComputeTaskSession)} will be executed.
 * <p>
 * Implementing this interface gives you ability to preserve job execution result or its intermediate state
 * which could be reused later. E.g. you can save job execution result to the database or as a checkpoint
 * and reuse it when failed task is being executed again thus avoiding job execution from scratch.
 */
public interface GridComputeJobMasterLeaveAware {
    /**
     * A method which is executed in case master node has left topology during job execution.
     *
     * @param ses Task session, can be used for checkpoint saving.
     * @throws GridException In case of any exception.
     */
    public void onMasterNodeLeft(GridComputeTaskSession ses) throws GridException;
}
