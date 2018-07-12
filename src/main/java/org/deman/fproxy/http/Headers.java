package org.deman.fproxy.http;

import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.util.Objects;

public class Headers {

    private static final String CRLF = "\r\n";
    private static final String HOST_HEADER_KEY = "HOST";
    private static final String HOST_PORT_SEP_REGEX = ":";

    private Map<String, List<String>> headers;
    private String method;
    private String requestUri;
    private String httpVersion;

    public Headers(Tuple3<String, String, String> requestLine, Map<String, List<String>> headers) {
        this.headers = headers;
        this.method = requestLine._1;
        this.requestUri = requestLine._2;
        this.httpVersion = requestLine._3;
    }

    public Headers(Headers headers) {
        this.headers = headers.headers;
        this.method = headers.method;
        this.requestUri = headers.requestUri;
        this.httpVersion = headers.httpVersion;
    }

    public String getMethod() {
        return method;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public Tuple2<String, Integer> getHostPort() {
        String hostStr = this.getHeader(HOST_HEADER_KEY)
            .flatMap(s -> Option.when(!s.isEmpty(),s))
            .getOrElse(this::getRequestUri);
        return extractHostPortFromString(hostStr);
    }

    public String getHost(){
        return getHostPort()._1;
    }


    public Integer getPort(){
        return getHostPort()._2;
    }

    private Tuple2<String, Integer> extractHostPortFromString(String hostStr){
        String[] hostArray = hostStr.split(HOST_PORT_SEP_REGEX);
        String host = hostArray[0];
        Integer port = Option.when(hostArray.length > 1, () -> hostArray[1])
            .orElse(Option.of("80"))
            .flatMap(portStr -> Try.of(() -> Integer.valueOf(portStr)).toOption())
            .getOrElse(80);
        return new Tuple2<>(host,port);
    }

    public Option<String> getHeader(String key) {
        String name = normalizeHeaderKey(key);
        return headers.get(name).flatMap(List::headOption);
    }

    public List<String> getHeaders(String key) {
        String name = normalizeHeaderKey(key);
        return headers.get(name).getOrElse(List.empty());
    }

    public void addToHeader(String key, String value) {
        String name = normalizeHeaderKey(key);
        List<String> newList = headers.getOrElse(name, List.empty()).append(value);
        headers.put(name, newList);
    }

    public void setHeader(String key, String value) {
        String name = normalizeHeaderKey(key);
        headers.put(name, List.of(value));
    }

    public void setHeader(String key, List<String> value) {
        String name = normalizeHeaderKey(key);
        headers.put(name, value);
    }

    public void removeHeader(String key) {
        String name = normalizeHeaderKey(key);
        headers.remove(name);
    }

    private String normalizeHeaderKey(String s) {
        return s.trim().toUpperCase();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getMethod()).append(" ").append(this.getRequestUri()).append(" ").append(this.httpVersion);
        headers.flatMap((entry) -> entry._2.map(value -> new Tuple2<>(entry._1, value)))
            .forEach(entry -> {
                builder.append(CRLF).append(entry._1).append(": ").append(entry._2);
            });
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Headers prolog = (Headers) o;
        return Objects.equals(headers, prolog.headers) &&
            Objects.equals(method, prolog.method) &&
            Objects.equals(requestUri, prolog.requestUri) &&
            Objects.equals(httpVersion, prolog.httpVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(headers, method, requestUri, httpVersion);
    }
}
