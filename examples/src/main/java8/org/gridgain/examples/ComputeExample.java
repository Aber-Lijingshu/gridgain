/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.examples;

import org.gridgain.grid.*;
import org.gridgain.grid.lang.*;
import org.gridgain.grid.lang.GridCallable;
import org.gridgain.grid.resources.*;

import java.util.*;

/**
 * Demonstrates broadcasting and unicasting computations within grid projection.
 * <p>
 * Remote nodes should always be started with special configuration file which
 * enables P2P class loading: {@code 'ggstart.{sh|bat} examples/config/example-compute.xml'}.
 */
public class ComputeExample {
    /**
     * Executes example.
     *
     * @param args Command line arguments, none required.
     * @throws GridException If example execution failed.
     */
    public static void main(String[] args) throws Exception {
        try (Grid grid = GridGain.start("examples/config/example-compute.xml")) {
            System.out.println();
            System.out.println(">>> Compute broadcast example started.");

            // Broadcast closure to all grid nodes.
            grid.compute().broadcast((GridRunnable)() -> System.out.println("Hello World")).get();


            // Unicast closure to some grid node picked by load balancer.
            grid.compute().run(((GridRunnable))() -> System.out.println("Hello World")).get();

            // Unicast closure to some grid node picked by load balancer and return result.
            int length = grid.compute().run(((GridCallable<Integer>))"Hello World"::length).get();

            System.out.println();
            System.out.println(">>> Check all nodes for hello message output.");
       }
    }
}
