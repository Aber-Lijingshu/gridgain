/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.examples.datagrid;

import org.gridgain.grid.*;
import org.gridgain.grid.cache.*;
import org.gridgain.grid.cache.query.*;
import org.gridgain.grid.lang.*;

import java.util.*;

/**
 * This examples demonstrates continuous query API.
 * <p>
 * Remote nodes should always be started with special configuration file which
 * enables P2P class loading: {@code 'ggstart.{sh|bat} examples/config/example-cache.xml'}.
 * <p>
 * Alternatively you can run {@link org.gridgain.examples.datagrid.CacheNodeStartup} in another JVM which will
 * start GridGain node with {@code examples/config/example-cache.xml} configuration.
 */
public class CacheContinuousQueryExample {
    /** Cache name. */
    private static final String CACHE_NAME = "partitioned";

    /**
     * Executes example.
     *
     * @param args Command line arguments, none required.
     * @throws GridException If example execution failed.
     */
    public static void main(String[] args) throws GridException, InterruptedException {
        try (Grid g = GridGain.start("examples/config/example-cache.xml")) {
            System.out.println();
            System.out.println(">>> Cache continuous query example started.");

            GridCache<Integer, String> cache = g.cache(CACHE_NAME);

            int keyCnt = 20;

            for (int i = 0; i < keyCnt; i++)
                cache.putx(i, Integer.toString(i));

            // Create new continuous query.
            try (GridCacheContinuousQuery<Integer, String> qry = cache.queries().createContinuousQuery()) {
                // Callback that is called locally when update notifications are received.
                qry.callback(new GridBiPredicate<UUID, Collection<Map.Entry<Integer, String>>>() {
                    @Override public boolean apply(UUID nodeId, Collection<Map.Entry<Integer, String>> entries) {
                        for (Map.Entry<Integer, String> e : entries)
                            System.out.println("Queried entry [key=" + e.getKey() + ", val=" + e.getValue() + ']');

                        return true; // Return true to continue listening.
                    }
                });

                // This filter will be evaluated remotely on all nodes
                // Entry that pass this filter will be sent to the caller.
                qry.filter(new GridBiPredicate<Integer, String>() {
                    @Override public boolean apply(Integer key, String val) {
                        return key > 15;
                    }
                });

                // Execute query.
                qry.execute();

                // Add a few more keys and watch more query notifications.
                for (int i = keyCnt; i < keyCnt + 5; i++)
                    cache.putx(i, Integer.toString(i));

                // Wait for a while while callback is notified about remaining puts.
                Thread.sleep(2000);
            }
        }
    }
}
