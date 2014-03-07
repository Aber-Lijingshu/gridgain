/* @scala.file.header */

/*
 * ___    _________________________ ________
 * __ |  / /____  _/__  ___/__  __ \___  __ \
 * __ | / /  __  /  _____ \ _  / / /__  /_/ /
 * __ |/ /  __/ /   ____/ / / /_/ / _  _, _/
 * _____/   /___/   /____/  \____/  /_/ |_|
 *
 */

package org.gridgain.visor.commands.config

import org.gridgain.visor._
import VisorConfigurationCommand._
import org.gridgain.grid._
import org.gridgain.grid.events.GridEventType
import GridEventType._
import org.gridgain.grid.events.GridEventType

/**
 * Unit test for 'config' command.
 */
class VisorConfigurationCommandSpec extends VisorRuntimeBaseSpec(1) {
    /**
     * Creates grid configuration for provided grid host.
     *
     * @param name Grid name.
     * @return Grid configuration.
     */
    override def config(name: String): GridConfiguration = {
        val cfg = new GridConfiguration

        cfg.setGridName(name)
        cfg.setLifeCycleEmailNotification(false)
        cfg.setIncludeEventTypes(EVTS_ALL: _*)

        cfg
    }

    behavior of "A 'config' visor command"

    it should "print configuration for first node" in {
        visor.open("-d", false)

        visor.config("-id8=@n0")

        visor.close()
    }
}
