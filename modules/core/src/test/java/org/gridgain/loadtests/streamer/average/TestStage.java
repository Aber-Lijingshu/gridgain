/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.loadtests.streamer.average;

import org.gridgain.grid.*;
import org.gridgain.grid.streamer.*;
import org.gridgain.grid.util.typedef.*;

import java.util.*;
import java.util.concurrent.*;

/**
 * Stage for average benchmark.
 */
class TestStage implements GridStreamerStage<Integer> {
    /** {@inheritDoc} */
    @Override public String name() {
        return "stage";
    }

    /** {@inheritDoc} */
    @Override public Map<String, Collection<?>> run(GridStreamerContext ctx, Collection<Integer> evts)
        throws GridException {
        ConcurrentMap<String, TestAverage> loc = ctx.localSpace();

        TestAverage avg = loc.get("avg");

        if (avg == null)
            avg = F.addIfAbsent(loc, "avg", new TestAverage());

        for (Integer e : evts)
            avg.increment(e, 1);

        GridStreamerWindow<Integer> win = ctx.window();

        win.enqueueAll(evts);

        while (true) {
            Integer e = win.pollEvicted();

            if (e == null)
                break;

            // Subtract evicted events from running total.
            avg.increment(-e, -1);
        }

        return null;
    }
}
