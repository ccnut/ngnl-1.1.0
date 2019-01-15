package com.ngnl.transport.test.chat;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ngnl.transport.socket.SocketServer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author 47
 *
 */
public class ChatServer {

	public static void main (String[] args) throws Exception{
		SocketServer socketServer = new SocketServer();
		socketServer.setChannelInitializer(new ChatServerInitializer());
		
		socketServer.start(8400);
	}
}

class ChatServerInitializer extends ChannelInitializer<SocketChannel>{

	/**
	 * @see io.netty.channel.ChannelInitializer#initChannel(io.netty.channel.Channel)
	 */
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipline = ch.pipeline();
		
		pipline.addLast(new DelimiterBasedFrameDecoder(4096, Delimiters.lineDelimiter()))
			   .addLast(new StringDecoder(CharsetUtil.UTF_8))
			   .addLast(new StringEncoder(CharsetUtil.UTF_8))
			   .addLast(new ChatServerHandler());
	}

}

class ChatServerHandler extends SimpleChannelInboundHandler<String>{

	static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE); 
	
	Logger logger = LoggerFactory.getLogger(getClass());
	/**
	 * @see io.netty.channel.SimpleChannelInboundHandler#channelRead0(io.netty.channel.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		Channel channel = ctx.channel();
		
		channelGroup.forEach(c -> {
			if (channel != c) {
				c.writeAndFlush(channel.remoteAddress() + " say: " + msg + "\n");
			}else {
				c.writeAndFlush("you say: " + msg + "\n");
			}
		});
	}
	
	/**
	 * @see io.netty.channel.ChannelHandlerAdapter#handlerAdded(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		super.handlerAdded(ctx);
		Channel channel = ctx.channel();
		channelGroup.writeAndFlush("[Server]" + channel.remoteAddress() + " join in.\n");
		channelGroup.add(channel);
	}
	
	/**
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelActive(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		logger.info(ctx.channel().remoteAddress() + " on line.");
	}

	/**
	 * @see io.netty.channel.ChannelHandlerAdapter#handlerRemoved(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		super.handlerRemoved(ctx);
		Channel channel = ctx.channel();
		channelGroup.writeAndFlush("[Server]" + channel.remoteAddress() + " leaved.\n");
		channelGroup.remove(channel);
	}
	
	/**
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelInactive(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		logger.info(ctx.channel().remoteAddress() + "off line.");
	}
	/**
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelUnregistered(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		super.channelUnregistered(ctx);
	}
	
	/**
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#exceptionCaught(io.netty.channel.ChannelHandlerContext, java.lang.Throwable)
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
		cause.printStackTrace();
		ctx.close();
	}
}