#! /bin/bash
DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

LOGBACK_PARAM=${LOGBACK:-classpath:logback.xml}
CONFIG_PARAM=${CONFIG:-$DIR/config.yml}
INSTANCES_PARAM=${INSTANCES:-2}

java -Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.SLF4JLogDelegateFactory \
     -Dlogback.configurationFile=$LOGBACK_PARAM \
     -Dconfig.file=$CONFIG_PARAM \
     -jar $DIR/target/fproxy-1.0.0-SNAPSHOT-fat.jar -instances $INSTANCES_PARAM

