package com.ngnl.transport.socket;

import org.slf4j.LoggerFactory;

import com.ngnl.core.utils.Assert;
import com.ngnl.transport.http.HttpServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author 47
 *
 */
public class SocketServer {

	int port = 8400;
	
	ChannelInitializer<SocketChannel> channelInitializer = null;
	
	public void start (int port) throws Exception{
		this.port = port;
		
		Assert.notNull(channelInitializer, "serverChannelInitializer is null");

		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup)
					 .channel(NioServerSocketChannel.class)
					 .handler(new LoggingHandler()) //bind to boss group
					 .childHandler(channelInitializer); //bind to worker group
			
			ChannelFuture channelFuture = bootstrap.bind(8400).sync();
			channelFuture.addListener(future ->{
				if (future.isSuccess())
					LoggerFactory.getLogger(HttpServer.class).info("Netty socket server start at port:{}", port);
				else
					LoggerFactory.getLogger(HttpServer.class).error("Netty socket server start failed!");
			});
			channelFuture.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	
	public void setChannelInitializer(ChannelInitializer<SocketChannel> serverChannelInitializer) {
		this.channelInitializer = serverChannelInitializer;
	}
}
