/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.visor.cmd.dto;

import java.io.Serializable;

/**
 * Data transfer object for query column type description.
 */
public class VisorFieldsQueryColumn implements Serializable {
    /** */
    private static final long serialVersionUID = 0L;

    /** Column type. */
    private final String type;

    /** Field name. */
    private final String field;

    /** Create data transfer object with given parameters. */
    public VisorFieldsQueryColumn(String type, String field) {
        this.type = type;
        this.field = field;
    }

    /**
     * @return Column type.
     */
    public String type() {
        return type;
    }

    /**
     * @return Field name.
     */
    public String field() {
        return field;
    }
}
