/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.visor.gui.tasks;

import org.gridgain.grid.*;
import org.gridgain.grid.ggfs.GridGgfsMode;
import org.gridgain.grid.kernal.processors.task.GridInternal;
import org.gridgain.grid.kernal.visor.cmd.*;
import org.gridgain.grid.kernal.visor.gui.dto.*;

import static org.gridgain.grid.kernal.ggfs.hadoop.GridGgfsHadoopLogger.*;
import static org.gridgain.grid.kernal.visor.gui.tasks.VisorHadoopTaskUtilsEnt.*;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

/**
 * Task that parse hadoop profiler logs.
 */
@GridInternal
public class VisorGgfsProfilerTask extends VisorOneNodeTask<VisorGgfsProfilerTask.VisorGgfsProfilerArg, Collection<VisorGgfsProfilerEntry>> {
    /**
     * Arguments for {@link VisorGgfsProfilerTask}.
     */
    @SuppressWarnings("PublicInnerClass")
    public static class VisorGgfsProfilerArg extends VisorOneNodeArg {
        /** */
        private static final long serialVersionUID = 0L;

        /** */
        private final String ggfsName;

        /**
         * @param nodeId Node Id.
         * @param ggfsName GGFS instance name.
         */
        public VisorGgfsProfilerArg(UUID nodeId, String ggfsName) {
            super(nodeId);

            this.ggfsName = ggfsName;
        }
    }

    /** Holder class for parsed data. */
    private static class VisorGgfsProfilerParsedLine {
        private final long ts;
        private final int entryType;
        private final String path;
        private final GridGgfsMode mode;
        private final long streamId;
        private final long dataLen;
        private final boolean append;
        private final boolean overwrite;
        private final long pos;
        private final int readLen;
        private final long userTime;
        private final long sysTime;
        private final long totalBytes;

        public VisorGgfsProfilerParsedLine(
            long ts,
            int entryType,
            String path,
            GridGgfsMode mode,
            long streamId,
            long dataLen,
            boolean append,
            boolean overwrite,
            long pos,
            int readLen,
            long userTime,
            long sysTime,
            long totalBytes
        ) {
            this.ts = ts;
            this.entryType = entryType;
            this.path = path;
            this.mode = mode;
            this.streamId = streamId;
            this.dataLen = dataLen;
            this.append = append;
            this.overwrite = overwrite;
            this.pos = pos;
            this.readLen = readLen;
            this.userTime = userTime;
            this.sysTime = sysTime;
            this.totalBytes = totalBytes;
        }
    }

    private static class VisorGgfsProfilerParsedLineComparator implements Comparator<VisorGgfsProfilerParsedLine> {
        @Override public int compare(VisorGgfsProfilerParsedLine a, VisorGgfsProfilerParsedLine b) {
            return a.ts < b.ts ? -1
                : a.ts > b.ts ? 1
                : 0;
        }
    }

    private static final VisorGgfsProfilerParsedLineComparator PARSED_LINE_BY_TS_COMPARATOR
        = new VisorGgfsProfilerParsedLineComparator();

    /**
     * Job that do actual profiler work.
     */
    @SuppressWarnings("PublicInnerClass")
    class VisorGgfsProfilerJob extends VisorOneNodeJob<VisorGgfsProfilerArg, Collection<VisorGgfsProfilerEntry>> {
        /** */
        private static final long serialVersionUID = 0L;

        /**
         * Create job with specified argument.
         *
         * @param arg Job argument.
         */
        public VisorGgfsProfilerJob(VisorGgfsProfilerArg arg) {
            super(arg);
        }

        @Override protected Collection<VisorGgfsProfilerEntry> run(VisorGgfsProfilerArg arg) throws GridException {
            try {
                Path logsDir = resolveGgfsProfilerLogsDir(g.ggfs(arg.ggfsName));

                if (logsDir != null)
                    return parse(logsDir, arg.ggfsName);
                else
                    return Collections.emptyList();
            }
            catch (IOException | IllegalArgumentException e) {
                throw new GridException("Failed to parse profiler logs for GGFS: " + arg.ggfsName, e);
            }
        }

        /**
         * Parse boolean.
         */
        private boolean parseBoolean(String[] ss, int ix) {
            return ix < ss.length && "1".equals(ss[ix]);
        }

        private int parseInt(String[] ss, int ix, int dflt) {
            if (ss.length <= ix)
                return dflt;
            else {
                String s = ss[ix];

                return s.isEmpty() ? dflt : Integer.parseInt(s);
            }
        }

        private long parseLong(String[] ss, int ix, long dflt) {
            if (ss.length <= ix)
                return dflt;
            else {
                String s = ss[ix];

                return s.isEmpty() ? dflt : Long.parseLong(s);
            }
        }

        private String parseString(String[] ss, int ix) {
            if (ss.length <= ix)
                return "";
            else {
                String s = ss[ix];

                return s.isEmpty() ? "" : s;
            }
        }

        private GridGgfsMode parseGgfsMode(String[] ss, int ix) {
            if (ss.length <= ix)
                return null;
            else {
                String s = ss[ix];

                return s.isEmpty() ? null : GridGgfsMode.valueOf(s);
            }
        }

        /**
         * Parse line from log.
         *
         * @param s Line with text to parse.
         * @return Parsed data.
         */
        private VisorGgfsProfilerParsedLine parseLine(String s) {
            String[] ss = s.split(DELIM_FIELD);

            long streamId = parseLong(ss, LOG_COL_STREAM_ID, -1);

            if (streamId >= 0) {
                int entryType = parseInt(ss, LOG_COL_ENTRY_TYPE, -1);

                // Parse only needed types.
                if (LOG_TYPES.contains(entryType))
                    return new VisorGgfsProfilerParsedLine(
                        parseLong(ss, LOG_COL_TIMESTAMP, 0),
                        entryType,
                        parseString(ss, LOG_COL_PATH),
                        parseGgfsMode(ss, LOG_COL_GGFS_MODE),
                        streamId,
                        parseLong(ss, LOG_COL_DATA_LEN, 0),
                        parseBoolean(ss, LOG_COL_APPEND),
                        parseBoolean(ss, LOG_COL_OVERWRITE),
                        parseLong(ss, LOG_COL_POS, 0),
                        parseInt(ss, LOG_COL_READ_LEN, 0),
                        parseLong(ss, LOG_COL_USER_TIME, 0),
                        parseLong(ss, LOG_COL_SYSTEM_TIME, 0),
                        parseLong(ss, LOG_COL_TOTAL_BYTES, 0)
                    );
            }

            return null;
        }

        /**
         * Aggregate information from parsed lines grouped by `streamId`.
         */
        private VisorGgfsProfilerEntry aggregateParsedLines(List<VisorGgfsProfilerParsedLine> lines) {
            String path = "";
            long ts = 0;
            long size = 0;
            long bytesRead = 0;
            long readTime = 0;
            long userReadTime = 0;
            long bytesWritten = 0;
            long writeTime = 0;
            long userWriteTime = 0;
            GridGgfsMode mode = null;

            VisorGgfsProfilerUniformityCounters counters = new VisorGgfsProfilerUniformityCounters();

            Collections.sort(lines, PARSED_LINE_BY_TS_COMPARATOR);

            for (VisorGgfsProfilerParsedLine line : lines) {
                if (!line.path.isEmpty())
                    path = line.path;

                ts = line.ts; // Remember last timestamp.

                // Remember last GGFS mode.
                mode = line.mode;

                switch (line.entryType) {
                    case TYPE_OPEN_IN:
                        size = line.dataLen; // Remember last file size.

                        counters.invalidate(size);
                        break;

                    case TYPE_OPEN_OUT:
                        if (line.overwrite) {
                            size = 0; // If file was overridden, set size to zero.

                            counters.invalidate(size);
                        }
                        break;

                    case TYPE_CLOSE_IN:
                        bytesRead += line.totalBytes; // Add to total bytes read.
                        readTime += line.sysTime; // Add to read time.
                        userReadTime += line.userTime; // Add to user read time.

                        counters.increment(line.pos, line.totalBytes);

                        break;

                    case TYPE_CLOSE_OUT:
                        size += line.totalBytes; // Add to files size.
                        bytesWritten += line.totalBytes; // Add to total bytes written.
                        writeTime += line.sysTime; // Add to write time.
                        userWriteTime += line.userTime; // Add to user write time.

                        counters.invalidate(size);

                        break;

                    case TYPE_RANDOM_READ:
                        counters.increment(line.pos, line.totalBytes);

                        break;

                    default:
                        throw new IllegalStateException("Unexpected GGFS profiler log entry type: " + line.entryType);
                }
            }

            // Return only fully parsed data with path.
            if (!path.isEmpty())
                return new VisorGgfsProfilerEntry(
                    path,
                    ts,
                    mode,
                    size,
                    bytesRead,
                    readTime,
                    userReadTime,
                    bytesWritten,
                    writeTime,
                    userWriteTime,
                    counters);
            else
                return null;
        }

        private Collection<VisorGgfsProfilerEntry> parseFile(Path p) throws IOException {
            ArrayList<VisorGgfsProfilerParsedLine> parsedLines = new ArrayList<>(512);

            try (BufferedReader br = Files.newBufferedReader(p, Charset.forName("UTF-8"))) {
                String line = br.readLine(); // Skip first line with columns header.

                if (line != null) {
                    // Check file header.
                    if (line.equalsIgnoreCase(HDR))
                        line = br.readLine();

                        while (line != null) {
                            try {
                                VisorGgfsProfilerParsedLine ln = parseLine(line);

                                if (ln != null)
                                    parsedLines.add(ln);
                            }
                            catch (NumberFormatException ignored) {
                                // Skip invalid lines.
                            }

                            line = br.readLine();
                        }
                }
            }

            // Group parsed lines by streamId.
            Map<Long, List<VisorGgfsProfilerParsedLine>> byStreamId = new HashMap<>();

            for (VisorGgfsProfilerParsedLine line: parsedLines) {
                List<VisorGgfsProfilerParsedLine> grp = byStreamId.get(line.streamId);

                if (grp == null) {
                    grp = new ArrayList<>();

                    byStreamId.put(line.streamId, grp);
                }

                grp.add(line);
            }

            // Aggregate each group.
            List<VisorGgfsProfilerEntry> entries = new ArrayList<>(byStreamId.size());

            for (List<VisorGgfsProfilerParsedLine> lines : byStreamId.values()) {
                VisorGgfsProfilerEntry entry = aggregateParsedLines(lines);

                if (entry != null)
                    entries.add(entry);
            }

            // Group by files.
            Map<String, List<VisorGgfsProfilerEntry>> byPath = new HashMap<>();

            for (VisorGgfsProfilerEntry entry: entries) {
                List<VisorGgfsProfilerEntry> grp = byPath.get(entry.path());

                if (grp == null) {
                    grp = new ArrayList<>();

                    byPath.put(entry.path(), grp);
                }

                grp.add(entry);
            }

            // Aggregate by files.
            List<VisorGgfsProfilerEntry> res = new ArrayList<>(byPath.size());

            for (List<VisorGgfsProfilerEntry> lst : byPath.values())
                res.add(VisorGgfsProfiler.aggregateGgfsProfilerEntries(lst));

            return res;
        }

        /**
         * Parse all GGFS log files in specified log directory.
         *
         * @param logDir Folder were log files located.
         * @return List of line with aggregated information by files.
         */
        private Collection<VisorGgfsProfilerEntry> parse(Path logDir, String ggfsName) throws IOException {
            ArrayList<VisorGgfsProfilerEntry> parsedFiles = new ArrayList<>(512);

            try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(logDir)) {
                PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:ggfs-log-" + ggfsName + "-*.csv");

                for (Path p : dirStream) {
                    if (matcher.matches(p.getFileName())) {
                        try {
                            parsedFiles.addAll(parseFile(p));
                        }
                        catch (NoSuchFileException ignored) {
                            // Files was deleted, skip it.
                        }
                        catch (Exception e) {
                            g.log().warning("Failed to parse GGFS profiler log file: " + p, e);
                        }
                    }
                }
            }

            return parsedFiles;
        }
    }

    @Override protected VisorGgfsProfilerJob job(VisorGgfsProfilerArg arg) {
        return new VisorGgfsProfilerJob(arg);
    }
}
