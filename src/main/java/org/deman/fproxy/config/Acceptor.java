package org.deman.fproxy.config;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.deman.fproxy.config.acceptors.AndAcceptor;
import org.deman.fproxy.config.acceptors.HostRegexpAcceptor;
import org.deman.fproxy.config.acceptors.NoAcceptor;
import org.deman.fproxy.config.acceptors.YesAcceptor;
import org.deman.fproxy.http.Headers;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = HostRegexpAcceptor.class, name = "host_regexp"),
    @JsonSubTypes.Type(value = YesAcceptor.class, name = "always_true"),
    @JsonSubTypes.Type(value = NoAcceptor.class, name = "always_false"),
    @JsonSubTypes.Type(value = AndAcceptor.class, name = "and"),
    @JsonSubTypes.Type(value = AndAcceptor.class, name = "or")
})
public interface Acceptor {

    boolean accept(Headers prolog);

}
