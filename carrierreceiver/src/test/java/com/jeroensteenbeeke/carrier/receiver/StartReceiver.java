package com.jeroensteenbeeke.carrier.receiver;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.webapp.WebAppContext;

public class StartReceiver {
	/**
	 * Main function, starts the jetty server.
	 * 
	 * @param args
	 *            Program arguments
	 */
	public static void main(String[] args) {
		Server server = new Server();
		SocketConnector connector = new SocketConnector();
		connector.setPort(8080);
		server.setConnectors(new Connector[] { connector });

		WebAppContext web = new WebAppContext();
		web.setContextPath("/carrier");
		web.setWar("src/main/webapp");
		server.addHandler(web);

		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(100);
		}
	}
}
