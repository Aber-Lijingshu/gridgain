/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.resources;

import org.gridgain.grid.*;
import org.gridgain.grid.compute.*;
import org.gridgain.grid.spi.*;

import java.lang.annotation.*;

/**
 * Annotates a field or a setter method for injection of GridGain installation home path.
 * GridGain installation home is provided to grid via {@link GridConfiguration}.
 * <p>
 * Home path can be injected into instances of following classes:
 * <ul>
 * <li>{@link GridComputeTask}</li>
 * <li>{@link GridComputeJob}</li>
 * <li>{@link GridSpi}</li>
 * <li>{@link GridLifecycleBean}</li>
 * <li>{@link GridUserResource @GridUserResource}</li>
 * </ul>
 * <p>
 * Here is how injection would typically happen:
 * <pre name="code" class="java">
 * public class MyGridJob implements GridComputeJob {
 *      ...
 *      &#64;GridHomeResource
 *      private String home;
 *      ...
 *  }
 * </pre>
 * or
 * <pre name="code" class="java">
 * public class MyGridJob implements GridComputeJob {
 *     ...
 *     private String home;
 *     ...
 *     &#64;GridHomeResource
 *     public void setGridGainHome(String home) {
 *          this.home = home;
 *     }
 *     ...
 * }
 * </pre>
 * <p>
 * See {@link GridConfiguration#getGridGainHome()} for Grid configuration details.
 *
 * @author @java.author
 * @version @java.version
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface GridHomeResource {
    // No-op.
}
