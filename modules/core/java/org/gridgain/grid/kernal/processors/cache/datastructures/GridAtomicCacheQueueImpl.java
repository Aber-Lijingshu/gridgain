/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.cache.datastructures;

import org.gridgain.grid.*;
import org.gridgain.grid.cache.*;
import org.gridgain.grid.cache.datastructures.*;
import org.gridgain.grid.kernal.processors.cache.*;
import org.gridgain.grid.util.typedef.internal.*;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * {@link GridCacheQueue} implementation using atomic cache.
 */
public class GridAtomicCacheQueueImpl<T> extends GridCacheQueueAdapter<T> {
    /** */
    private static final long RETRY_TIMEOUT = 3000;

    /**
     * @param queueName Queue name.
     * @param header Queue header.
     * @param cctx Cache context.
     * @throws GridException If failed.
     */
    public GridAtomicCacheQueueImpl(String queueName, GridCacheQueueHeader header, GridCacheContext<?, ?> cctx)
        throws GridException {
        super(queueName, header, cctx);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override public boolean offer(T item) throws GridRuntimeException {
        try {
            Long idx = transformHeader(new AddClosure(uuid, 1));

            if (idx == null)
                return false;

            checkRemoved(idx);

            int cnt = 0;

            GridCacheQueueItemKey key = itemKey(idx);

            while (true) {
                try {
                    boolean putx = cache.putx(key, item, null);

                    assert putx;

                    break;
                }
                catch (GridCachePartialUpdateException e) {
                    if (cnt++ == MAX_UPDATE_RETRIES)
                        throw e;
                    else
                        U.warn(log, "Failed to put queue item, will retry [err=" + e + ", idx=" + idx + ']');
                }
            }

            return true;
        }
        catch (GridException e) {
            throw new GridRuntimeException(e);
        }
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Nullable @Override public T poll() throws GridRuntimeException {
        try {
            Long idx = transformHeader(new PollClosure(uuid));

            if (idx == null)
                return null;

            checkRemoved(idx);

            GridCacheQueueItemKey key = itemKey(idx);

            int cnt = 0;

            long stop = 0;

            while (true) {
                try {
                    T data = (T)cache.remove(key, null);

                    if (data != null)
                        return data;

                    if (stop == 0)
                        stop = U.currentTimeMillis() + RETRY_TIMEOUT;

                    while (U.currentTimeMillis() < stop ) {
                        data = (T)cache.remove(key, null);

                        if (data != null)
                            return data;
                    }

                    break;
                }
                catch (GridCachePartialUpdateException e) {
                    if (cnt++ == MAX_UPDATE_RETRIES)
                        throw e;
                    else
                        U.warn(log, "Failed to remove queue item, will retry [err=" + e + ']');
                }
            }

            U.warn(log, "Failed to get item, will retry poll [queue=" + queueName + ", idx=" + idx + ']');

            return poll();
        }
        catch (GridException e) {
            throw new GridRuntimeException(e);
        }
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override public boolean addAll(Collection<? extends T> items) {
        A.notNull(items, "items");

        try {
            Long idx = transformHeader(new AddClosure(uuid, items.size()));

            if (idx == null)
                return false;

            checkRemoved(idx);

            Map<GridCacheQueueItemKey, T> putMap = new HashMap<>();

            for (T item : items) {
                putMap.put(itemKey(idx), item);

                idx++;
            }

            int cnt = 0;

            while (true) {
                try {
                    cache.putAll(putMap, null);

                    break;
                }
                catch (GridCachePartialUpdateException e) {
                    if (cnt++ == MAX_UPDATE_RETRIES)
                        throw e;
                    else
                        U.warn(log, "Failed to add items, will retry [err=" + e + ']');
                }
            }

            return true;
        }
        catch (GridException e) {
            throw new GridRuntimeException(e);
        }
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override protected void removeItem(long rmvIdx) throws GridException {
        Long idx = (Long)cache.transformCompute(queueKey, new RemoveClosure(uuid, rmvIdx));

        if (idx != null) {
            checkRemoved(idx);

            GridCacheQueueItemKey key = itemKey(idx);

            int cnt = 0;

            long stop = 0;

            while (true) {
                try {
                    if (cache.removex(key))
                        return;

                    if (stop == 0)
                        stop = U.currentTimeMillis() + RETRY_TIMEOUT;

                    while (U.currentTimeMillis() < stop ) {
                        if (cache.removex(key))
                            return;
                    }

                    break;
                }
                catch (GridCachePartialUpdateException e) {
                    if (cnt++ == MAX_UPDATE_RETRIES)
                        throw e;
                    else
                        U.warn(log, "Failed to add items, will retry [err=" + e + ']');
                }
            }

            U.warn(log, "Failed to remove item, [queue=" + queueName + ", idx=" + idx + ']');
        }
    }

    /**
     * @param c Transform closure to be applied for queue header.
     * @return Value computed by the transform closure.
     * @throws GridException If failed.
     */
    @SuppressWarnings("unchecked")
    @Nullable private Long transformHeader(GridCacheTransformComputeClosure<GridCacheQueueHeader, Long> c)
        throws GridException {
        int cnt = 0;

        while (true) {
            try {
                return (Long)cache.transformCompute(queueKey, c);
            }
            catch (GridCachePartialUpdateException e) {
                if (cnt++ == MAX_UPDATE_RETRIES)
                    throw e;
                else
                    U.warn(log, "Failed to update queue header, will retry [err=" + e + ']');
            }
        }
    }
}
