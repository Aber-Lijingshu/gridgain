/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.spi.indexing;

import org.gridgain.grid.logger.*;
import org.gridgain.grid.resources.*;
import org.gridgain.grid.spi.*;
import org.gridgain.grid.util.typedef.internal.*;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * No-op implementation of {@link GridIndexingSpi}, throws exception on query attempt.
 */
public class GridIndexingNoopSpi extends GridSpiAdapter implements GridIndexingSpi {
    /** */
    @GridLoggerResource
    private GridLogger log;

    /** {@inheritDoc} */
    @Override public <K, V> GridIndexingFieldsResult queryFields(@Nullable String spaceName, String qry,
        Collection<Object> params, GridIndexingQueryFilter<K, V>... filters) throws GridSpiException {
        throw spiException();
    }

    /** {@inheritDoc} */
    @Override public <K, V> GridSpiCloseableIterator<GridIndexingKeyValueRow<K, V>> query(@Nullable String spaceName,
        String qry, Collection<Object> params, GridIndexingTypeDescriptor type,
        GridIndexingQueryFilter<K, V>... filters) throws GridSpiException {
        throw spiException();
    }

    /** {@inheritDoc} */
    @Override public <K, V> GridSpiCloseableIterator<GridIndexingKeyValueRow<K, V>> queryText(@Nullable
        String spaceName, String qry, GridIndexingTypeDescriptor type, GridIndexingQueryFilter<K, V>... filters)
        throws GridSpiException {
        throw spiException();
    }

    /** {@inheritDoc} */
    @Override public long size(@Nullable String spaceName, GridIndexingTypeDescriptor desc) throws GridSpiException {
        throw spiException();
    }

    /** {@inheritDoc} */
    @Override public boolean registerType(@Nullable String spaceName, GridIndexingTypeDescriptor desc)
        throws GridSpiException {
        return false;
    }

    /** {@inheritDoc} */
    @Override public void unregisterType(@Nullable String spaceName, GridIndexingTypeDescriptor type)
        throws GridSpiException {
        // No-op.
    }

    /** {@inheritDoc} */
    @Override public <K, V> void store(@Nullable String spaceName, GridIndexingTypeDescriptor type,
        GridIndexingEntity<K> key, GridIndexingEntity<V> val, byte[] ver, long expirationTime) throws GridSpiException {
        // No-op.
    }

    /** {@inheritDoc} */
    @Override public <K> boolean remove(@Nullable String spaceName, GridIndexingEntity<K> key) throws GridSpiException {
        return false;
    }

    /** {@inheritDoc} */
    @Override public <K> void onSwap(@Nullable String spaceName, String swapSpaceName, K key) throws GridSpiException {
        // No-op.
    }

    /** {@inheritDoc} */
    @Override public <K, V> void onUnswap(@Nullable String spaceName, K key, V val, byte[] valBytes)
        throws GridSpiException {
        // No-op.
    }

    /** {@inheritDoc} */
    @Override public void registerMarshaller(GridIndexingMarshaller marshaller) {
        // No-op.
    }

    /** {@inheritDoc} */
    @Override public void registerSpace(String spaceName) throws GridSpiException {
        // No-op.
    }

    /** {@inheritDoc} */
    @Override public void rebuildIndexes(@Nullable String spaceName, GridIndexingTypeDescriptor type) {
        // No-op.
    }

    /** {@inheritDoc} */
    @Override public void spiStart(@Nullable String gridName) throws GridSpiException {
        U.warn(log, "GridGain uses no-op indexing SPI.");
    }

    /** {@inheritDoc} */
    @Override public void spiStop() throws GridSpiException {
        // No-op.
    }

    /**
     * @return No-op SPI usage exception.
     */
    private GridSpiException spiException() {
        // TODO 8242.
        return new GridSpiException("No-op SPI does not support queries, please use GridH2IndexingSpi.");
    }
}
