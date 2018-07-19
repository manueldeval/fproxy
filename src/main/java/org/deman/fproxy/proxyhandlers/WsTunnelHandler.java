package org.deman.fproxy.proxyhandlers;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.net.NetSocket;
import io.vertx.core.net.ProxyOptions;
import org.deman.fproxy.config.rules.WSTunnelRule;
import org.deman.fproxy.http.Prolog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WsTunnelHandler implements ProxyHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(WsTunnelHandler.class);

    private Vertx vertx;
    private WSTunnelRule rule;

    public WsTunnelHandler(Vertx vertx, WSTunnelRule rule) {
        this.vertx = vertx;
        this.rule = rule;
    }

    @Override
    public void handle(NetSocket browserSocket, Prolog prolog) {
        HttpClientOptions httpClientOptions = new HttpClientOptions();
        if (this.rule.getProxyHost() != null) {
            ProxyOptions proxyOptions = new ProxyOptions();
            proxyOptions.setHost(this.rule.getProxyHost());
            proxyOptions.setPort(this.rule.getProxyPort());
            if (this.rule.getProxyUser() != null) {
                proxyOptions.setUsername(this.rule.getProxyUser());
                proxyOptions.setPassword(this.rule.getProxyPassword());
            }
            httpClientOptions.setProxyOptions(proxyOptions);
        }
        httpClientOptions.setDefaultHost(this.rule.getHost()).setDefaultPort(this.rule.getPort());
        vertx.createHttpClient(httpClientOptions).websocket("/", wsHandler -> {
            wsHandler.write(prolog.getBuffer());
            browserSocket.handler(wsHandler::write);
            wsHandler.handler(browserSocket::write);
            wsHandler.closeHandler((s) -> browserSocket.close());
            browserSocket.closeHandler((s) -> wsHandler.close());
        });
    }
}
