package org.deman.fproxy.config.acceptors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vavr.control.Option;
import org.deman.fproxy.config.Acceptor;
import org.deman.fproxy.http.Headers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OrAcceptor implements Acceptor {

    private List<Acceptor> acceptorList;

    @JsonCreator
    public OrAcceptor(@JsonProperty("expressions") List<Acceptor> acceptorList) {
        this.acceptorList = Option.of(acceptorList)
            .getOrElse(new ArrayList<>());
        if (this.acceptorList.size() == 0){
            this.acceptorList.add(new YesAcceptor());
        }
    }

    @Override
    public boolean accept(Headers prolog) {
        return acceptorList.stream()
            .map(a -> a.accept(prolog))
            .filter(b -> b)
            .findFirst()
            .orElse(false);
    }
}
