package org.deman.fproxy

import io.vertx.core.buffer.Buffer
import org.deman.fproxy.utils.AesCipher
import org.deman.fproxy.utils.AesMode
import spock.lang.Specification

class CryptoSpec extends Specification{


    def "Message must be encoded and decoded"(String key, String message) {
        given:
        AesCipher encoder = new AesCipher(key,AesMode.ENCODE)
        AesCipher decoder = new AesCipher(key,AesMode.DECODE)


        when:
        Buffer encodeBuffer = encoder.next(Buffer.buffer(message.getBytes()))
        encodeBuffer.appendBuffer(encoder.end())

        Buffer decodedBuffer = decoder.next(encodeBuffer)
        decodedBuffer.appendBuffer(decoder.end())

        then:
        new String(decodedBuffer.getBytes()) == message

        where:
        key                 | message
        "145678"            | "Et licet quocumque oculos flexeris feminas adfatim"
        "14567812345678"    | "Et licet quocumque oculos flexeris feminas adfatim"
        "14567812345678123" | "Et licet quocumque oculos flexeris feminas adfatim"
        "14567812345678123" | ""
    }


    def "Message chunks must be encoded and decoded"(String result, List<String> messages) {
        given:
        AesCipher encoder = new AesCipher("key",AesMode.ENCODE)
        AesCipher decoder = new AesCipher("key",AesMode.DECODE)

        when:
        Buffer encodeBuffer = Buffer.buffer()

        messages.forEach{
            Buffer buffer = Buffer.buffer(it.getBytes())
            encodeBuffer.appendBuffer(encoder.next(buffer))
        }
        encodeBuffer.appendBuffer(encoder.end())

        Buffer decodedBuffer = decoder.next(encodeBuffer)
        decodedBuffer.appendBuffer(decoder.end())

        then:
        new String(decodedBuffer.getBytes()) == result

        where:
        result                                               | messages
        "Et licet quocumque oculos flexeris feminas adfatim" | ["Et licet quocumque oculos"," flexeris feminas adfatim"]
        "Et licet quocumque oculos flexeris feminas adfatim" | ["Et licet quocumque oculos flexeris feminas adfatim"]
        "Et licet quocumque oculos flexeris feminas adfatim" | ["Et licet"," quocumque oculos"," flexeris feminas ","adfatim"]
        "Et licet quocumque oculos flexeris feminas adfatim" | ["","Et licet"," quocumque oculos"," flexeris feminas ","adfatim"]

    }
}
