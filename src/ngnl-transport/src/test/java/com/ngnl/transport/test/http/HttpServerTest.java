package com.ngnl.transport.test.http;

import com.ngnl.transport.http.HttpServer;

/**
 * @author 47
 *
 */
public class HttpServerTest {

	public static void main (String[] args) throws Exception{
		HttpServer httpServer = new HttpServer();
		httpServer.start();
	}
}
