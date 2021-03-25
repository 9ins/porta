package org.chaostocosmos.net.porta;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;

import org.chaostocosmos.porta.Context;
import org.chaostocosmos.porta.web.handlers.ServletHandlerManager;
import org.junit.Before;
import org.junit.Test;

public class ServletHandlerManagerTest {

    Context context;

    @Before
    public void doBefore() throws ClassNotFoundException, IOException {
        this.context = new Context(Paths.get("D:/Projects/porta/config"));
    }

    @Test
    public void loadHandlersTest() throws Exception {
        ServletHandlerManager manager = new ServletHandlerManager(this.context);
        manager.loadHandlersAndServlets();
    }
    
}
