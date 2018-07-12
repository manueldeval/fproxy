#! /bin/bash

java -Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.SLF4JLogDelegateFactory \
     -Dlogback.configurationFile=classpath:logback.xml \
     -Dconfig.file=./src/main/resources/config.yml \
     -jar ./target/fproxy-1.0.0-SNAPSHOT-fat.jar


