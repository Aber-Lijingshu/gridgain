/* @scala.file.header */

/*
 * ___    _________________________ ________
 * __ |  / /____  _/__  ___/__  __ \___  __ \
 * __ | / /  __  /  _____ \ _  / / /__  /_/ /
 * __ |/ /  __/ /   ____/ / / /_/ / _  _, _/
 * _____/   /___/   /____/  \____/  /_/ |_|
 *
 */

package org.gridgain.visor.commands.deploy

import org.scalatest._
import matchers._
import org.gridgain.visor._
import VisorDeployCommand._

/**
 * Unit test for 'deploy' command.
 *
 * @author @java.author
 * @version @java.version
 */
class VisorDeployCommandSpec extends FlatSpec with ShouldMatchers {
    behavior of "A 'deploy' visor command"

    it should "copy folder" in {
        visor.deploy("-h=uname:passwd@localhost -s=/home/uname/test -d=dir")
    }
}
