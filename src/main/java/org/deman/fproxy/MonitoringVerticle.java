package org.deman.fproxy;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.dropwizard.MetricsService;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonitoringVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleRouter.class);

    public static final String METRICS_PORT = "metrics.port";
    public static final int DEFAULT_PORT = 7676;

    private static final String CONTENT_TYPE = "content-type";
    private static final String APPLICATION_JSON = "application/json";

    public void start(Future<Void> fut) {
        // Create a router object.
        Router router = Router.router(vertx);
        MetricsService metricsService = MetricsService.create(vertx);

        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response
                .putHeader(CONTENT_TYPE, APPLICATION_JSON)
                .end(metricsService.getMetricsSnapshot(vertx).encodePrettily());
        });

        Integer port = config().getInteger(METRICS_PORT, DEFAULT_PORT);

        HttpServer server = vertx
            .createHttpServer()
            .requestHandler(router::accept)
            .listen(
                port,
                result -> {
                    if (result.succeeded()) {
                        LOGGER.info("Monitoring started on port: {}",port);
                        fut.complete();
                    } else {
                        fut.fail(result.cause());
                    }
                }
            );

    }
}
