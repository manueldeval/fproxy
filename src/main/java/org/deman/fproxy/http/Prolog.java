package org.deman.fproxy.http;

import io.vertx.core.buffer.Buffer;

public class Prolog extends Headers {

    private final Buffer tail;

    public Prolog(Headers headers, Buffer tail) {
        super(headers);
        this.tail = tail;
    }

    public Buffer getBodyBuffer() {
        return tail;
    }

    public Buffer getHeaderBuffer() {
        return Buffer.buffer(super.toString());
    }

    public Buffer getBuffer() {
        return getHeaderBuffer().appendBuffer(getBodyBuffer());
    }
}
