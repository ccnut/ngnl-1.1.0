package com.ngnl.transport.test.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ngnl.transport.socket.SocketClient;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * @author 47
 *
 */
public class ChatClient {
	
	public static void main (String[] args) throws Exception{
		SocketClient client = new SocketClient();
		client.setChannelInitializer(new ChatClientInitilizer());
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					client.connect("localhost", 8400);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, "chatClient").start();
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		for (;;) {
			String msg = bufferedReader.readLine();
			client.channel.writeAndFlush(msg + "\r\n");
		}
	}
}

class ChatClientInitilizer extends ChannelInitializer<SocketChannel>{

	/**
	 * @see io.netty.channel.ChannelInitializer#initChannel(io.netty.channel.Channel)
	 */
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipline = ch.pipeline();
		
		pipline.addLast(new DelimiterBasedFrameDecoder(4096, Delimiters.lineDelimiter()))
			   .addLast(new StringDecoder(CharsetUtil.UTF_8))
			   .addLast(new StringEncoder(CharsetUtil.UTF_8))
			   .addLast(new ChatClientHandler());
		
	}
	
}

class ChatClientHandler extends SimpleChannelInboundHandler<String>{

	Logger logger = LoggerFactory.getLogger(ChatClientHandler.class);
	/**
	 * @see io.netty.channel.SimpleChannelInboundHandler#channelRead0(io.netty.channel.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		logger.info(msg);
	}
	
}
