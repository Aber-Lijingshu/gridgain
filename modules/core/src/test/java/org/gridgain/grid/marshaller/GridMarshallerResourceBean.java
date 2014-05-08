/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.marshaller;

import org.gridgain.grid.*;
import org.gridgain.grid.compute.*;
import org.gridgain.grid.kernal.managers.loadbalancer.*;
import org.gridgain.grid.logger.*;
import org.gridgain.grid.logger.java.*;
import org.gridgain.grid.marshaller.jdk.*;
import org.gridgain.grid.thread.*;
import org.springframework.context.*;
import org.springframework.context.support.*;

import javax.management.*;
import java.io.*;
import java.lang.management.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Marshaller resource bean.
 */
class GridMarshallerResourceBean implements Serializable {
    /** Logger. */
    private GridLogger log;

    /** Marshaller. */
    private GridMarshaller marshaller;

    /** Load balancer. */
    private GridComputeLoadBalancer balancer;

    /** MBean server. */
    private MBeanServer mbeanSrv;

    /** Session. */
    private GridComputeTaskSession ses;

    /** Executor service. */
    private ExecutorService execSvc;

    /** Application context. */
    private ApplicationContext appCtx;

    /** Job context. */
    private GridComputeJobContext jobCtx;

    /**
     * Initialization.
     */
    GridMarshallerResourceBean() {
        log = new GridJavaLogger();
        marshaller = new GridJdkMarshaller();
        mbeanSrv = ManagementFactory.getPlatformMBeanServer();
        ses = new GridTestTaskSession();
        execSvc = new GridThreadPoolExecutor(1, 1, 0, new LinkedBlockingQueue<Runnable>());
        appCtx = new GenericApplicationContext();
        jobCtx = new GridTestJobContext();
        balancer = new LoadBalancer();
    }

    /**
     * Checks that all resources are null.
     */
    void checkNullResources() {
        assert log == null;
        assert marshaller == null;
        assert balancer == null;
        assert mbeanSrv == null;
        assert ses == null;
        assert execSvc == null;
        assert appCtx == null;
        assert jobCtx == null;
    }

    /** */
    private static class LoadBalancer extends GridLoadBalancerAdapter {
        /** */
        public LoadBalancer() {
        }

        /** {@inheritDoc} */
        @Override public GridNode getBalancedNode(GridComputeJob job, Collection<GridNode> exclNodes) {
            return null;
        }
    }
}
