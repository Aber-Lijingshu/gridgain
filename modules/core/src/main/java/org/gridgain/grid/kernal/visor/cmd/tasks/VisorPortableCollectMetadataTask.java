/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.visor.cmd.tasks;

import org.gridgain.grid.*;
import org.gridgain.grid.kernal.processors.task.*;
import org.gridgain.grid.kernal.visor.cmd.*;
import org.gridgain.grid.kernal.visor.cmd.dto.*;
import org.gridgain.grid.lang.*;
import org.gridgain.grid.portables.*;
import org.gridgain.grid.util.typedef.internal.*;

import java.util.*;

/**
 * Task that collects portables metadata.
 */
@GridInternal
public class VisorPortableCollectMetadataTask extends VisorOneNodeTask<Long, GridBiTuple<Long, Collection<VisorPortableMetadata>>> {
    /** */
    private static final long serialVersionUID = 0L;

    /** {@inheritDoc} */
    @Override protected VisorPortableCollectMetadataJob job(Long lastUpdate) {
        return new VisorPortableCollectMetadataJob(lastUpdate);
    }

    /** Job that collect portables metadata on node. */
    private static class VisorPortableCollectMetadataJob extends VisorJob<Long, GridBiTuple<Long, Collection<VisorPortableMetadata>>> {
        /** */
        private static final long serialVersionUID = 0L;

        /** Create job with given argument. */
        private VisorPortableCollectMetadataJob(Long lastUpdate) {
            super(lastUpdate);
        }

        /** {@inheritDoc} */
        @Override protected GridBiTuple<Long, Collection<VisorPortableMetadata>> run(Long lastUpdate) throws GridException {
            final GridPortables p = g.portables();

            final Collection<VisorPortableMetadata> data = new ArrayList<>(p.metadata().size());

            for(GridPortableMetadata metadata: p.metadata()) {
                final VisorPortableMetadata type = new VisorPortableMetadata();

                type.typeName(metadata.typeName());

                type.typeId(p.typeId(metadata.typeName()));

                final Collection<VisorPortableMetadataField> fields = new ArrayList<>(metadata.fields().size());

                for (String fieldName: metadata.fields()) {
                    final VisorPortableMetadataField field = new VisorPortableMetadataField();

                    field.fieldName(fieldName);
                    field.fieldTypeName(metadata.fieldTypeName(fieldName));

                    fields.add(field);
                }

                type.fields(fields);

                data.add(type);
            }

            return new GridBiTuple<>(0L, data);
        }

        /** {@inheritDoc} */
        @Override public String toString() {
            return S.toString(VisorPortableCollectMetadataJob.class, this);
        }
    }
}
