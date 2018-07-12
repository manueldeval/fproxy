package org.deman.fproxy.proxyhandlers;

import io.vertx.core.net.NetSocket;
import org.deman.fproxy.http.Prolog;

public interface ProxyHandler {
    void handle(NetSocket socket, Prolog prolog);
}
