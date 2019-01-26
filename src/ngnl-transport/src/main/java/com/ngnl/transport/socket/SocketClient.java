package com.ngnl.transport.socket;

import org.slf4j.LoggerFactory;

import com.ngnl.transport.http.HttpServer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author 47
 *
 */
public class SocketClient{
	
	int port;
	
	ChannelInitializer<SocketChannel> channelInitializer = null;
	
	public Channel channel;

	public void connect (String ip, int port)throws Exception{
		this.port = port;
		
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(eventLoopGroup)
					 .channel(NioSocketChannel.class)
					 .handler(channelInitializer);
			
			ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
			channelFuture.addListener(future ->{
				if (future.isSuccess())
					LoggerFactory.getLogger(HttpServer.class).info("Netty socket client connectted port:{}", port);
				else
					LoggerFactory.getLogger(HttpServer.class).error("Netty socket client connectted failed!");
			});
			
			this.channel = channelFuture.channel();
			//执行这句会阻塞线程 直到服务端断开连接
			channelFuture.channel().closeFuture().sync();
		} finally {
			eventLoopGroup.shutdownGracefully();
		}
	}
	
	public void setChannelInitializer (ChannelInitializer<SocketChannel> channelInitializer) {
		this.channelInitializer = channelInitializer;
	}
}
