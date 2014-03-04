/* @scala.file.header */

/*
 * ___    _________________________ ________
 * __ |  / /____  _/__  ___/__  __ \___  __ \
 * __ | / /  __  /  _____ \ _  / / /__  /_/ /
 * __ |/ /  __/ /   ____/ / / /_/ / _  _, _/
 * _____/   /___/   /____/  \____/  /_/ |_|
 *
 */

package org.gridgain.visor.commands.ccompact

import org.gridgain.visor._
import VisorCacheCompactCommand._
import org.gridgain.grid._
import cache._
import GridCacheMode._
import org.gridgain.grid.{GridGain => G}
import org.gridgain.grid.spi.discovery.tcp.GridTcpDiscoverySpi
import org.gridgain.grid.spi.discovery.tcp.ipfinder.vm.GridTcpDiscoveryVmIpFinder
import collection.JavaConversions._
import org.jetbrains.annotations.Nullable

/**
 * @author @java.author
 * @version @java.version
 */
class VisorCacheCompactCommandSpec extends VisorRuntimeBaseSpec(2) {
    /** IP finder. */
    val ipFinder = new GridTcpDiscoveryVmIpFinder(true)

    /**
     * Creates grid configuration for provided grid host.
     *
     * @param name Grid name.
     * @return Grid configuration.
     */
    override def config(name: String): GridConfiguration = {
        val cfg = new GridConfiguration

        cfg.setGridName(name)
        cfg.setLocalHost("127.0.0.1")
        cfg.setCacheConfiguration(cacheConfig(null), cacheConfig("cache"))

        val discoSpi = new GridTcpDiscoverySpi()

        discoSpi.setIpFinder(ipFinder)

        cfg.setDiscoverySpi(discoSpi)

        cfg
    }

    /**
     * @param name Cache name.
     * @return Cache Configuration.
     */
    def cacheConfig(@Nullable name: String): GridCacheConfiguration = {
        val cfg = new GridCacheConfiguration

        cfg.setCacheMode(REPLICATED)
        cfg.setName(name)

        cfg
    }

    behavior of "An 'ccompact' visor command"

    it should "show correct result for default cache" in {
        GridGain.grid("node-1").cache[Int, Int](null).putAll(Map(1 -> 1, 2 -> 2, 3 -> 3))

        GridGain.grid("node-1").cache[Int, Int](null).clear(1)

        visor.open("-e -g=node-1", false)

        visor.ccompact()

        visor.close()
    }

    it should "show correct result for named cache" in {
        GridGain.grid("node-1").cache[Int, Int]("cache").putAll(Map(1 -> 1, 2 -> 2, 3 -> 3))

        GridGain.grid("node-1").cache[Int, Int]("cache").clear(1)

        visor.open("-e -g=node-1", false)

        visor.ccompact("cache")

        visor.close()
    }

    it should "show correct help" in {
        VisorCacheCompactCommand

        visor.help("ccompact")
    }

    it should "show empty projection error message" in {
        visor.open("-e -g=node-1", false)

        visor.ccompact("wrong")

        visor.close()
    }
}
