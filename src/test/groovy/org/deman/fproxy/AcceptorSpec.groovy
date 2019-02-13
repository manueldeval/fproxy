package org.deman.fproxy

import org.deman.fproxy.config.Acceptor
import org.deman.fproxy.config.acceptors.AndAcceptor
import org.deman.fproxy.config.acceptors.NoAcceptor
import org.deman.fproxy.config.acceptors.NotAcceptor
import org.deman.fproxy.config.acceptors.OrAcceptor
import org.deman.fproxy.config.acceptors.YesAcceptor
import spock.lang.Specification

class AcceptorSpec extends Specification {

    def "And"(boolean [] inputs,boolean result){
        given:
        List<Acceptor> inputsAcceptors = inputs?.collect{ it?new YesAcceptor():new NoAcceptor() }
        def acceptor = new AndAcceptor(inputsAcceptors)

        expect:
        result == acceptor.accept(null)

        where:
        inputs              | result
        [false,false,false] | false
        [true,false,false]  | false
        [false,true,false]  | false
        [true,true,false]   | false
        [false,false,true]  | false
        [true,false,true]   | false
        [false,true,true]   | false
        [true,true,true]    | true
        [false]             | false
        [true]              | true
        []                  | true
        null                | true
    }

    def "Or"(boolean [] inputs,boolean result){
        given:
        List<Acceptor> inputsAcceptors = inputs?.collect{ it?new YesAcceptor():new NoAcceptor() }
        def acceptor = new OrAcceptor(inputsAcceptors)

        expect:
        result == acceptor.accept(null)

        where:
        inputs              | result
        [false,false,false] | false
        [true,false,false]  | true
        [false,true,false]  | true
        [true,true,false]   | true
        [false,false,true]  | true
        [true,false,true]   | true
        [false,true,true]   | true
        [true,true,true]    | true
        [false]             | false
        [true]              | true
        []                  | true
        null                | true
    }

    def "Not"(Boolean input ,boolean result) {
        given:
        def wrapped =  input?.booleanValue() ? new YesAcceptor() : new NoAcceptor()
        def acceptor = new NotAcceptor(wrapped)

        expect:
        result == acceptor.accept(null)

        where:
        input           | result
        true            | false
        false           | true
        null            | true
    }
}
