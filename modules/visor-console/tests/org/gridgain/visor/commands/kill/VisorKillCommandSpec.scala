/* @scala.file.header */

/*
 * ___    _________________________ ________
 * __ |  / /____  _/__  ___/__  __ \___  __ \
 * __ | / /  __  /  _____ \ _  / / /__  /_/ /
 * __ |/ /  __/ /   ____/ / / /_/ / _  _, _/
 * _____/   /___/   /____/  \____/  /_/ |_|
 *
 */

package org.gridgain.visor.commands.kill

import org.scalatest._
import matchers._
import org.gridgain.visor._
import VisorKillCommand._

/**
 * Unit test for 'kill' command.
 *
 * @author @java.author
 * @version @java.version
 */
class VisorKillCommandSpec extends FlatSpec with ShouldMatchers {
    behavior of "A 'kill' visor command"

    it should "print error message with null argument" in {
        visor open("-d", false)
        visor kill(null)
        visor close()
    }

    it should "print error message if both kill and restart specified" in {
        visor open("-d", false)
        visor kill("-k -r")
        visor close()
    }

    it should "print error message if not connected" in {
        visor kill("-k")
    }

    it should "restart node" in {
        visor open("-d", false)
        visor kill("-r -id8=@n1")
        visor close()
    }

    it should "print error message" in {
        visor open("-d", false)
        visor kill("-r -id=xxx")
        visor close()
    }
}
