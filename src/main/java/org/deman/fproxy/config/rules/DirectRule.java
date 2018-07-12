package org.deman.fproxy.config.rules;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.deman.fproxy.config.Acceptor;
import org.deman.fproxy.config.Rule;

public class DirectRule extends Rule {

    @JsonCreator
    public DirectRule(@JsonProperty("name")  String name,
                      @JsonProperty("when") Acceptor acceptor) {
        super(name,acceptor);
    }

}
