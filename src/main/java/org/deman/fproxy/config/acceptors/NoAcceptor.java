package org.deman.fproxy.config.acceptors;

import org.deman.fproxy.config.Acceptor;
import org.deman.fproxy.http.Headers;

public class NoAcceptor implements Acceptor {

    @Override
    public boolean accept(Headers prolog) {
        return false;
    }
}
