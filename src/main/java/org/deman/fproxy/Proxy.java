package org.deman.fproxy;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.net.NetSocket;
import org.deman.fproxy.config.Config;
import org.deman.fproxy.http.PrologFinder;
import org.deman.fproxy.wstunnel.WsPseudoNetSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Proxy {
    private static final Logger LOGGER = LoggerFactory.getLogger(Proxy.class);

    private Vertx vertx;
    private Config configuration;
    private RuleRouter ruleRouter;

    public Proxy(Vertx vertx, Config configuration) {
        this.vertx = vertx;
        this.configuration = configuration;
        this.ruleRouter = new RuleRouter(vertx, configuration.getRules());
    }

    public void start() {
        startHttpServer();
        startWsTunnel();
    }

    private void startHttpServer() {
        if (configuration.getPort() != null) {
            LOGGER.info("Starting HTTP server on port {}", configuration.getPort());
            vertx.createNetServer()
                .connectHandler(this::httpRequestHandler)
                .listen(configuration.getPort());
        }
    }

    private void startWsTunnel() {
        if (configuration.getWsTunnelPort() != null) {
            LOGGER.info("Starting WS Tunnel server on port {}", configuration.getWsTunnelPort());
            HttpServer server = vertx.createHttpServer();
            server.websocketHandler(websocket -> httpRequestHandler(new WsPseudoNetSocket(websocket)))
                .listen(configuration.getWsTunnelPort());
        }
    }

    private void httpRequestHandler(NetSocket browserSocket) {
        PrologFinder prologFinder = new PrologFinder();
        browserSocket.handler(b -> {
            prologFinder.onBuffer(b).peek(prolog -> {
                LOGGER.debug("> " + prolog.getMethod() + " " + prolog.getRequestUri() + " " + prolog.getHttpVersion() + " HOST:" + prolog.getHost() + ":" + prolog.getPort());
                ruleRouter.route(browserSocket, prolog);
            });
        });
    }
}

