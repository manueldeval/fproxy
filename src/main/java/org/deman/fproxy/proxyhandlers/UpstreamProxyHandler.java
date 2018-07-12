package org.deman.fproxy.proxyhandlers;

import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetSocket;
import org.deman.fproxy.ProxyError;
import org.deman.fproxy.config.rules.UpstreamRule;
import org.deman.fproxy.http.Prolog;

import java.util.Base64;

public class UpstreamProxyHandler implements ProxyHandler {

    private Vertx vertx;
    private UpstreamRule upstreamRule;

    public UpstreamProxyHandler(Vertx vertx, UpstreamRule upstreamRule) {
        this.vertx = vertx;
        this.upstreamRule = upstreamRule;
    }

    @Override
    public void handle(NetSocket browserSocket, Prolog prolog) {

        vertx.createNetClient().connect(this.upstreamRule.getPort(), this.upstreamRule.getHost(), (AsyncResult<NetSocket> serverSocketAsync) -> {
            if (serverSocketAsync.succeeded()) {

                if (upstreamRule.getUser()!= null && upstreamRule.getPassword()!=null) {
                    String encoded = Base64.getEncoder().encodeToString((upstreamRule.getUser() + ":" + upstreamRule.getPassword()).getBytes());
                    prolog.setHeader("Proxy-Authorization", "Basic " + encoded);
                }

                NetSocket serverSocket = serverSocketAsync.result();
                serverSocket.write(prolog.getBuffer());

                browserSocket.handler(serverSocket::write);
                serverSocket.handler(browserSocket::write);
                serverSocket.closeHandler((s) -> browserSocket.close());
                browserSocket.closeHandler((s) -> serverSocket.close());
            } else {
                ProxyError.sendError(browserSocket, "Unable to connect to upstream server.");
            }
        });
    }
}
