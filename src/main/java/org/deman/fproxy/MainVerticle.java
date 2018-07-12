package org.deman.fproxy;

import io.vertx.core.AbstractVerticle;
import org.deman.fproxy.config.Config;
import org.deman.fproxy.config.ConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);
    private static final String CONFIG_FILE_PROPERTY_KEY = "config.file";

    @Override
    public void start() throws Exception {
        Config configuration = ConfigLoader.load(System.getProperty(CONFIG_FILE_PROPERTY_KEY));
        new Proxy(vertx,configuration).start();
        LOGGER.info("Starting proxy on port: {}", configuration.getPort());
    }
}
