/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.spi.checkpoint.sharedfs;

import org.gridgain.testframework.junits.spi.*;
import java.util.*;

/**
 * Grid shared file system checkpoint SPI config self test.
 */
@GridSpiTest(spi = GridSharedFsCheckpointSpi.class, group = "Checkpoint SPI")
public class GridSharedFsCheckpointSpiConfigSelfTest extends GridSpiAbstractConfigTest<GridSharedFsCheckpointSpi> {
    /**
     * @throws Exception If failed.
     */
    public void testNegativeConfig() throws Exception {
        checkNegativeSpiProperty(new GridSharedFsCheckpointSpi(), "directoryPaths", null);
        checkNegativeSpiProperty(new GridSharedFsCheckpointSpi(), "directoryPaths", new LinkedList<String>());
    }
}
