/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.client;

import org.gridgain.grid.*;
import org.gridgain.grid.portables.*;

import java.util.*;

/**
 * Task where argument and result are {@link GridClientTestPortable}.
 */
public class GridClientPortableArgumentTask extends GridTaskSingleJobSplitAdapter {
    /** {@inheritDoc} */
    @Override protected Object executeJob(int gridSize, Object arg) throws GridException {
        Collection args = (Collection)arg;

        Iterator<Object> it = args.iterator();

        assert args.size() == 2 : args.size();

        boolean expPortable = (Boolean)it.next();

        GridClientTestPortable p;

        if (expPortable) {
            GridPortableObject<GridClientTestPortable> obj =
                (GridPortableObject<GridClientTestPortable>)it.next();

            p = obj.deserialize();
        }
        else
            p = (GridClientTestPortable)it.next();

        assert p != null;

        return new GridClientTestPortable(p.i + 1, true);
    }
}
