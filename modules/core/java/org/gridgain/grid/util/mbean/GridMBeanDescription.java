/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.util.mbean;

import java.lang.annotation.*;

/**
 * Provides description for MBean classes and methods.
 *
 * @author @java.author
 * @version @java.version
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface GridMBeanDescription {
    /**
     *
     * Description for Mbean.
     */
    @SuppressWarnings({"JavaDoc"}) public String value();
}
