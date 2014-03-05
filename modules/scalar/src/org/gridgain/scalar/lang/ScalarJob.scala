/* @scala.file.header */

/*
 * ________               ______                    ______   _______
 * __  ___/_____________ ____  /______ _________    __/__ \  __  __ \
 * _____ \ _  ___/_  __ `/__  / _  __ `/__  ___/    ____/ /  _  / / /
 * ____/ / / /__  / /_/ / _  /  / /_/ / _  /        _  __/___/ /_/ /
 * /____/  \___/  \__,_/  /_/   \__,_/  /_/         /____/_(_)____/
 *
 */

package org.gridgain.scalar.lang

import org.gridgain.grid.util.{GridUtils => U}
import org.gridgain.grid.compute.GridComputeJobAdapter

/**
 * Peer deploy aware adapter for Java's `GridComputeJob`.
 */
class ScalarJob(private val inner: () => Any) extends GridComputeJobAdapter {
    assert(inner != null)

    setPeerDeployAware(U.peerDeployAware(inner))

    /**
     * Delegates to passed in function.
     */
    def execute = inner().asInstanceOf[AnyRef]
}
