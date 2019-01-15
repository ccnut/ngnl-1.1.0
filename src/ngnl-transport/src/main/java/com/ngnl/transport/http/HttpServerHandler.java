package com.ngnl.transport.http;

import java.net.URI;

import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/**
 * @author 47
 *
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject>{

	/**
	 * @see io.netty.channel.SimpleChannelInboundHandler#channelRead0(io.netty.channel.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
		if (msg instanceof HttpRequest) {
			HttpRequest httpRequest = (HttpRequest)msg;
			URI uri = new URI(httpRequest.uri());
			if ("favicon.ico".equals(uri.getPath()) == true) {
				LoggerFactory.getLogger(HttpServerHandler.class).info("Http request 'favicon.ico. skipped.'");
				return;
			}
			
			ByteBuf buffer = Unpooled.copiedBuffer("hello world!", CharsetUtil.UTF_8);
			FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buffer);
			
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain")
							  .set(HttpHeaderNames.CONTENT_LENGTH, buffer.readableBytes());
			
			ctx.writeAndFlush(response);
			
			//TODO: handle close http request. condition on http1.0 or http 1.1
			ctx.channel().close();
		}else {
//			LoggerFactory.getLogger(HttpServerHandler.class).warn("Unexcept http request {}", msg.toString());
		}
	}
	
	
	/**
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelActive(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		LoggerFactory.getLogger(HttpServerHandler.class).info("channelActive");
		super.channelActive(ctx);
	}
	
	/**
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelRegistered(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		LoggerFactory.getLogger(HttpServerHandler.class).info("channelRegistered");
		super.channelRegistered(ctx);
	}
	
	/**
	 * @see io.netty.channel.ChannelHandlerAdapter#handlerAdded(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		LoggerFactory.getLogger(HttpServerHandler.class).info("handlerAdded");
		super.handlerAdded(ctx);
	}
	
	/**
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelInactive(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		LoggerFactory.getLogger(HttpServerHandler.class).info("channelInactive");
		super.channelInactive(ctx);
	}
	
	/**
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelUnregistered(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		LoggerFactory.getLogger(HttpServerHandler.class).info("channelUnregistered");
		super.channelUnregistered(ctx);
	}
}
