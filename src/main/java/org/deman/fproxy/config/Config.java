package org.deman.fproxy.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vavr.collection.List;
import io.vavr.control.Option;

public class Config {
    private int port;
    private List<Rule> rules;

    @JsonCreator
    public Config(@JsonProperty("port") int port,
                  @JsonProperty("rules") List<Rule> rules) {
        this.port = Option.of(port).getOrElse(8080);
        this.rules = Option.of(rules).getOrElse(List.empty());
    }

    public int getPort() {
        return port;
    }

    public List<Rule> getRules() {
        return List.ofAll(rules);
    }
}
