/* @scala.file.header */

/*
 * ________               ______                    ______   _______
 * __  ___/_____________ ____  /______ _________    __/__ \  __  __ \
 * _____ \ _  ___/_  __ `/__  / _  __ `/__  ___/    ____/ /  _  / / /
 * ____/ / / /__  / /_/ / _  /  / /_/ / _  /        _  __/___/ /_/ /
 * /____/  \___/  \__,_/  /_/   \__,_/  /_/         /____/_(_)____/
 *
 */

package org.gridgain.scalar.testsuites

import org.scalatest._
import org.gridgain.scalar.tests._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 *
 */
@RunWith(classOf[JUnitRunner])
class ScalarSelfTestSuite extends Suites(
    new ScalarAffinityRoutingSpec,
    new ScalarCacheQueriesSpec,
    new ScalarCacheSpec,
    new ScalarConversionsSpec,
    new ScalarProjectionSpec,
    new ScalarReturnableSpec,
    new ScalarSpec
) {
}