package org.deman.fproxy.http;

import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import org.deman.fproxy.utils.FP;

public class HeadersBuilder {
    private static final String HEADER_SEP = ":";
    private static final String CRLF_REGEXP = "\\r?\\n";
    private static final String BLANK_REGEXP = "\\s+";

    public static Option<Headers> from(byte[] bytes) {
        List<String> headerLines = splitAsLines(bytes);

        Option<String> requestLineOpt = headerLines.headOption();
        Option<Tuple3<String,String,String>> methodRequestUriHttpVersionOpt = requestLineOpt.flatMap(HeadersBuilder::parseRequestLine);

        Map<String, List<String>> headers = headerLines
            .drop(1)
            .flatMap(HeadersBuilder::headerLineToTuple)
            .groupBy(Tuple2::_1)
            .mapValues(lt -> lt.map(Tuple2::_2));

        return methodRequestUriHttpVersionOpt.map(m  -> new Headers(m,headers));
    }

    public static Option<String> findHeader(Map<String, List<String>> headers,String header) {
        return headers.keySet().map(String::toUpperCase).filter(header::equals).headOption();
    }

    public static Option<Tuple3<String,String,String>> parseRequestLine(String requestLine) {
        List<String> splitted = List.of(requestLine.trim().split(BLANK_REGEXP));

        Option<String> methodOpt        = splitted.headOption();
        Option<String> requestUriOpt    = splitted.drop(1).headOption();
        Option<String> httpVersionOpt   = splitted.drop(2).headOption();

        return FP.mapAllOptions(methodOpt,requestUriOpt,httpVersionOpt,Tuple3::new);
    }

    public static List<String> splitAsLines(byte[] bytes) {
        String headerString = new String(bytes);
        String[] headerLines = headerString.split(CRLF_REGEXP);
        return List.of(headerLines);
    }

    public static Option<Tuple2<String, String>> headerLineToTuple(String line) {
        int index = line.indexOf(HEADER_SEP);
        return Option.when(index>0,() -> {
            String key   = line.substring(0, index).trim().toUpperCase(); //  RFC 7230 Headers are case insensitive
            String value = line.substring(index+1).trim();
            return new Tuple2<>(key, value);
        });
    }
}
