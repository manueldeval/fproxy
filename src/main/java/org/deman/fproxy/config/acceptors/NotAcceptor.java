package org.deman.fproxy.config.acceptors;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.vavr.control.Option;
import org.deman.fproxy.config.Acceptor;
import org.deman.fproxy.http.Headers;

import java.util.List;

public class NotAcceptor implements Acceptor {

    private Acceptor acceptor;

    public NotAcceptor(@JsonProperty("expression") Acceptor acceptor){
        this.acceptor = Option.of(acceptor).getOrElse(NoAcceptor::new);
    }

    @Override
    public boolean accept(Headers prolog) {
        return !this.acceptor.accept(prolog);
    }
}
