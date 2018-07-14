package com.bairock.iot.eleInfo.communication;

import java.util.Arrays;

import org.apache.log4j.Logger;

import com.bairock.iot.eleInfo.MsgManager;
import com.bairock.iot.eleInfo.listener.StartUpListener;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {

	public static ChannelHandlerContext channel;

	private Logger logger = Logger.getLogger(this.getClass().getName()); 
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		if (null != channel) {
			channel.close();
			channel = null;
		}
		channel = ctx;
		logger.info("new channel " + ctx.channel().id().asShortText());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf m = (ByteBuf) msg;
		try {
			byte[] req = new byte[m.readableBytes()];
			m.readBytes(req);
			MyClient.getIns().send(req);
			logger.info(bytesToHexString(req));
			int len = (req[0] << 8) | req[1];
			// 数据有效长度不包括长度字节数和通信管理机字节数,长度字节数=2,通信管理机字节数=4
			if (req.length != len + 8) {
				logger.error("长度不匹配");
				return;
			}
			int managerNum = (req[2] << 24) | (req[3] << 16) | (req[4] << 8) | req[5];
			MsgManager mm = StartUpListener.findMsgManager(managerNum);

			if(mm == null) {
				logger.error("通信管理机不存在-num:" + managerNum);
				return;
			}
			byte[] by = new byte[len];
			by = Arrays.copyOfRange(req, 6, req.length);
			mm.handler(by);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			m.release();
			// ReferenceCountUtil.release(msg);
		}
	}
	
	@Override 
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
		logger.info("channel " + ctx.channel().id().asShortText() + " is closed");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		ctx.close();
		logger.info("channel " + ctx.channel().id().asShortText() + " is closed");
	}

	public static void send(byte[] by) {
		if(null != channel) {
			channel.writeAndFlush(Unpooled.copiedBuffer(by));
		}
	}
	
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
			stringBuilder.append(" ");
		}
		return stringBuilder.toString();
	}

	public static void main(String[] args) {
		byte[] b = new byte[] { 0x00, 0, 1, 0 };
		int managerNum = (b[0] << 24) | (b[1] << 16) | (b[2] << 8) | b[3];
		System.out.println(managerNum + "?");
	}

}
