package org.deman.fproxy.config.acceptors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.deman.fproxy.config.Acceptor;
import org.deman.fproxy.http.Headers;

public class HostRegexpAcceptor implements Acceptor {

    private String regexp;

    @JsonCreator
    public HostRegexpAcceptor(@JsonProperty("pattern") String regexp) {
        this.regexp = regexp;
    }

    @Override
    public boolean accept(Headers prolog) {
        return prolog.getHost().matches(regexp);
    }
}
