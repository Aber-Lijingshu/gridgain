/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.examples.misc.deployment.gar;

import org.gridgain.grid.*;
import org.gridgain.grid.marshaller.optimized.*;
import org.gridgain.grid.spi.deployment.uri.*;
import org.gridgain.grid.spi.discovery.tcp.*;
import org.gridgain.grid.spi.discovery.tcp.ipfinder.vm.*;

import java.util.*;

/**
 * Starts up an empty node with deployment-enabled configuration.
 * <p>
 * The difference is that running this class from IDE adds all example classes to classpath
 * but running from command line doesn't.
 */
public class GarDeploymentNodeStartup {
    /**
     * Start up an empty node with specified configuration.
     *
     * @param args Command line arguments, none required.
     * @throws org.gridgain.grid.GridException If example execution failed.
     */
    public static void main(String[] args) throws GridException {
        GridGain.start(configuration());
    }

    /**
     * Create Grid configuration with configured checkpoints.
     *
     * @return Grid configuration.
     * @throws GridException If configuration creation failed.
     */
    public static GridConfiguration configuration() throws GridException {
        GridConfiguration cfg = new GridConfiguration();

        cfg.setLocalHost("127.0.0.1");
        cfg.setPeerClassLoadingEnabled(true);

        GridOptimizedMarshaller marsh = new GridOptimizedMarshaller();

        marsh.setRequireSerializable(false);

        cfg.setMarshaller(marsh);

        cfg.setDeploymentSpi(new GridUriDeploymentSpi());

        GridTcpDiscoverySpi discoSpi = new GridTcpDiscoverySpi();

        GridTcpDiscoveryVmIpFinder ipFinder = new GridTcpDiscoveryVmIpFinder();

        ipFinder.setAddresses(Collections.singletonList("127.0.0.1:47500..47509"));

        discoSpi.setIpFinder(ipFinder);

        cfg.setDiscoverySpi(discoSpi);

        return cfg;
    }
}
