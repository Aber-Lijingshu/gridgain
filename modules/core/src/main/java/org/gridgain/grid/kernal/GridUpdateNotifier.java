/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal;

import org.gridgain.grid.*;
import org.gridgain.grid.kernal.processors.license.*;
import org.gridgain.grid.logger.*;
import org.gridgain.grid.product.*;
import org.gridgain.grid.util.typedef.*;
import org.gridgain.grid.util.typedef.internal.*;
import org.gridgain.grid.util.worker.*;
import org.jetbrains.annotations.*;
import org.w3c.dom.*;
import org.w3c.dom.Node;
import org.xml.sax.*;

import javax.xml.parsers.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import static java.net.URLEncoder.*;

/**
 * This class is responsible for notification about new version availability. Note that this class
 * does not send any information and merely accesses the {@code www.gridgain.org} web site for the
 * latest version data.
 * <p>
 * Note also that this connectivity is not necessary to successfully start the system as it will
 * gracefully ignore any errors occurred during notification and verification process.
 */
class GridUpdateNotifier {
    /** Access URL to be used to access latest version data. */
    private static final String UPD_STATUS_PARAMS = GridProperties.get("gridgain.update.status.params");

    /** Throttling for logging out. */
    private static final long THROTTLE_PERIOD = 24 * 60 * 60 * 1000; // 1 day.

    /** Max length of package prefixes string. */
    private static final int MAX_PREFIXES_LENGTH = 1024;

    /** Grid version. */
    private final String ver;

    /** Site. */
    private final String url;

    /** Asynchronous checked. */
    private GridWorker checker;

    /** Latest version. */
    private volatile String latestVer;

    /** HTML parsing helper. */
    private final DocumentBuilder documentBuilder;

    /** Grid name. */
    private final String gridName;

    /** Whether or not to report only new version. */
    private boolean reportOnlyNew;

    /** */
    private int topSize;

    /** Package prefixes. */
    private String packages;

    /** */
    private long lastLog = -1;

    /** */
    private GridLicenseProcessor licProc;

    /**
     * Creates new notifier with default values.
     *
     * @param gridName gridName
     * @param ver Compound GridGain version.
     * @param site Site.
     * @param reportOnlyNew Whether or not to report only new version.
     * @throws GridException If failed.
     */
    GridUpdateNotifier(String gridName, String ver, String site, boolean reportOnlyNew)
        throws GridException {
        try {
            packages = getPackages();

            if (packages.length() > MAX_PREFIXES_LENGTH)
                packages = packages.substring(0, MAX_PREFIXES_LENGTH);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            documentBuilder = factory.newDocumentBuilder();

            documentBuilder.setEntityResolver(new EntityResolver() {
                @Override public InputSource resolveEntity(String publicId, String sysId) {
                    if (sysId.endsWith(".dtd"))
                        return new InputSource(new StringReader(""));

                    return null;
                }
            });

            this.ver = ver;

            url = "http://" + site + "/update_status.php";

            this.gridName = gridName == null ? "null" : gridName;
            this.reportOnlyNew = reportOnlyNew;
        }
        catch (ParserConfigurationException e) {
            throw new GridException("Failed to create xml parser.", e);
        }
    }

    /**
     * Gets package prefixes from current stack trace.
     *
     * @return Package prefixes.
     */
    private static String getPackages() {
        Collection<String> prefixes = new HashSet<>();

        for (StackTraceElement trace : Thread.currentThread().getStackTrace()) {
            String cls = trace.getClassName();

            if (cls.startsWith("sun.") || cls.startsWith("lang.") || cls.startsWith("java.") ||
                cls.startsWith("javax.") || cls.startsWith("junit."))
                continue;

            String[] pckgs = cls.split("\\.");

            String pckg = pckgs[0] + (pckgs.length > 1 ? "." + pckgs[1] : "");

            prefixes.add(pckg);
        }

        return prefixes.isEmpty() ? null : F.concat(prefixes, ",");
    }

    /**
     * @param reportOnlyNew Whether or not to report only new version.
     */
    void reportOnlyNew(boolean reportOnlyNew) {
        this.reportOnlyNew = reportOnlyNew;
    }

    /**
     * @param topSize Size of topology for license verification purpose.
     */
    void topologySize(int topSize) {
        this.topSize = topSize;
    }

    /**
     * @param licProc License processor.
     */
    void licenseProcessor(GridLicenseProcessor licProc) {
        this.licProc = licProc;
    }

    /**
     * @return Latest version.
     */
    String latestVersion() {
        return latestVer;
    }

    /**
     * Starts asynchronous process for retrieving latest version data.
     *
     * @param exec Executor service.
     * @param log Logger.
     */
    void checkForNewVersion(Executor exec, GridLogger log) {
        assert log != null;

        log = log.getLogger(getClass());

        try {
            exec.execute(checker = new UpdateChecker(log));
        }
        catch (RejectedExecutionException e) {
            U.error(log, "Failed to schedule a thread due to execution rejection (safely ignoring): " +
                e.getMessage());
        }
    }

    /**
     * Logs out latest version notification if such was received and available.
     *
     * @param log Logger.
     */
    void reportStatus(GridLogger log) {
        assert log != null;

        log = log.getLogger(getClass());

        // Don't join it to avoid any delays on update checker.
        // Checker thread will eventually exit.
        U.cancel(checker);

        String latestVer = this.latestVer;

        if (latestVer != null)
            if (latestVer.equals(ver)) {
                if (!reportOnlyNew)
                    throttle(log, false, "Your version is up to date.");
            }
            else
                throttle(log, true, "New version is available at " + GridKernal.SITE + ": " + latestVer);
        else
            if (!reportOnlyNew)
                throttle(log, false, "Update status is not available.");
    }

    /**
     *
     * @param log Logger to use.
     * @param warn Whether or not this is a warning.
     * @param msg Message to log.
     */
    private void throttle(GridLogger log, boolean warn, String msg) {
        assert(log != null);
        assert(msg != null);

        long now = U.currentTimeMillis();

        if (now - lastLog > THROTTLE_PERIOD) {
            if (!warn)
                U.log(log, msg);
            else {
                U.quiet(true, msg);

                if (log.isInfoEnabled())
                    log.warning(msg);
            }

            lastLog = now;
        }
    }

    /**
     * Asynchronous checker of the latest version available.
     */
    private class UpdateChecker extends GridWorker {
        /** Default encoding. */
        private static final String CHARSET = "UTF-8";

        /** Logger. */
        private final GridLogger log;

        /**
         * Creates checked with given logger.
         *
         * @param log Logger.
         */
        UpdateChecker(GridLogger log) {
            super(gridName, "grid-version-checker", log);

            this.log = log.getLogger(getClass());
        }

        /** {@inheritDoc} */
        @Override protected void body() throws InterruptedException {
            try {
                GridProductLicense lic = licProc != null ? licProc.license() : null;

                String postParams =
                    (F.isEmpty(UPD_STATUS_PARAMS) ? "" : encode(UPD_STATUS_PARAMS, CHARSET) + '&') +
                    (topSize > 0 ? "t=" + topSize + '&' : "") +
                    (lic != null ? "l=" + lic.id() + '&' : "") +
                    "p=" + encode(gridName, CHARSET) + '&' +
                    (packages != null ? "c=" + encode(packages, CHARSET) : "");

                URLConnection conn = new URL(url).openConnection();

                if (!isCancelled()) {
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Accept-Charset", CHARSET);
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + CHARSET);

                    conn.setConnectTimeout(3000);
                    conn.setReadTimeout(3000);

                    Document dom = null;

                    try {
                        try (OutputStream os = conn.getOutputStream()) {
                            os.write(postParams.getBytes(CHARSET));
                        }

                        try (InputStream in = conn.getInputStream()) {
                            if (in == null)
                                return;

                            BufferedReader reader = new BufferedReader(new InputStreamReader(in, CHARSET));

                            StringBuilder xml = new StringBuilder();

                            String line;

                            while ((line = reader.readLine()) != null) {
                                if (line.contains("<meta") && !line.contains("/>"))
                                    line = line.replace(">", "/>");

                                xml.append(line).append('\n');
                            }

                            dom = documentBuilder.parse(new ByteArrayInputStream(xml.toString().getBytes(CHARSET)));
                        }
                    }
                    catch (IOException e) {
                        if (log.isDebugEnabled())
                            log.debug("Failed to connect to GridGain update server. " + e.getMessage());
                    }

                    if (dom != null)
                        latestVer = obtainVersionFrom(dom);
                }
            }
            catch (Exception e) {
                if (log.isDebugEnabled())
                    log.debug("Unexpected exception in update checker. " + e.getMessage());
            }
        }

        /**
         * Gets the version from the current {@code node}, if one exists.
         *
         * @param node W3C DOM node.
         * @return Version or {@code null} if one's not found.
         */
        @Nullable private String obtainVersionFrom(Node node) {
            assert node != null;

            if (node instanceof Element && "meta".equals(node.getNodeName().toLowerCase())) {
                Element meta = (Element)node;

                String name = meta.getAttribute("name");

                if (("version").equals(name)) {
                    String content = meta.getAttribute("content");

                    if (content != null && !content.isEmpty())
                        return content;
                }
            }

            NodeList childNodes = node.getChildNodes();

            for (int i = 0; i < childNodes.getLength(); i++) {
                String ver = obtainVersionFrom(childNodes.item(i));

                if (ver != null)
                    return ver;
            }

            return null;
        }
    }
}
