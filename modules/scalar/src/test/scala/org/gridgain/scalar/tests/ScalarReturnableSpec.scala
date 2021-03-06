/* 
 Copyright (C) GridGain Systems. All Rights Reserved.
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

/*
 * ________               ______                    ______   _______
 * __  ___/_____________ ____  /______ _________    __/__ \  __  __ \
 * _____ \ _  ___/_  __ `/__  / _  __ `/__  ___/    ____/ /  _  / / /
 * ____/ / / /__  / /_/ / _  /  / /_/ / _  /        _  __/___/ /_/ /
 * /____/  \___/  \__,_/  /_/   \__,_/  /_/         /____/_(_)____/
 *
 */

package org.gridgain.scalar.tests

import org.scalatest.matchers._
import org.gridgain.scalar._
import scalar._
import org.scalatest._
import junit.JUnitRunner
import scala.util.control.Breaks._
import org.junit.runner.RunWith

/**
 *
 */
@RunWith(classOf[JUnitRunner])
class ScalarReturnableSpec extends FlatSpec with ShouldMatchers {
    "Scalar '^^'" should "work" in {
        var i = 0

        breakable {
            while (true) {
                if (i == 0)
                    println("Only once!") ^^

                i += 1
            }
        }

        assert(i == 0)
    }

    "Scalar '^^'" should "also work" in {
        test()
    }

    // Ignore exception below.
    def test() = breakable {
        while (true) {
            println("Only once!") ^^
        }
    }
}
