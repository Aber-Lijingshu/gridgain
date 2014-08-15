/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.managers.security;

import org.gridgain.grid.*;
import org.gridgain.grid.kernal.*;
import org.gridgain.grid.security.*;
import org.gridgain.grid.util.typedef.internal.*;

import java.io.*;
import java.util.*;

/**
 * Implementation of grid security interface.
 */
public class GridSecurityImpl implements GridSecurity, Externalizable {
    /** */
    private static final long serialVersionUID = 0L;

    /** */
    private static final ThreadLocal<GridKernalContext> stash = new ThreadLocal<>();

    /** Security manager. */
    private GridSecurityManager secMgr;

    /** Context. */
    private GridKernalContext ctx;

    /**
     * Required by {@link Externalizable}.
     */
    public GridSecurityImpl() {
        // No-op.
    }

    /**
     * @param ctx Context.
     */
    public GridSecurityImpl(GridKernalContext ctx) {
        this.secMgr = ctx.security();
        this.ctx = ctx;
    }

    /** {@inheritDoc} */
    @Override public Collection<GridSecuritySubject> authenticatedSubjects() throws GridException {
        return secMgr.authenticatedSubjects();
    }

    /** {@inheritDoc} */
    @Override public GridSecuritySubject authenticatedSubject(UUID subjId) throws GridException {
        return secMgr.authenticatedSubject(subjId);
    }

    /** {@inheritDoc} */
    @Override public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(ctx);
    }

    /** {@inheritDoc} */
    @Override public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        stash.set((GridKernalContext)in.readObject());
    }

    /**
     * Reconstructs object on demarshalling.
     *
     * @return Reconstructed object.
     * @throws ObjectStreamException Thrown in case of demarshalling error.
     */
    private Object readResolve() throws ObjectStreamException {
        try {
            GridKernalContext ctx = stash.get();

            return ctx.grid().security();
        }
        catch (Exception e) {
            throw U.withCause(new InvalidObjectException(e.getMessage()), e);
        }
        finally {
            stash.remove();
        }
    }
}
