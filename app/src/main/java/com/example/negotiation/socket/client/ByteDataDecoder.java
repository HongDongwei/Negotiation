package com.example.negotiation.socket.client;


import com.example.negotiation.utils.HexUtils;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderException;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.RecoverableProtocolDecoderException;
import org.apache.mina.filter.codec.textline.LineDelimiter;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class ByteDataDecoder implements ProtocolDecoder {
    private final AttributeKey CONTEXT;
    private final Charset charset;
    private final LineDelimiter delimiter;
    private IoBuffer delimBuf;
    private int maxLineLength;
    private int bufferLength;

    public ByteDataDecoder() {
        this(LineDelimiter.AUTO);
    }

    public ByteDataDecoder(String delimiter) {
        this(new LineDelimiter(delimiter));
    }

    public ByteDataDecoder(LineDelimiter delimiter) {
        this(Charset.defaultCharset(), delimiter);
    }

    public ByteDataDecoder(Charset charset) {
        this(charset, LineDelimiter.AUTO);
    }

    public ByteDataDecoder(Charset charset, String delimiter) {
        this(charset, new LineDelimiter(delimiter));
    }

    public ByteDataDecoder(Charset charset, LineDelimiter delimiter) {
        this.CONTEXT = new AttributeKey(this.getClass(), "context");
        this.maxLineLength = 1024;
        this.bufferLength = 128;
        if (charset == null) {
            throw new IllegalArgumentException("charset parameter shuld not be null");
        } else if (delimiter == null) {
            throw new IllegalArgumentException("delimiter parameter should not be null");
        } else {
            this.charset = charset;
            this.delimiter = delimiter;
            if (this.delimBuf == null) {
                IoBuffer tmp = IoBuffer.allocate(2).setAutoExpand(true);

                try {
                    tmp.putString(delimiter.getValue(), charset.newEncoder());
                } catch (CharacterCodingException var5) {
                    ;
                }

                tmp.flip();
                this.delimBuf = tmp;
            }

        }
    }

    public int getMaxLineLength() {
        return this.maxLineLength;
    }

    public void setMaxLineLength(int maxLineLength) {
        if (maxLineLength <= 0) {
            throw new IllegalArgumentException("maxLineLength (" + maxLineLength + ") should be a positive value");
        } else {
            this.maxLineLength = maxLineLength;
        }
    }

    public void setBufferLength(int bufferLength) {
        if (bufferLength <= 0) {
            throw new IllegalArgumentException("bufferLength (" + this.maxLineLength + ") should be a positive value");
        } else {
            this.bufferLength = bufferLength;
        }
    }

    public int getBufferLength() {
        return this.bufferLength;
    }

    public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {

        ByteDataDecoder.Context ctx = this.getContext(session);

        byte[] bytes = {0x0D, 0x0A};
        in.setAutoExpand(true);//设置自动扩展
        in.position(in.limit());
        in.put(bytes);
        in.position(0);

        if (LineDelimiter.AUTO.equals(this.delimiter)) {
            this.decodeAuto(ctx, session, in, out);
        } else {
            this.decodeNormal(ctx, session, in, out);
        }

    }

    private ByteDataDecoder.Context getContext(IoSession session) {
        ByteDataDecoder.Context ctx = (ByteDataDecoder.Context) session.getAttribute(this.CONTEXT);
        if (ctx == null) {
            ctx = new ByteDataDecoder.Context(this.bufferLength);
            session.setAttribute(this.CONTEXT, ctx);
        }

        return ctx;
    }

    public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {
    }

    public void dispose(IoSession session) throws Exception {
        ByteDataDecoder.Context ctx = (ByteDataDecoder.Context) session.getAttribute(this.CONTEXT);
        if (ctx != null) {
            session.removeAttribute(this.CONTEXT);
        }

    }

    private void decodeAuto(ByteDataDecoder.Context ctx, IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws CharacterCodingException, ProtocolDecoderException {
        int matchCount = ctx.getMatchCount();
        int oldPos = in.position();
        int oldLimit = in.limit();
        int cout = 0;
        while (in.hasRemaining()) {
            byte b = in.get();
            boolean matched = false;
            switch (b) {
                case 10:
                    if (1 == cout) {
                        ++matchCount;
                        matched = true;
                        break;
                    } else {
                        matchCount = 0;
                    }
                case 13:
                    ++matchCount;
                    cout = matchCount;
                default:
                    matchCount = 0;
            }
            if (matched) {
                int pos = in.position();
                in.limit(pos);
                in.position(oldPos);
                ctx.append(in);
                in.limit(oldLimit);
                in.position(pos);
                if (ctx.getOverflowPosition() != 0) {
                    int overflowPosition = ctx.getOverflowPosition();
                    ctx.reset();
                    throw new RecoverableProtocolDecoderException("Line is too long: " + overflowPosition);
                }

                IoBuffer buf = ctx.getBuffer();
                buf.flip();
                buf.limit(buf.limit() - matchCount);

                try {
                    byte[] data = new byte[buf.limit()];
                    buf.get(data);
//                    CharsetDecoder decoder = ctx.getDecoder();
//                    CharBuffer buffer = decoder.decode(ByteBuffer.wrap(data));
                    String str = HexUtils.bytesToHexString(data);
                    this.writeText(session, str, out);
                } finally {
                    buf.clear();
                }

                oldPos = pos;
                matchCount = 0;
            }
        }

        in.position(oldPos);
        ctx.append(in);
        ctx.setMatchCount(matchCount);
    }

    private void decodeNormal(ByteDataDecoder.Context ctx, IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws CharacterCodingException, ProtocolDecoderException {
        int matchCount = ctx.getMatchCount();
        int oldPos = in.position();
        int oldLimit = in.limit();

        while (in.hasRemaining()) {
            byte b = in.get();
            if (this.delimBuf.get(matchCount) == b) {
                ++matchCount;
                if (matchCount == this.delimBuf.limit()) {
                    int pos = in.position();
                    in.limit(pos);
                    in.position(oldPos);
                    ctx.append(in);
                    in.limit(oldLimit);
                    in.position(pos);
                    if (ctx.getOverflowPosition() != 0) {
                        int overflowPosition = ctx.getOverflowPosition();
                        ctx.reset();
                        throw new RecoverableProtocolDecoderException("Line is too long: " + overflowPosition);
                    }

                    IoBuffer buf = ctx.getBuffer();
                    buf.flip();
                    buf.limit(buf.limit() - matchCount);

                    try {
                        this.writeText(session, buf.getString(ctx.getDecoder()), out);
                    } finally {
                        buf.clear();
                    }

                    oldPos = pos;
                    matchCount = 0;
                }
            } else {
                in.position(Math.max(0, in.position() - matchCount));
                matchCount = 0;
            }
        }

        in.position(oldPos);
        ctx.append(in);
        ctx.setMatchCount(matchCount);
    }

    protected void writeText(IoSession session, String text, ProtocolDecoderOutput out) {
        out.write(text);
    }

    private class Context {
        private final CharsetDecoder decoder;
        private final IoBuffer buf;
        private int matchCount;
        private int overflowPosition;

        private Context(int bufferLength) {
            this.matchCount = 0;
            this.overflowPosition = 0;
            this.decoder = ByteDataDecoder.this.charset.newDecoder();
            this.buf = IoBuffer.allocate(bufferLength).setAutoExpand(true);
        }

        public CharsetDecoder getDecoder() {
            return this.decoder;
        }

        public IoBuffer getBuffer() {
            return this.buf;
        }

        public int getOverflowPosition() {
            return this.overflowPosition;
        }

        public int getMatchCount() {
            return this.matchCount;
        }

        public void setMatchCount(int matchCount) {
            this.matchCount = matchCount;
        }

        public void reset() {
            this.overflowPosition = 0;
            this.matchCount = 0;
            this.decoder.reset();
        }

        public void append(IoBuffer in) {
            if (this.overflowPosition != 0) {
                this.discard(in);
            } else if (this.buf.position() > ByteDataDecoder.this.maxLineLength - in.remaining()) {
                this.overflowPosition = this.buf.position();
                this.buf.clear();
                this.discard(in);
            } else {
                this.getBuffer().put(in);
            }

        }

        private void discard(IoBuffer in) {
            if (2147483647 - in.remaining() < this.overflowPosition) {
                this.overflowPosition = 2147483647;
            } else {
                this.overflowPosition += in.remaining();
            }

            in.position(in.limit());
        }
    }
}
