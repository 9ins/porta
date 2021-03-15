package org.chaostocosmos.net.porta;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.chaostocosmos.porta.properties.PropertiesHelper;
import org.chaostocosmos.porta.web.ManagementServer;
import org.junit.Before;
import org.junit.Test;

public class ManagementServerTest {

	Path configPath;
    PropertiesHelper configHandler; 
	
	@Before
	public void before_test() throws Exception {
		this.configPath = Paths.get("D:/Projects/TCPProxy/config.yml");
	}

	@Test
    public void test_ManagementServer() throws Exception {
		this.configHandler = PropertiesHelper.getInstance(this.configPath);
		Path credentialPath = this.configHandler.getYamlPath("credentials.yml");
		ManagementServer server = new ManagementServer(null, this.configHandler);
	}
	
	public static void main(String[] args) throws Exception {
		ManagementServerTest server = new ManagementServerTest();
		server.before_test();
		server.test_ManagementServer();
	}
}
