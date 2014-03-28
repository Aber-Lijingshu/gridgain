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
import org.jetbrains.annotations.*;

import java.util.*;

import static org.gridgain.grid.cache.GridCacheTxConcurrency.*;
import static org.gridgain.grid.cache.GridCacheTxIsolation.*;

/**
 * {@link GridCacheQueue} implementation using transactional cache.
 */
public class GridTransactionalCacheQueueImpl<T> extends GridCacheQueueAdapter<T> {
    /**
     * @param queueName Queue name.
     * @param uuid Queue UUID.
     * @param cap Capacity.
     * @param collocated Collocation flag.
     * @param cctx Cache context.
     */
    public GridTransactionalCacheQueueImpl(String queueName, GridUuid uuid, int cap, boolean collocated,
        GridCacheContext<?, ?> cctx) {
        super(queueName, uuid, cap, collocated, cctx);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override public boolean offer(T item) throws GridRuntimeException {
        try {
            try (GridCacheTx tx = cache.txStart(PESSIMISTIC, REPEATABLE_READ)) {
                Long idx = (Long)cache.transformCompute(queueKey, new AddClosure(uuid, 1));

                if (idx == null)
                    return false;

                checkRemoved(idx);

                boolean putx = cache.putx(new ItemKey(uuid, idx, collocated()), item, null);

                assert putx;

                tx.commit();

                return true;
            }
        }
        catch (GridException e) {
            throw new GridRuntimeException(e);
        }
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override public boolean addAll(Collection<? extends T> items) {
        try {
            try (GridCacheTx tx = cache.txStart(PESSIMISTIC, REPEATABLE_READ)) {
                Long idx = (Long)cache.transformCompute(queueKey, new AddClosure(uuid, items.size()));

                if (idx == null)
                    return false;

                checkRemoved(idx);

                Map<ItemKey, T> putMap = new HashMap<>();

                for (T item : items) {
                    putMap.put(new ItemKey(uuid, idx, collocated()), item);

                    idx += 1;
                }

                cache.putAll(putMap, null);

                tx.commit();

                return true;
            }
        }
        catch (GridException e) {
            throw new GridRuntimeException(e);
        }
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Nullable @Override public T poll() throws GridRuntimeException {
        try {
            T e = null;

            try (GridCacheTx tx = cache.txStart(PESSIMISTIC, REPEATABLE_READ)) {
                Long idx = (Long)cache.transformCompute(queueKey, new PollClosure(uuid));

                if (idx != null) {
                    checkRemoved(idx);

                    e = (T)cache.remove(new ItemKey(uuid, idx, collocated()), null);

                    assert e != null;
                }

                tx.commit();
            }

            return e;
        }
        catch (GridException e) {
            throw new GridRuntimeException(e);
        }
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override protected void removeItem(long rmvIdx) throws GridException {
        try {
            try (GridCacheTx tx = cache.txStart(PESSIMISTIC, REPEATABLE_READ)) {
                Long idx = (Long)cache.transformCompute(queueKey, new RemoveClosure(uuid, rmvIdx));

                if (idx != null) {
                    boolean removex = cache.removex(new ItemKey(uuid, idx, collocated()));

                    assert removex;
                }

                tx.commit();
            }
        }
        catch (GridException e) {
            throw new GridRuntimeException(e);
        }
    }
}
