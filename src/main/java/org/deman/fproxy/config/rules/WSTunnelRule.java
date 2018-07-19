package org.deman.fproxy.config.rules;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.deman.fproxy.config.Acceptor;
import org.deman.fproxy.config.Rule;

public class WSTunnelRule extends Rule {

    private String host;
    private Integer port;
    private String proxyHost;
    private Integer proxyPort;
    private String proxyUser;
    private String proxyPassword;
    @JsonCreator
    public WSTunnelRule(@JsonProperty("name") String name,
                        @JsonProperty("when") Acceptor acceptor,
                        @JsonProperty("host") String host,
                        @JsonProperty("port") Integer port,
                        @JsonProperty("proxy_host") String proxyHost,
                        @JsonProperty("proxy_port") Integer proxyPort,
                        @JsonProperty("proxy_user") String proxyUser,
                        @JsonProperty("proxy_password") String proxyPassword) {
        super(name,acceptor);
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.proxyUser = proxyUser;
        this.proxyPassword = proxyPassword;
        this.host = host;
        this.port = port;

    }


    public Integer getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public String getProxyUser() {
        return proxyUser;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }
}
