/**
 * Copyright (c) 2007-2014 Kaazing Corporation. All rights reserved.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.kaazing.tools;

import org.kaazing.net.ws.WebSocket;
import org.kaazing.net.ws.WebSocketFactory;
import org.kaazing.net.ws.WebSocketMessageReader;
import org.kaazing.net.ws.WebSocketMessageType;
import org.kaazing.net.ws.WebSocketMessageWriter;
import org.kaazing.net.http.HttpRedirectPolicy;

import java.net.URI;
import java.nio.ByteBuffer;
import java.io.InputStream;
import java.io.IOException;

public class WSCat {

    private static WebSocketFactory wsFactory;
    private static WebSocket webSocket;

    public static void main(String[] args) {
	if (args.length < 1) {
	    System.err.println("wscat <url>");
	    return;
	}

	try {
	    URI location = new URI(args[0]);
	    
	    wsFactory = WebSocketFactory.createWebSocketFactory();
	    wsFactory.setDefaultRedirectPolicy(HttpRedirectPolicy.ALWAYS);
	    
	    webSocket = wsFactory.createWebSocket(location);
	    webSocket.connect();
	    
	    Thread t = new Thread() {
		    @Override
		    public void run() {
			try {
			    WebSocketMessageWriter messageWriter = webSocket.getMessageWriter();
			    
			    InputStream in = System.in;
			    byte b[] = new byte[8192];
			    
			    for (;;) {
				int n = in.read(b);
				if (n < 0) {
				    break;
				}
				else if (n > 0) {
				    ByteBuffer buf = ByteBuffer.wrap(b, 0, n);
				    messageWriter.writeBinary(buf);
				}
			    }
			} catch (IOException e) {
			    e.printStackTrace();
			}
		    }
		};
	    t.start();
	    
	    final WebSocketMessageReader messageReader = webSocket.getMessageReader();
	    WebSocketMessageType type = null;
	    while ((type = messageReader.next()) != WebSocketMessageType.EOS) {
		switch (type) {
		case BINARY:
		    ByteBuffer data = messageReader.getBinary();
		    while (data.position() < data.limit()) {
			System.out.write(data.get());
		    }
		    break;
		case TEXT:
		    CharSequence text = messageReader.getText();
		    System.out.print(text.toString());
		    break;
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
