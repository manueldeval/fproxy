package org.deman.fproxy.config.rules;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.deman.fproxy.config.Acceptor;
import org.deman.fproxy.config.Rule;


public class UpstreamRule extends Rule {

    private final String host;
    private final Integer port;
    private final String user;
    private final String password;

    @JsonCreator
    public UpstreamRule(@JsonProperty("name") String name,
                        @JsonProperty("host") String host,
                        @JsonProperty("port") Integer port,
                        @JsonProperty("user") String user,
                        @JsonProperty("password") String password,
                        @JsonProperty("when") Acceptor acceptor) {
        super(name, acceptor);

        assert(host != null);
        assert(port != null);

        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
