/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.streamer;

import org.gridgain.grid.*;
import org.gridgain.grid.streamer.*;

import java.util.*;

/**
 * Test stage.
 */
class GridTestStage implements GridStreamerStage<Object> {
    /** Stage name. */
    private String name;

    /** Stage closure. */
    private SC stageClos;

    /**
     * @param name Stage name.
     * @param stageClos Stage closure to execute.
     */
    GridTestStage(String name, SC stageClos) {
        this.name = name;
        this.stageClos = stageClos;
    }

    /** {@inheritDoc} */
    @Override public String name() {
        return name;
    }

    /** {@inheritDoc} */
    @Override public Map<String, Collection<?>> run(GridStreamerContext ctx, Collection<Object> evts)
        throws GridException {
        return stageClos.apply(name(), ctx, evts);
    }
}
