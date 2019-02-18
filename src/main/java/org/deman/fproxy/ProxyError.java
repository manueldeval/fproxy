package org.deman.fproxy;

import io.vertx.core.net.NetSocket;

public class ProxyError {

    public static void sendError(NetSocket browserSocket,String message) {
        browserSocket.write("HTTP/1.1 502 OK\r\nCache-Control: no-cache, private\r\n\r\n"+message);
        browserSocket.close();
    }

    public static void sendError(NetSocket browserSocket) {
        sendError(browserSocket,"ProxyVerticle error.");
    }
}
