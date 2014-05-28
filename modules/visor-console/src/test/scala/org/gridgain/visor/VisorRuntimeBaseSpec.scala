/* @scala.file.header */

/*
 * ___    _________________________ ________
 * __ |  / /____  _/__  ___/__  __ \___  __ \
 * __ | / /  __  /  _____ \ _  / / /__  /_/ /
 * __ |/ /  __/ /   ____/ / / /_/ / _  _, _/
 * _____/   /___/   /____/  \____/  /_/ |_|
 *
 */
package org.gridgain.visor

import org.gridgain.grid.{GridGain => G, _}

import org.scalatest._

/**
 * Base abstract class for unit tests requiring Visor runtime.
 */
abstract class VisorRuntimeBaseSpec(private[this] val num: Int) extends FlatSpec with Matchers
    with BeforeAndAfterAll with BeforeAndAfterEach {
    assert(num >= 1)

    /**
     * Gets grid configuration.
     *
     * @param name Grid name.
     * @return Grid configuration.
     */
    protected def config(name: String): GridConfiguration = {
        val cfg = new GridConfiguration

        cfg.setGridName(name)

        cfg
    }

    protected def openVisor() {
        visor.open(config("visor-demo-node"), "n/a")
    }

    protected def closeVisorQuiet() {
        if (visor.isConnected)
            visor.close()
    }

    /**
     * Runs before all tests.
     */
    final override def beforeAll() {
        (1 to num).foreach((n: Int) => G.start(config("node-" + n)))
    }

    /**
     * Runs after all tests.
     */
    override def afterAll() {
        (1 to num).foreach((n: Int) => G.stop("node-" + n, false))
    }

    override protected def beforeEach() {
        openVisor()
    }

    override protected def afterEach() {
        closeVisorQuiet()
    }
}
