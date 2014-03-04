/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.managers.eventstorage;

import org.gridgain.grid.events.*;
import org.gridgain.grid.lang.*;

import java.io.*;
import java.util.*;

/**
 * Listener for asynchronous local node grid events. You can subscribe for local node grid
 * event notifications via {@link GridEventStorageManager#addLocalEventListener(GridLocalEventListener, int...)}.
 * <p>
 * Use {@link GridEventStorageManager#addLocalEventListener(GridPredicate, int...)} to register
 * this listener with grid.
 *
 * @author @java.author
 * @version @java.version
 * @see GridEvents#localListen(GridPredicate, int...)
 */
public interface GridLocalEventListener extends EventListener {
    /**
     * Local event callback.
     *
     * @param evt local grid event.
     */
    public void onEvent(GridEvent evt);
}
