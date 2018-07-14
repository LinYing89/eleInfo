package com.bairock.iot.eleInfo.communication;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class MyClient {

	private static MyClient ins = new MyClient();
	public boolean linking = false;

	private String nextIp = "192.168.43.238";
//	private String nextIp = "218.92.24.10";
	private int nextPort = 9090;
    private Bootstrap b;

    private MyClientHandler myClientHandler;
    
    private MyClient(){
        init();
    }

    public static MyClient getIns(){
        return ins;
    }

    private void init(){
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        b = new Bootstrap(); // (1)
        b.group(workerGroup); // (2)
        b.channel(NioSocketChannel.class); // (3)
        b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new IdleStateHandler(-1, 20,-1, TimeUnit.SECONDS)); // 1
                ch.pipeline().addLast(new MyClientHandler());
            }
        });
    }

    public void link(){
    	if(linking) {
    		return;
    	}
        try {
            // Start the client.
            ChannelFuture channelFuture = b.connect(nextIp, nextPort); // (5)
            // Wait until the connection is closed.
            channelFuture.channel().closeFuture();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    void setMyClientHandler(MyClientHandler myClientHandler){
        if(this.myClientHandler != null){
            this.myClientHandler.channel.close();
            this.myClientHandler = null;
        }
        this.myClientHandler = myClientHandler;
    }
    
    public void closeHandler(){
        if(null != myClientHandler) {
        	myClientHandler.channel.close();
        	myClientHandler = null;
        }
    }

    public void send(byte[] msg){
        if(null != myClientHandler){
        	myClientHandler.send(msg);
        }
    }
}
