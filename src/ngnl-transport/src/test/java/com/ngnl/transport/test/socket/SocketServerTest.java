package com.ngnl.transport.test.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ngnl.transport.socket.SocketServer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * @author 47
 *
 */
public class SocketServerTest {

	public static void main (String[] args) throws Exception{
		SocketServer socketServer = new SocketServer();
		socketServer.setChannelInitializer(new SocketServerInitializer());
		socketServer.start(8400);
	}
	
}

class SocketServerInitializer extends ChannelInitializer<SocketChannel>{

	/**
	 * @see io.netty.channel.ChannelInitializer#initChannel(io.netty.channel.Channel)
	 */
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipline = ch.pipeline();
		
		pipline.addLast("LengthFieldBasedFrameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
			   .addLast("LengthFieldPrepender", new LengthFieldPrepender(4))
			   .addLast("StringDecoder", new StringDecoder(CharsetUtil.UTF_8))
			   .addLast("StringEncoder", new StringEncoder(CharsetUtil.UTF_8))
			   .addLast("socketServerHandler", new SocketServerHandler());
	}

}

class SocketServerHandler extends SimpleChannelInboundHandler<String>{
	
	Logger logger = LoggerFactory.getLogger(SocketServerHandler.class);

	/**
	 * @see io.netty.channel.SimpleChannelInboundHandler#channelRead0(io.netty.channel.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		Channel channel = ctx.channel();
		logger.info("remote address:{}, message :{}", channel.remoteAddress(), msg);
		
		channel.writeAndFlush(msg + " (reponse)");
	}
	
	/**
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#exceptionCaught(io.netty.channel.ChannelHandlerContext, java.lang.Throwable)
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
		logger.info(cause.getMessage());
		ctx.close();
	}

}