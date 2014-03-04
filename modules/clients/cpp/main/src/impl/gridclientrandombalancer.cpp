/* @cpp.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */
#include "gridgain/impl/utils/gridclientdebug.hpp"

#include <cstdlib>
#include <ctime>

#include "gridgain/loadbalancer/gridclientrandombalancer.hpp"

/** Default constructor. */
GridClientRandomBalancer::GridClientRandomBalancer() {
        srand((int)time(NULL));
};

/**
 * Gets next node for executing client command.
 *
 * @param nodes Nodes to pick from.
 * @return Next node to pick.
 */
TGridClientNodePtr GridClientRandomBalancer::balancedNode(const TGridClientNodeList& pNodes) {
    size_t n = rand() % pNodes.size();

    return pNodes[n];
}
