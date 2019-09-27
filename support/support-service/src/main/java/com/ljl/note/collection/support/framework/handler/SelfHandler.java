package com.ljl.note.collection.support.framework.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.internal.TypeParameterMatcher;

public abstract class SelfHandler<I> extends ChannelInboundHandlerAdapter {
    private final TypeParameterMatcher matcher;
    private final boolean autoRelease;


    protected SelfHandler() {
        this(true);
    }


    protected SelfHandler(boolean autoRelease) {
        matcher = TypeParameterMatcher.find(this, SelfHandler.class, "I");
        this.autoRelease = autoRelease;
    }


    protected SelfHandler(Class<? extends I> inboundMessageType) {
        this(inboundMessageType, true);
    }


    protected SelfHandler(Class<? extends I> inboundMessageType, boolean autoRelease) {
        matcher = TypeParameterMatcher.get(inboundMessageType);
        this.autoRelease = autoRelease;
    }


    public boolean acceptInboundMessage(Object msg) throws Exception {
        return matcher.match(msg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        boolean release = true;
        try {
            if (acceptInboundMessage(msg)) {
                @SuppressWarnings("unchecked")
                I imsg = (I) msg;
                channelRead0(ctx, imsg);
            } else {
                release = false;
                ctx.fireChannelRead(msg);
            }
        } finally {
            if (autoRelease && release) {
                //ReferenceCountUtil.release(msg);
            }
        }
    }


    protected abstract void channelRead0(ChannelHandlerContext ctx, I msg) throws Exception;
}
