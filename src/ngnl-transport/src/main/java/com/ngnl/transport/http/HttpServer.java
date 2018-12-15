package com.ngnl.transport.http;

import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author 47
 *
 */
public class HttpServer {
	
	int port = 9000;

	public void start () throws Exception{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workerGroup)
						   .channel(NioServerSocketChannel.class)
						   .childHandler(new HttpServerInitializer());
			
			ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
			channelFuture.addListener(future ->{
				if (future.isSuccess())
					LoggerFactory.getLogger(HttpServer.class).info("Netty http server start at port:{}", port);
				else
					LoggerFactory.getLogger(HttpServer.class).error("Netty http server start failed!");
			});
			channelFuture.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
