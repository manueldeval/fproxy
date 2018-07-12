package org.deman.fproxy.proxyhandlers;

import io.vertx.core.net.NetSocket;
import org.deman.fproxy.ProxyError;
import org.deman.fproxy.http.Prolog;

public class NoRuleProxyHandler implements ProxyHandler {

    @Override
    public void handle(NetSocket browserSocket, Prolog prolog) {
        ProxyError.sendError(browserSocket,"No proxy rule found.");
    }
}
