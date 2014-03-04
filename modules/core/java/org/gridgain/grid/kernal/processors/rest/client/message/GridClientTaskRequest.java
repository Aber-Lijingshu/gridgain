/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.rest.client.message;

import org.gridgain.grid.util.typedef.internal.*;

import java.io.*;

/**
 * {@code Task} command request.
 *
 * @author @java.author
 * @version @java.version
 */
public class GridClientTaskRequest extends GridClientAbstractMessage {
    /** Task name. */
    private String taskName;

    /** Task parameter. */
    private Object arg;

    /**
     * @return Task name.
     */
    public String taskName() {
        return taskName;
    }

    /**
     * @param taskName Task name.
     */
    public void taskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * @return Arguments.
     */
    public Object argument() {
        return arg;
    }

    /**
     * @param arg Arguments.
     */
    public void argument(Object arg) {
        this.arg = arg;
    }

    /** {@inheritDoc} */
    @Override public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        GridClientTaskRequest other = (GridClientTaskRequest)o;

        return (taskName == null ? other.taskName == null : taskName.equals(other.taskName)) &&
            arg == null ? other.arg == null : arg.equals(other.arg);
    }

    /** {@inheritDoc} */
    @Override public int hashCode() {
        return (taskName == null ? 0 : taskName.hashCode()) +
            31 * (arg == null ? 0 : arg.hashCode());
    }

    /** {@inheritDoc} */
    @Override public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);

        U.writeString(out, taskName);

        out.writeObject(arg);
    }

    /** {@inheritDoc} */
    @Override public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);

        taskName = U.readString(in);

        arg = in.readObject();
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return getClass().getSimpleName() + " [taskName=" + taskName + ", arg=" + arg + "]";
    }
}
