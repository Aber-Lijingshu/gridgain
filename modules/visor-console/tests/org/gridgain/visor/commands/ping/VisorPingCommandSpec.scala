/* @scala.file.header */

/*
 * ___    _________________________ ________
 * __ |  / /____  _/__  ___/__  __ \___  __ \
 * __ | / /  __  /  _____ \ _  / / /__  /_/ /
 * __ |/ /  __/ /   ____/ / / /_/ / _  _, _/
 * _____/   /___/   /____/  \____/  /_/ |_|
 *
 */

package org.gridgain.visor.commands.ping

import org.gridgain.visor._
import VisorPingCommand._

/**
 * Unit test for 'ping' command.
 */
class VisorPingCommandSpec extends VisorRuntimeBaseSpec(2) {
    behavior of "A 'ping' visor command"

    it should "properly execute" in {
        visor open("-d", false)
        visor ping()
        visor close()
    }

    it should "print error message when not connected" in {
        visor ping()
    }
}
