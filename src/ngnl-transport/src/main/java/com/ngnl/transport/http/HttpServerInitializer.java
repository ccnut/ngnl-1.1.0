package com.ngnl.transport.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author 47
 *
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel>{

	/**
	 * @see io.netty.channel.ChannelInitializer#initChannel(io.netty.channel.Channel)
	 */
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		
		pipeline.addLast("httpServerCodec", new HttpServerCodec())
				.addLast("httpServerHandler", new HttpServerHandler());
	}

}
