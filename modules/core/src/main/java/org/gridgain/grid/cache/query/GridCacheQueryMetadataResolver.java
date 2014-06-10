// @java.file.header

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.cache.query;

/**
 * TODO: Add class description.
 *
 * @author @java.author
 * @version @java.version
 */
public interface GridCacheQueryMetadataResolver {
    public String resolveTypeName(Object key, Object val);

    public Class<?> resolveFieldType(String field, Object key, Object val);

    public Object valueOf(String field, Object key, Object val);
}
