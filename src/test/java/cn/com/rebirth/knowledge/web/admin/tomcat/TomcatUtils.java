/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin TomcatUtils.java 2012-7-19 13:07:49 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.tomcat;

import org.apache.catalina.Context;
import org.apache.catalina.Engine;
import org.apache.catalina.Host;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Embedded;

/**
 * The Class TomcatUtils.
 *
 * @author l.xue.nong
 */
public abstract class TomcatUtils {

	/**
	 * Builds the normal server.
	 *
	 * @param port the port
	 * @param contextPath the context path
	 * @return the embedded
	 */
	public static Embedded buildNormalServer(int port, String contextPath) {
		return buildNormalServer(port, contextPath, System.getProperty("project.name"), "UTF-8");
	}

	/**
	 * Builds the normal server.
	 *
	 * @param port the port
	 * @param contextPath the context path
	 * @param serverName the server name
	 * @param encoding the encoding
	 * @return the embedded
	 */
	public static Embedded buildNormalServer(int port, String contextPath, String serverName, String encoding) {
		Embedded tomcat = new Embedded();
		tomcat.setCatalinaBase(System.getProperty("user.dir") + "/server");
		Host host = tomcat.createHost("127.0.0.1", tomcat.getCatalinaHome() + "/webapps");
		Context context = tomcat.createContext(contextPath, System.getProperty("user.dir") + "/src/main/webapp");
		// 当以debug模式启动时，修改可立即生效
		context.setReloadable(false);
		host.addChild(context);

		Engine engine = tomcat.createEngine();
		engine.setName(serverName);
		engine.addChild(host);
		engine.setDefaultHost(host.getName());
		tomcat.addEngine(engine);
		// 只能本机访问
		// Connector connector = tomcat.createConnector("127.0.0.1",
		// getPort(),false);
		// 可从其它机器访问
		Connector connector = tomcat.createConnector((java.net.InetAddress) null, port, false);
		connector.setURIEncoding(encoding);
		tomcat.addConnector(connector);
		return tomcat;
	}
}
