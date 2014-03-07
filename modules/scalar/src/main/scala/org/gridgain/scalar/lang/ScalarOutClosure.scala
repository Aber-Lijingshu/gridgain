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

import org.gridgain.grid.lang.GridOutClosure
import java.util.concurrent.Callable

/**
 * Peer deploy aware adapter for Java's `GridOutClosure`.
 */
class ScalarOutClosure[R](private val f: () => R) extends GridOutClosure[R] with Callable[R] {
    assert(f != null)

    /**
     * Delegates to passed in function.
     */
    def apply: R = {
        f()
    }

    /**
     * Delegates to passed in function.
     */
    def call: R = {
        f()
    }
}
