/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.examples.compute;

import org.gridgain.grid.*;
import org.gridgain.grid.compute.*;
import org.gridgain.grid.lang.*;

import java.util.*;

/**
 * Demonstrates a simple use of {@link org.gridgain.grid.lang.GridRunnable}.
 * <p>
 * Remote nodes should always be started with special configuration file which
 * enables P2P class loading: {@code 'ggstart.{sh|bat} examples/config/example-compute.xml'}.
 * <p>
 * Alternatively you can run {@link ComputeNodeStartup} in another JVM which will start GridGain node
 * with {@code examples/config/example-compute.xml} configuration.
 *
 * @author @java.author
 * @version @java.version
 */
public class ComputeRunnableExample {
    /**
     * Executes example.
     *
     * @param args Command line arguments, none required.
     * @throws GridException If example execution failed.
     */
    public static void main(String[] args) throws GridException {
        try (Grid g = GridGain.start("examples/config/example-compute.xml")) {
            System.out.println();
            System.out.println("Compute runnable example started.");

            Collection<GridFuture> futs = new ArrayList<>();

            // Iterate through all words in the sentence and create callable jobs.
            for (final String word : "Print words using runnable".split(" ")) {
                // Execute runnable on some node.
                futs.add(g.compute().run(new GridRunnable() {
                    @Override public void run() {
                        System.out.println();
                        System.out.println(">>> Printing '" + word + "' on this node from grid job.");
                    }
                }));
            }

            // Wait for all futures to complete.
            for (GridFuture<?> f : futs)
                f.get();

            System.out.println();
            System.out.println(">>> Finished printing words using runnable execution.");
            System.out.println(">>> Check all nodes for output (this node is also part of the grid).");
        }
    }
}
