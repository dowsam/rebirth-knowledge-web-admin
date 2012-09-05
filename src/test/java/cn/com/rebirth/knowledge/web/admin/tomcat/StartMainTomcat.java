package cn.com.rebirth.knowledge.web.admin.tomcat;

import org.apache.catalina.startup.Embedded;

import cn.com.rebirth.commons.utils.H2ServerUtils;

/**
 * The Class StartMainTomcat.
 *
 * @author l.xue.nong
 */
public class StartMainTomcat {

	/** The Constant PORT. */
	public static final int PORT = 8080;

	/** The Constant CONTEXT. */
	public static final String CONTEXT = "/rebirth-knowledge-web-admin";

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception {
		System.setProperty("catalina.base", System.getProperty("user.dir") + "/server");
		System.setProperty("zk.zkConnect", "192.168.2.179:2181");
		System.setProperty("rebirth.service.middleware.development.model", "true");
		H2ServerUtils.buildH2Server("-web", "-tcp");
		Embedded server = TomcatUtils.buildNormalServer(PORT, CONTEXT, "rebirth-knowledge-web-admin", "utf-8");
		server.start();
		System.out.println("Hit Enter in console to stop server");
		if (System.in.read() != 0) {
			server.stop();
			H2ServerUtils.stopAll();
			System.out.println("Server stopped");
			System.exit(0);
		}
	}

}
