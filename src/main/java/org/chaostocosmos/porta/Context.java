package org.chaostocosmos.porta;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.chaostocosmos.porta.properties.Configs;
import org.chaostocosmos.porta.properties.PropertiesHelper;
import org.chaostocosmos.porta.properties.Credentials;
import org.chaostocosmos.porta.properties.Handlers;
import org.chaostocosmos.porta.properties.Messages;

/**
 * Context object
 */
public class Context {
    public String TRADE_MARK; 
    public String version;
	public Path portaHomePath;
    public Path configsPath;
	public Path credentialsPath;
	public Path messagesPath;
    public Path trademarkPath;
    public PropertiesHelper propertiesHelper;
    public Configs configs; 
    public Credentials credentials;
    public Messages messages;
    public Handlers handlers;

    public Context(Path configPath) throws IOException, ClassNotFoundException {
        configsPath = configPath;
        portaHomePath = Paths.get(System.getProperty("user.dir"));
        propertiesHelper = PropertiesHelper.getInstance(configsPath);
        configs = propertiesHelper.getConfigs();
        handlers = propertiesHelper.getHandlers();
        credentials = propertiesHelper.getCredentials();
        version = configs.getAppConfigs().getVersion();
        credentialsPath = propertiesHelper.getYamlPath("credentials.yml");
        messagesPath = propertiesHelper.getYamlPath("messages.yml"); 
        messages = propertiesHelper.getMessages();       
        trademarkPath = portaHomePath.resolve("config").resolve("trademark");
        TRADE_MARK =new FileSource().readTradeMark(trademarkPath); 
    }

    public String getTRADE_MARK() {
        return this.TRADE_MARK;
    }

    public void setTRADE_MARK(String TRADE_MARK) {
        this.TRADE_MARK = TRADE_MARK;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Path getPortaHomePath() {
        return this.portaHomePath;
    }

    public void setPortaHomePath(Path portaHomePath) {
        this.portaHomePath = portaHomePath;
    }

    public Path getConfigsPath() {
        return this.configsPath;
    }

    public void setConfigsPath(Path configsPath) {
        this.configsPath = configsPath;
    }

    public Handlers getHandlers() {
        return this.handlers;
    }

    public void setHandlers(Handlers handlers) {
        this.handlers = handlers;
    }

    public Path getCredentialsPath() {
        return this.credentialsPath;
    }

    public void setCredentialsPath(Path credentialsPath) {
        this.credentialsPath = credentialsPath;
    }

    public Path getMessagesPath() {
        return this.messagesPath;
    }

    public void setMessagesPath(Path messagesPath) {
        this.messagesPath = messagesPath;
    }

    public Path getTrademarkPath() {
        return this.trademarkPath;
    }

    public void setTrademarkPath(Path trademarkPath) {
        this.trademarkPath = trademarkPath;
    }

    public PropertiesHelper getPropertiesHelper() {
        return this.propertiesHelper;
    }

    public void setPropertiesHelper(PropertiesHelper propertiesHelper) {
        this.propertiesHelper = propertiesHelper;
    }

    public Configs getConfigs() {
        return this.configs;
    }

    public void setConfigs(Configs configs) {
        this.configs = configs;
    }

    public Credentials getCredentials() {
        return this.credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public Messages getMessages() {
        return this.messages;
    }

    public void setMessages(Messages messages) {
        this.messages = messages;
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

    @Override
    public String toString() {
        return "{" +
            " TRADE_MARK='" + TRADE_MARK + "'" +
            ", version='" + version + "'" +
            ", portaHomePath='" + portaHomePath + "'" +
            ", configsPath='" + configsPath + "'" +
            ", credentialsPath='" + credentialsPath + "'" +
            ", messagesPath='" + messagesPath + "'" +
            ", trademarkPath='" + trademarkPath + "'" +
            ", propertiesHelper='" + propertiesHelper + "'" +
            ", configs='" + configs + "'" +
            ", credentials='" + credentials + "'" +
            ", messages='" + messages + "'" +
            ", handlers='" + handlers + "'" +
            "}";
    }
}
