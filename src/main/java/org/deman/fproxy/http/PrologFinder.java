package org.deman.fproxy.http;

import io.vavr.collection.Stream;
import io.vavr.control.Option;
import io.vertx.core.buffer.Buffer;
import org.deman.fproxy.utils.FP;

import java.util.function.Function;


public class PrologFinder {

    private Buffer buffer = Buffer.buffer();
    private Option<Headers> prologOpt = Option.none();

    public Option<Prolog> onBuffer(Buffer newBuffer) {
        buffer.appendBuffer(newBuffer);
        Option<Integer> indexOpt = findEndOfHeader(buffer.getBytes());
        Option<Headers> prologOpt = indexOpt.flatMap(index -> HeadersBuilder.from(buffer.getBytes(0, index)));
        Option<Buffer> tailBuffer = indexOpt.map(index -> buffer.getBuffer(index, buffer.length()));
        return FP.mapAllOptions(prologOpt, tailBuffer, Prolog::new);
    }

    /**
     * Check if an array contains another array, return an optional containing the position
     * of the match.
     *
     * @param outerArray
     * @param smallerArray
     * @return
     */
    private static Option<Integer> indexOf(byte[] outerArray, byte[] smallerArray) {
        for (int i = 0; i < outerArray.length - smallerArray.length + 1; ++i) {
            boolean found = true;
            for (int j = 0; j < smallerArray.length; ++j) {
                if (outerArray[i + j] != smallerArray[j]) {
                    found = false;
                    break;
                }
            }
            if (found) return Option.of(i);
        }
        return Option.none();
    }

    /**
     * Search the end of the http header in a byte array. Return an optional containing the position of the header.
     *
     * @param bytes
     * @return
     */
    private static final String[] END_OF_HEADER_STR = {"\r\n\r\n", "\n\n"};

    private static Option<Integer> findEndOfHeader(byte[] bytes) {
        return Stream.of(END_OF_HEADER_STR)
            .map(String::getBytes)
            .map(endHeaderBytes -> indexOf(bytes, endHeaderBytes))
            .flatMap(Function.identity())
            .headOption();
    }
}
