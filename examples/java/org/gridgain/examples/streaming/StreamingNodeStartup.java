/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.examples.streaming;

import org.gridgain.grid.*;
import org.gridgain.grid.product.*;

import static org.gridgain.grid.product.GridProductEdition.*;

/**
 * Starts up node with example streamer configuration.
 *
 * @author @java.author
 * @version @java.version
 */
@GridOnlyAvailableIn(STREAMING)
public class StreamingNodeStartup {
    /**
     * Start up an empty node with specified cache configuration.
     *
     * @param args Command line arguments, none required.
     * @throws GridException If example execution failed.
     */
    public static void main(String[] args) throws GridException {
        GridGain.start("examples/config/example-streamer.xml");
    }
}
