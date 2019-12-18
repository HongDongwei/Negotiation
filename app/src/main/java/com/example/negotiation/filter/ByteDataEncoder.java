package com.example.negotiation.filter;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineEncoder;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 *  编码器将数据直接发出去(不做处理)
 */
public class ByteDataEncoder extends ProtocolEncoderAdapter {

    private static final AttributeKey ENCODER = new AttributeKey(TextLineEncoder.class, "encoder");
    private final Charset charset;
    private final LineDelimiter delimiter;
    private int maxLineLength;

    public ByteDataEncoder() {
        this(Charset.defaultCharset(), LineDelimiter.UNIX);
    }

    public ByteDataEncoder(String delimiter) {
        this(new LineDelimiter(delimiter));
    }

    public ByteDataEncoder(LineDelimiter delimiter) {
        this(Charset.defaultCharset(), delimiter);
    }

    public ByteDataEncoder(Charset charset) {
        this(charset, LineDelimiter.UNIX);
    }

    public ByteDataEncoder(Charset charset, String delimiter) {
        this(charset, new LineDelimiter(delimiter));
    }

    public ByteDataEncoder(Charset charset, LineDelimiter delimiter) {
        this.maxLineLength = 2147483647;
        if (charset == null) {
            throw new IllegalArgumentException("charset");
        } else if (delimiter == null) {
            throw new IllegalArgumentException("delimiter");
        } else if (LineDelimiter.AUTO.equals(delimiter)) {
            throw new IllegalArgumentException("AUTO delimiter is not allowed for encoder.");
        } else {
            this.charset = charset;
            this.delimiter = delimiter;
        }
    }

    public int getMaxLineLength() {
        return this.maxLineLength;
    }

    public void setMaxLineLength(int maxLineLength) {
        if (maxLineLength <= 0) {
            throw new IllegalArgumentException("maxLineLength: " + maxLineLength);
        } else {
            this.maxLineLength = maxLineLength;
        }
    }

    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        CharsetEncoder encoder = (CharsetEncoder)session.getAttribute(ENCODER);
        if (encoder == null) {
            encoder = this.charset.newEncoder();
            session.setAttribute(ENCODER, encoder);
        }

        String value = message == null ? "" : message.toString();
        IoBuffer buf = IoBuffer.allocate(value.length()).setAutoExpand(true);
        buf.putString(value, encoder);
        if (buf.position() > this.maxLineLength) {
            throw new IllegalArgumentException("Line length: " + buf.position());
        } else {
            buf.putString(this.delimiter.getValue(), encoder);
            buf.flip();
            out.write(buf);
        }
    }

    public void dispose() throws Exception {
    }
}
