package org.chaostocosmos.net.porta;

import org.chaostocosmos.porta.PortaApp;
import org.junit.Before;
import org.junit.Test;

public class PortaAppTest {

    String configPath;

    @Before
    public void before_test() {
        this.configPath = "D:\\Projects\\porta\\config";
    }

    @Test
    public void test_TCPProxy() throws Exception {
        PortaApp tcpProxy = new PortaApp(this.configPath);
        tcpProxy.start();
    }

    public static void main(String[] args) throws Exception {
        PortaAppTest proxy = new PortaAppTest();
        proxy.before_test();
        proxy.test_TCPProxy();
    }
}
