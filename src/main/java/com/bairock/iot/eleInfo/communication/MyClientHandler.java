package com.bairock.iot.eleInfo.communication;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

public class MyClientHandler extends ChannelInboundHandlerAdapter {
	
    public Channel channel;
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
    	MyClient.getIns().setMyClientHandler(this);
        channel = ctx.channel();
        MyClient.getIns().linking = false;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf m = (ByteBuf)msg;
        try {
            byte[] req = new byte[m.readableBytes()];
            m.readBytes(req);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        ctx.close();
        channel = null;
        MyClient.getIns().linking = false;
        MyClient.getIns().setMyClientHandler(null);
        MyClient.getIns().link();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof IdleStateEvent) {  // 2
            ctx.close();
            MyClient.getIns().linking = false;
        }
    }

    public void send(byte[] by){
        try {
            if(null != channel) {
                channel.writeAndFlush(Unpooled.copiedBuffer(by));
            }else {
            	 MyClient.getIns().link();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
