/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.tests.p2p;

import org.gridgain.grid.lang.*;
import org.gridgain.grid.resources.*;

import java.util.*;

/**
 * Closure that returns cache query reducer, which uses
 * {@link GridP2PAwareTestUserResource}.
 */
public class GridExternalCacheQueryReducerClosure implements GridReducer<Map.Entry<Integer, Integer>, Integer> {
    /** */
    @GridUserResource
    private transient GridP2PAwareTestUserResource rsrc;

    /** {@inheritDoc} */
    @Override public boolean collect(Map.Entry<Integer, Integer> e) {
        return true;
    }

    /** {@inheritDoc} */
    @Override public Integer reduce() {
        return 0;
    }
}
