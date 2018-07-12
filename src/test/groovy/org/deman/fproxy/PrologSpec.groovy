package org.deman.fproxy

import io.vavr.control.Option
import io.vavr.Tuple2
import io.vavr.Tuple3
import io.vavr.collection.List
import io.vertx.core.buffer.Buffer
import org.deman.fproxy.http.Headers
import org.deman.fproxy.http.HeadersBuilder
import org.deman.fproxy.http.Prolog
import org.deman.fproxy.http.PrologFinder
import spock.lang.Specification

class PrologSpec extends Specification{

    def "headerLineToTuple should split the header line"(String line, Option<Tuple2<String,String>> tuple) {
        expect:
            HeadersBuilder.headerLineToTuple(line) == tuple

        where:
            line         | tuple
            "a:b"        | Option.of(new Tuple2<>("A","b"))
            " a : b "    | Option.of(new Tuple2<>("A","b"))
            "a:"         | Option.of(new Tuple2<>("A",""))
            "a:b: c "    | Option.of(new Tuple2<>("A","b: c"))
            " "          | Option.none()
            ":"          | Option.none()
    }

    def "splitAsLine should split the byte header"(String line,List<String> lines){
        expect:
            HeadersBuilder.splitAsLines(line.getBytes()) == lines

        where:
            line         | lines
            "a"          | List.of("a")
            "a\n"        | List.of("a")
            "a\r\n"      | List.of("a")
            "a\r\nb"     | List.of("a","b")
            "a\nb"       | List.of("a","b")
    }

    def "parseRequestLine should read the first header line"(String line,Option<Tuple3<String,String,String>> tuple){
        expect:
            HeadersBuilder.parseRequestLine(line) == tuple

        where:
            line                        | tuple
            "a b c"                     | Option.of(new Tuple3<>("a","b","c"))
            "CONNECT   /toto HTTP/1.1"  | Option.of(new Tuple3<>("CONNECT","/toto","HTTP/1.1"))
            "CONNECT   /toto"           | Option.none()
            "CONNECT"                   | Option.none()
            ""                          | Option.none()
    }

    def "Builder should create a prolog which generate a valid header."(){
        given:
            String httpHeader = "CONNECT  localhost HTTP/1.1\nkey1: 1\r\nkey2: 2 \r\n\r\n"
        when:
            String generatedHeader = HeadersBuilder.from(httpHeader.getBytes()).map({ p -> p.toString()}).getOrElse("")
        then:
            generatedHeader == "CONNECT localhost HTTP/1.1\r\nKEY1: 1\r\nKEY2: 2"
    }

    def "Prolog should return correct header values."(String key,Option<String> v1,List<String> v2){
        given:
            String httpHeader = "CONNECT  localhost HTTP/1.1\nkey1: 1\r\nkey2: 2\r\nKey1: 5 "
            Headers prolog = HeadersBuilder.from(httpHeader.getBytes()).get()
        expect:
            prolog.getHeader(key) == v1
            prolog.getHeaders(key) == v2
        where:
            key     | v1                | v2
            "key1"  | Option.of("1")    | List.of("1","5")
            "key2"  | Option.of("2")    | List.of("2")
            "KEY2"  | Option.of("2")    | List.of("2")
            "keyN"  | Option.none()     | List.empty()
    }

    def "Prolog should return correct host values."(String header,Tuple2<String,Integer> hostPort){
        given:
            Headers prolog = HeadersBuilder.from(header.getBytes()).get()
        expect:
            prolog.getHostPort() == hostPort
        where:
            header                                          | hostPort
            "GET index.html HTTP/1.1\nHost: a:2\r\n\r\n"    | new Tuple2("a",2)
            "GET index.html HTTP/1.1\nHost: a:2"            | new Tuple2("a",2)
            "GET index.html HTTP/1.1\nHost: a\r\n\r\n"      | new Tuple2("a",80)
            "GET index.html HTTP/1.1\nHost: a:z\r\n\r\n"    | new Tuple2("a",80)
            "CONNECT srv:443 HTTP/1.1\r\n\r\n"              | new Tuple2("srv",443)
    }

    def "PrologFinder must return the correct tail."(List<String> chunks,Option<String> tail){
        given:
            List<Buffer> buffers = chunks.map {Buffer.buffer(it)}
            PrologFinder prologFinder = new PrologFinder()
        when:
            Option<Prolog> result
            buffers.forEach { result = prologFinder.onBuffer(it) }
            Option<String> tailReturned = result.map { it.getBodyBuffer().toString() }
        then:
            tailReturned == tail
        where:
            chunks                                                          | tail
            List.of("GET index.html HTTP/1.1\nHost: a\r\n\r\nBody")         | Option.of("\r\n\r\nBody")
            List.of("GET index.html HTTP/1.1\nHost: a\r","\n\r\nBody")      | Option.of("\r\n\r\nBody")
            List.of("GET index.html HTTP/1.1\nHost: a\n\n")                 | Option.of("\n\n")
            List.of("GET index.html HTTP/1.1\nHost: a\nBody")               | Option.none()
    }
}
