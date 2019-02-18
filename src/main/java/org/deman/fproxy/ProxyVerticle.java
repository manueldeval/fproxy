package org.deman.fproxy;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.NetSocket;
import org.deman.fproxy.config.ConfigException;
import org.deman.fproxy.config.ConfigLoader;
import org.deman.fproxy.http.PrologFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyVerticle.class);

    public static final String CONFIG_FILE = "config.file";
    public static final String DEFAULT_CONFIG_FILE = "config.file";

    public static final String HTTP_PORT = "proxy.port";
    public static final int DEFAULT_PORT = 8181;

    private RuleRouter ruleRouter;
    private Integer httpPort;

    public void start() {
        this.httpPort = config().getInteger(HTTP_PORT, DEFAULT_PORT);
        String configFile = config().getString(CONFIG_FILE, DEFAULT_CONFIG_FILE);
        try {
            this.ruleRouter = new RuleRouter(this.vertx, ConfigLoader.load(configFile).getRules());
        } catch (ConfigException e) {
            LOGGER.error("Unable to read the config file: '{}'",configFile,e);
        }

        LOGGER.info("Starting HTTP server on port {}", this.httpPort);
        vertx.createNetServer()
            .connectHandler(this::httpRequestHandler)
            .listen(this.httpPort);
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

