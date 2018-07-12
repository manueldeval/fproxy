package org.deman.fproxy.proxyhandlers;

import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetSocket;
import org.deman.fproxy.ProxyError;
import org.deman.fproxy.http.Prolog;

public class DirectProxyHandler implements ProxyHandler {

    private Vertx vertx;

    public DirectProxyHandler(Vertx vertx){
        this.vertx = vertx;
    }

    @Override
    public void handle(NetSocket browserSocket, Prolog prolog) {
        vertx.createNetClient().connect(prolog.getPort(), prolog.getHost(), (AsyncResult<NetSocket> serverSocketAsync) -> {
            if (serverSocketAsync.succeeded()) {
                NetSocket serverSocket = serverSocketAsync.result();
                if (!prolog.getMethod().equals("CONNECT")) {
                    serverSocket.write(prolog.getBuffer());
                } else {
                    browserSocket.write("HTTP/1.1 200 Connection established\r\n\r\n");
                }
                browserSocket.handler(serverSocket::write);
                serverSocket.handler(browserSocket::write);
                serverSocket.closeHandler((s) -> browserSocket.close());
                browserSocket.closeHandler((s) -> serverSocket.close());
            } else {
                ProxyError.sendError(browserSocket,"Unable to connect to upstream server.");
            }
        });
    }
}
