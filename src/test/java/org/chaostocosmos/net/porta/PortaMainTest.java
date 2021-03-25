package org.chaostocosmos.net.porta;

import org.chaostocosmos.porta.PortaMain;
import org.junit.Before;
import org.junit.Test;

public class PortaMainTest {

    String configPath;

    @Before
    public void before_test() {
        this.configPath = "D:\\Projects\\porta\\config";
    }

    @Test
    public void test_TCPProxy() throws Exception {
        PortaMain tcpProxy = new PortaMain(this.configPath);
        tcpProxy.start();
    }

    public static void main(String[] args) throws Exception {
        PortaMainTest proxy = new PortaMainTest();
        proxy.before_test();
        proxy.test_TCPProxy();
    }
}
