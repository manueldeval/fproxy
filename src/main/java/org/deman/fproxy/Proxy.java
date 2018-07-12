package org.deman.fproxy;

import io.vavr.control.Option;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetSocket;
import org.deman.fproxy.config.Config;
import org.deman.fproxy.http.Prolog;
import org.deman.fproxy.http.PrologFinder;
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

    private void requestHandler(NetSocket browserSocket) {
        PrologFinder prologFinder = new PrologFinder();
        browserSocket.handler(b -> {
            prologFinder.onBuffer(b).peek(prolog -> {
                LOGGER.debug("> " + prolog.getMethod() + " " + prolog.getRequestUri() + " " + prolog.getHttpVersion() + " HOST:" + prolog.getHost() + ":" + prolog.getPort());
                ruleRouter.route(browserSocket, prolog);
            });
        });
    }

    public void start() {
        vertx.createNetServer()
            .connectHandler(this::requestHandler)
            .listen(configuration.getPort());
    }
}
