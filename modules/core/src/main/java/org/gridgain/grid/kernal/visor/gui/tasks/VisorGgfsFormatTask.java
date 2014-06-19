/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.visor.gui.tasks;

import org.gridgain.grid.*;
import org.gridgain.grid.kernal.processors.task.*;
import org.gridgain.grid.kernal.visor.cmd.*;
import org.gridgain.grid.util.typedef.internal.*;

/**
 * Format GGFS instance.
 */
@GridInternal
public class VisorGgfsFormatTask extends VisorOneNodeTask<String, Void> {
    /** {@inheritDoc} */
    @Override protected VisorGgfsFormatJob job(String arg) {
        return new VisorGgfsFormatJob(arg);
    }

    /**
     * Job that format GGFS.
     */
    private static class VisorGgfsFormatJob extends VisorJob<String, Void> {
        /** */
        private static final long serialVersionUID = 0L;

        /**
         * @param arg GGFS name to format.
         */
        private VisorGgfsFormatJob(String arg) {
            super(arg);
        }

        /** {@inheritDoc} */
        @Override protected Void run(String ggfsName) throws GridException {
            try {
                g.ggfs(ggfsName).format().get();
            }
            catch (IllegalArgumentException iae) {
                throw new GridException("Failed to format GGFS: " + ggfsName, iae);
            }

            return null;
        }

        /** {@inheritDoc} */
        @Override public String toString() {
            return S.toString(VisorGgfsFormatJob.class, this);
        }
    }
}
