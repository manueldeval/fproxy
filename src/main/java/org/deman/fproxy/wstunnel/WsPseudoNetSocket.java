package org.deman.fproxy.wstunnel;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.net.NetSocket;
import io.vertx.core.net.SocketAddress;

import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.security.cert.X509Certificate;
import java.io.UnsupportedEncodingException;

public class WsPseudoNetSocket implements NetSocket {

    private ServerWebSocket serverWebSocket;

    public WsPseudoNetSocket(ServerWebSocket serverWebSocket) {
        this.serverWebSocket = serverWebSocket;
    }

    @Override
    public WsPseudoNetSocket exceptionHandler(Handler<Throwable> handler) {
        serverWebSocket.exceptionHandler(handler);
        return this;
    }

    @Override
    public WsPseudoNetSocket handler(Handler<Buffer> handler) {
        serverWebSocket.handler(handler);
        return this;
    }

    @Override
    public WsPseudoNetSocket pause() {
        serverWebSocket.pause();
        return this;
    }

    @Override
    public WsPseudoNetSocket resume() {
        serverWebSocket.resume();
        return this;
    }

    @Override
    public WsPseudoNetSocket endHandler(Handler<Void> endHandler) {
        serverWebSocket.endHandler(endHandler);
        return this;
    }

    @Override
    public WsPseudoNetSocket write(Buffer data) {
        serverWebSocket.write(data);
        return this;
    }

    @Override
    public WsPseudoNetSocket setWriteQueueMaxSize(int maxSize) {
        serverWebSocket.setWriteQueueMaxSize(maxSize);
        return this;
    }

    @Override
    public boolean writeQueueFull() {
        return serverWebSocket.writeQueueFull();
    }

    @Override
    public WsPseudoNetSocket drainHandler(Handler<Void> handler) {
        serverWebSocket.drainHandler(handler);
        return this;
    }

    @Override
    public String writeHandlerID() {
        return serverWebSocket.binaryHandlerID();
    }

    @Override
    public WsPseudoNetSocket write(String str) {
        serverWebSocket.write(Buffer.buffer(str));
        return this;
    }

    @Override
    public WsPseudoNetSocket write(String str, String enc) {
        try {
            serverWebSocket.write(Buffer.buffer(str.getBytes(enc)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public WsPseudoNetSocket sendFile(String filename, long offset, long length) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public WsPseudoNetSocket sendFile(String filename, long offset, long length, Handler<AsyncResult<Void>> resultHandler) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public SocketAddress remoteAddress() {
        return serverWebSocket.remoteAddress();
    }

    @Override
    public SocketAddress localAddress() {
        return serverWebSocket.localAddress();
    }

    @Override
    public void end() {
        serverWebSocket.end();
    }

    @Override
    public void close() {
        serverWebSocket.close();
    }

    @Override
    public WsPseudoNetSocket closeHandler(Handler<Void> handler) {
        serverWebSocket.closeHandler(handler);
        return this;
    }

    @Override
    public WsPseudoNetSocket upgradeToSsl(Handler<Void> handler) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public WsPseudoNetSocket upgradeToSsl(String serverName, Handler<Void> handler) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public boolean isSsl() {
        return serverWebSocket.isSsl();
    }

    @Override
    public SSLSession sslSession() {
        return serverWebSocket.sslSession();
    }

    @Override
    public X509Certificate[] peerCertificateChain() throws SSLPeerUnverifiedException {
        return serverWebSocket.peerCertificateChain();
    }

    @Override
    public String indicatedServerName() {
        throw new UnsupportedOperationException("Not implemented.");
    }
}
