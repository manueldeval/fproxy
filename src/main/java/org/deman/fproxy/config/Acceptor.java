package org.deman.fproxy.config;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.deman.fproxy.config.acceptors.HostRegexpAcceptor;
import org.deman.fproxy.http.Headers;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = HostRegexpAcceptor.class, name = "regexp")
})
public interface Acceptor {

    boolean accept(Headers prolog);

}
