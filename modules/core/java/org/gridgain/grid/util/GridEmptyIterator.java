/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.util;

import org.gridgain.grid.util.typedef.internal.*;
import org.gridgain.grid.util.lang.*;

import java.util.*;

/**
 * Empty iterator.
 *
 * @author @java.author
 * @version @java.version
 */
public class GridEmptyIterator<T> extends GridIteratorAdapter<T> {
    /** {@inheritDoc} */
    @Override public boolean hasNextX() {
        return false;
    }

    /** {@inheritDoc} */
    @Override public T nextX() {
        throw new NoSuchElementException("Iterator is empty.");
    }

    /** {@inheritDoc} */
    @Override public void removeX() {
        throw new NoSuchElementException("Iterator is empty.");
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(GridEmptyIterator.class, this);
    }
}
