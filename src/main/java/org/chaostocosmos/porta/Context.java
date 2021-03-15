package org.chaostocosmos.porta;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.chaostocosmos.porta.properties.Configs;
import org.chaostocosmos.porta.properties.PropertiesHelper;
import org.chaostocosmos.porta.properties.Credentials;
import org.chaostocosmos.porta.properties.Messages;

/**
 * Context object
 */
public class Context {
    public static String TRADE_MARK; 
    public static String version;
	public static Path portaHomePath;
    public static Path configsPath;
	public static Path credentialsPath;
	public static Path messagesPath;
    public static Path trademarkPath;
    public static PropertiesHelper propertiesHelper;
    public static Configs configs; 
    public static Credentials credentials;
    public static Messages messages;

    public Context(Path configPath) throws IOException, ClassNotFoundException {
        configsPath = configPath;
        portaHomePath = Paths.get(System.getProperty("user.dir"));
        propertiesHelper = PropertiesHelper.getInstance(configsPath);
        configs = propertiesHelper.getConfigs();
        version = configs.getAppConfigs().getVersion();
        credentialsPath = propertiesHelper.getYamlPath("credentials.yml");
        messagesPath = propertiesHelper.getYamlPath("messages.yml");
        trademarkPath = propertiesHelper.getYamlPath("trademark");
        TRADE_MARK =new FileSource().readTradeMark(trademarkPath); 
    }

	public class FileSource {
		public List<String> readFileSource(Path path) {
			try {
				return Files.readAllLines(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
        public String readTradeMark(Path path) {
            return readFileSource(path).stream().collect(java.util.stream.Collectors.joining(System.lineSeparator()));
        }
	}    
}
