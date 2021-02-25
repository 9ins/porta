package org.chaostocosmos.net.porta;

import org.junit.Before;
import org.junit.Test;

public class testPortaMain {

    String configPath;

    @Before
    public void before_test() {
        this.configPath = "D:\\Projects\\porta\\config.yml";
    }
    
    @Test
    public void test_TCPProxy() throws Exception {
        PortaMain tcpProxy = new PortaMain(this.configPath);
        tcpProxy.startProxy();
    }

    public static void main(String[] args) throws Exception {
        testPortaMain proxy = new testPortaMain();
        proxy.before_test();
        proxy.test_TCPProxy();
    }
}
