package com.example.negotiation.filter;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class ByteCodecFactory implements ProtocolCodecFactory {
    private ByteDataDecoder decoder;
    private ByteDataEncoder encoder;
    public ByteCodecFactory() {
        encoder = new ByteDataEncoder();
        decoder = new ByteDataDecoder();
    }
    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return decoder;
    }
    @Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return encoder;
    }
}
