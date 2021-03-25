package org.chaostocosmos.net.porta;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.chaostocosmos.porta.properties.Configs;
import org.chaostocosmos.porta.properties.Credentials;
import org.chaostocosmos.porta.properties.PropertiesHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PropertiesHelperTest {

    Path path;
    PropertiesHelper helper;

    @Before
    public void beforeTest() throws ClassNotFoundException, IOException {
        path = Paths.get("D:/Projects/porta/config");
        this.helper = PropertiesHelper.getInstance(this.path);         
    }

    @Test
    public void configsTest() throws IOException, ClassNotFoundException {
        Configs configs = helper.getConfigs(); 
        System.out.println(configs.getManagementConfigs());                
    }    

    @Test
    public void credentialsTset() {
        Credentials credentials = helper.getCredentials();
        System.out.println(credentials);
    }

    @Test
    public void getSuperUserTest() {
        String s = this.helper.getCredentials().getSuperUser(); 
        Assert.assertEquals("super", s);
    }

}
