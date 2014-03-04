/* @cpp.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

#ifndef GRID_CLIENTMESSAGE_COMMAND_HPP_INCLUDED
#define GRID_CLIENTMESSAGE_COMMAND_HPP_INCLUDED

#include "gridgain/impl/cmd/gridclientmessage.hpp"

/**
 * Generic grid message command.
 *
 * @author @cpp.author
 * @version @cpp.version
 */
class GridClientMessageCommand : public GridClientMessage {
public:
    /** Virtual destructor. */
    virtual ~GridClientMessageCommand() {};
};

#endif //GRID_CLIENTMESSAGE_COMMAND_HPP_INCLUDED
