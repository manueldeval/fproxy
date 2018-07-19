package org.deman.fproxy.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vavr.collection.List;
import io.vavr.control.Option;

public class Config {
    private Integer port;
    private List<Rule> rules;

    private Integer wsTunnelPort;

    @JsonCreator
    public Config(@JsonProperty("port") Integer port,
                  @JsonProperty("wsTunnelPort") Integer wsTunnelPort,
                  @JsonProperty("rules") List<Rule> rules) {
        this.port = port;
        this.wsTunnelPort = wsTunnelPort;
        this.rules = Option.of(rules).getOrElse(List.empty());
    }


    public Integer getPort() {
        return port;
    }

    public Integer getWsTunnelPort() {
        return wsTunnelPort;
    }

    public List<Rule> getRules() {
        return List.ofAll(rules);
    }
}
