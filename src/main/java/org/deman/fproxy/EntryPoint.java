package org.deman.fproxy;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.logging.SLF4JLogDelegateFactory;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import org.deman.fproxy.utils.Env;

import static io.vertx.core.logging.LoggerFactory.LOGGER_DELEGATE_FACTORY_CLASS_NAME;

public class EntryPoint {
    private static final String PROXY_HTTP_PORT = "PROXY_HTTP_PORT";
    private static final String MON_HTTP_PORT = "MON_HTTP_PORT";
    private static final String WORKER_POOL_SIZE = "WORKER_POOL_SIZE";
    private static final String CONFIG_FILE = "CONFIG_FILE";

    private Integer workerPoolSize;
    private Integer proxyHttpPort;
    private Integer monitoringHttpPort;
    private String configFile;
    private Vertx vertx;

    public EntryPoint(Integer workerPoolSize, Integer proxyHttpPort, Integer monitoringHttpPort, String configFile) {
        this.workerPoolSize = workerPoolSize;
        this.proxyHttpPort = proxyHttpPort;
        this.monitoringHttpPort = monitoringHttpPort;
        this.configFile = configFile;
    }

    public void start() {
        this.setupLogging();
        this.vertx = Vertx.vertx(createVertxOptions());
        this.deployVerticles();

    }

    private void deployVerticles() {
        deployProxy();
        deployMonitoring();
    }

    private void deployMonitoring() {
        DeploymentOptions options = new DeploymentOptions()
            .setConfig(new JsonObject()
                .put(MonitoringVerticle.METRICS_PORT, monitoringHttpPort));
        this.vertx.deployVerticle(MonitoringVerticle.class.getName(), options);
    }

    private void deployProxy() {
        DeploymentOptions options = new DeploymentOptions()
            .setConfig(new JsonObject()
                .put(ProxyVerticle.HTTP_PORT, proxyHttpPort)
                .put(ProxyVerticle.CONFIG_FILE, configFile));
        this.vertx.deployVerticle(ProxyVerticle.class.getName(), options);
    }

    private VertxOptions createVertxOptions() {
        VertxOptions options = new VertxOptions();
        options.setMetricsOptions(new DropwizardMetricsOptions().setEnabled(true));
        options.setWorkerPoolSize(this.workerPoolSize);
        return options;
    }

    private void setupLogging() {
        System.setProperty(LOGGER_DELEGATE_FACTORY_CLASS_NAME, SLF4JLogDelegateFactory.class.getName());
        LoggerFactory.getLogger(LoggerFactory.class);
    }

    public static void main(String[] args) {
        EntryPoint entryPoint = new EntryPoint(
            Env.getInteger(WORKER_POOL_SIZE, 1),
            Env.getInteger(PROXY_HTTP_PORT, 8888),
            Env.getInteger(MON_HTTP_PORT, 7676),
            Env.getString(CONFIG_FILE, "config.yml"));
        entryPoint.start();
    }
}
