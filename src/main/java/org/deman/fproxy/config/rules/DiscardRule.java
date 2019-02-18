package org.deman.fproxy.config.rules;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.deman.fproxy.config.Acceptor;
import org.deman.fproxy.config.Rule;

public class DiscardRule extends Rule {
    @JsonCreator
    public DiscardRule(@JsonProperty("name") String name,
                       @JsonProperty("when") Acceptor acceptor) {
        super(name, acceptor);
    }

}
