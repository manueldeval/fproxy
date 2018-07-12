mvn vertx:hot

java -Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.impl.SLF4JLogDelegateFactory \
     -Dlogback.configurationFile=file:$DIR/src/main/resources/logback.xml \
     -jar ./target/fproxy-1.0.0-SNAPSHOT-fat.jar 
