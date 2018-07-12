package org.deman.fproxy.config;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.vavr.control.Option;
import org.deman.fproxy.config.rules.DirectRule;
import org.deman.fproxy.config.rules.DiscardRule;
import org.deman.fproxy.config.rules.UpstreamRule;
import org.deman.fproxy.http.Headers;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = DirectRule.class, name = "direct"),
    @JsonSubTypes.Type(value = DiscardRule.class, name = "discard"),
    @JsonSubTypes.Type(value = UpstreamRule.class, name = "upstream")
})
public abstract class Rule {
    private String name;
    private Acceptor acceptor;

    public Rule(String name, Acceptor acceptor) {
        this.name = Option.of(name).getOrElse("Unknown");
        this.acceptor = Option.of(acceptor).getOrElse((h) -> false);
    }

    public String getName() {
        return name;
    }

    public boolean accept(Headers p) {
        return acceptor.accept(p);
    }

}
