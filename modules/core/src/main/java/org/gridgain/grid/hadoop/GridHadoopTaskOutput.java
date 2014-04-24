/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.hadoop;

import org.gridgain.grid.*;

/**
 * Task output.
 */
public interface GridHadoopTaskOutput extends AutoCloseable {
    /**
     * @param key Key.
     * @param val Value.
     */
    public void write(Object key, Object val) throws GridException;

    /**
     * @return Finish future.
     */
    public GridFuture<?> finish();
}
