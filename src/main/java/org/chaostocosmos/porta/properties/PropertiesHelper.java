package org.chaostocosmos.porta.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.chaostocosmos.porta.Constants;
import org.chaostocosmos.porta.Logger;
import org.chaostocosmos.porta.PortaException;
import org.chaostocosmos.porta.PortaApp;
import org.chaostocosmos.porta.UtilBox;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.Yaml;

/**
 * 
 * ConfigHandler - Managing configuration of Porta
 *
 * @author 9ins 2020. 11. 17.
 */
public class PropertiesHelper { 

	private static PropertiesHelper propertiesHelper;
	Logger logger = Logger.getInstance();
	Path configsPath;
	List<Path> configFiles;
	Map<File, Object> configsMap = new HashMap<>();

	/**
	 * Get instance of ConfigsHelper
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static PropertiesHelper getInstance() {
		return getInstance(PortaApp.configPath);
	}

	/**
	 * Get instance of ConfigsHelper
	 * 
	 * @param configPath
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static PropertiesHelper getInstance(Path configPath) {
		if (propertiesHelper == null) {
			if(!configPath.toFile().isDirectory()) {
				throw new IllegalArgumentException("Config path must be directory!!! Specified path is "+configPath.toString());
			}
			propertiesHelper = new PropertiesHelper(configPath);
		}
		return propertiesHelper;
	}

	/**
	 * Constructor
	 * 
	 * @param configPath
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private PropertiesHelper(Path configPath) {
		this.configsPath = configPath;
		try {
			loadYamlPaths();
			loadConfig(); 
		} catch (IOException | ClassNotFoundException e) {
			logger.throwable(e);
		}
		logger.info(("[Config] Configuration loaded from: " + configPath.toString()));
	}

	/**
	 * Load yaml path list
	 * @throws IOException
	 */
	private void loadYamlPaths() throws IOException {
		this.configFiles = Files.walk(this.configsPath).filter(p -> p.toFile().getName().endsWith(".yml")).collect(Collectors.toList());
	}

	/**
	 * Load config.yml
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 */
	public void loadConfig() throws FileNotFoundException, ClassNotFoundException {
		loadConfig(this.configFiles);
	}

	/**
	 * Load config.yml
	 * @param configPath
	 * @throws ClassNotFoundException
	 * @throws FileNotFoundException
	 */
	public void loadConfig(List<Path> configPath) throws ClassNotFoundException, FileNotFoundException {
		for(Path path : configPath) {
			String fname = path.toFile().getName(); 
			String className = (fname.charAt(0)+"").toUpperCase()+fname.subSequence(1, fname.lastIndexOf("."));
			String cname = this.getClass().getPackage().getName()+"."+className;
			Class cls = Class.forName(cname);
			Representer representer = new Representer();
			representer.addClassTag(cls.getClass(), Tag.MAP);	
			Yaml yaml = new Yaml(representer);
			Object obj = yaml.loadAs(new FileInputStream(path.toFile()), cls);
			this.configsMap.put(path.toFile(), obj);
		}
	}

	/**
	 * Dump config to config.yml
	 * 
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	public void dumpConfigs() throws IllegalArgumentException, IllegalAccessException, IOException {
		dumpConfigs(this.configsPath, Constants.defaultCharset);
	}

	/**
	 * Dump yaml files
	 * 
	 * @param configPath
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	public void dumpConfigs(Path configPath, Charset charset) throws IllegalArgumentException, IllegalAccessException, IOException {
		for(Map.Entry<File, Object> entry : this.configsMap.entrySet()) {
			dump(entry.getKey(), entry.getValue(), charset);
		}
	}

	/**
	 * Dump config for specified name
	 * @param name
	 * @throws PortaException
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public void dump(String name) throws PortaException, IOException, IllegalArgumentException, IllegalAccessException {
		File file = this.configsMap.keySet().stream().filter(f -> f.getName().substring(0, f.getName().lastIndexOf(".")).equals(name)).findAny().orElse(null);
		if(file == null) {
			throw new PortaException("ERRCODE009", new Object[]{"name"});
		}
		dump(file, this.configsMap.get(file), Constants.defaultCharset);
	}

	/**
	 * Dump to file
	 * @param file
	 * @param obj
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public void dump(File file, Object obj, Charset charset) throws IOException, IllegalArgumentException, IllegalAccessException {
		DumperOptions options = new DumperOptions();
		options.setDefaultFlowStyle(FlowStyle.BLOCK);
		options.setIndent(4);
		options.setPrettyFlow(true);
		Representer representer = new Representer();
		representer.addClassTag(obj.getClass(), Tag.MAP);
		Yaml yml = new Yaml(representer, options);
		Map<String, Object> map = getMap(obj);		
		yml.dump(map, new OutputStreamWriter(new FileOutputStream(file), charset.name()));
	}

	/**
	 * Get field map from object
	 * @param obj
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public Map<String, Object> getMap(Object obj) throws IllegalArgumentException, IllegalAccessException {
		Map<String, Object> map = new LinkedHashMap<>();
		Field[] fields = obj.getClass().getDeclaredFields();
		for(Field field : fields) {
			Object o = field.get(obj);
			map.put(field.getName(), o);
		}
		return map;
	}

	/**
	 * Get Yaml object
	 * @param yamlFileName
	 * @return
	 */
	public Object getYamlObject(String yamlFileName) {
		File file = this.configsMap.keySet().stream().filter(f -> f.getName().equals(yamlFileName)).findAny().orElse(null);
		if(file == null) {
			throw new RuntimeException("There isn't exist credentials.yml. Please check it.");
		}
		return this.configsMap.get(file);
	}

	/**
	 * Get Yaml file path
	 * @param yamlFileName
	 * @return
	 */
	public Path getYamlPath(String yamlFileName) {
		return this.configFiles.stream().filter(p -> p.toFile().getName().equals(yamlFileName)).findAny().orElse(null);
	}

	/**
	 * Get Credentials object
	 * @return
	 */
	public Credentials getCredentials() {
		return (Credentials)getYamlObject("credentials.yml");
	}

	/**
	 * Get Configs object
	 * @return
	 */
	public Configs getConfigs() {
		return (Configs)getYamlObject("configs.yml");
	}

	/**
	 * Get Handler object
	 * @return
	 */
	public Handlers getHandlers() {
		return (Handlers)getYamlObject("handlers.yml");
	}

	/**
	 * Get Messages object
	 * @return
	 */
	public Messages getMessages() {
		return (Messages)getYamlObject("messages.yml");
	}

	/**
	 * Get super user
	 * @return
	 */
	public String getSuperUser() {			
		return getCredentials().getSuperUser();
	}
	
	/**
	 * Get super password
	 * @return
	 */
	public String getSuperPassword() {
		return getCredentials().getSuperPassword();
	}
	
	/**
	 * Whether specified user and password is super user.
	 * @param user
	 * @param password
	 * @return
	 */
	public boolean isSuperUser(String user, String password) {
		Credentials credentials = getCredentials();
		if(user.equals(credentials.getSuperUser()) && password.equals(credentials.getSuperPassword())) {
			return true;
		}
		return false;
	}

	/**
	 * Get Credential object matching with specified user and password.
	 * @param user
	 * @param password
	 * @return
	 */
	public Credential isValidUser(final String user, final String password) {
		Credentials credentials = getCredentials();
		List<Session> sessions = credentials.getSessions();
		for(Session session : sessions) {
			Credential credential = session.getCredential().stream().filter(c -> c.getUsername().equals(user) && c.getPassword().equals(password)).findAny().orElse(null);
			if(credential != null) {
				return credential;
			}
		}
		return null;
	}

	/**
	 * Get session mapping object by session name
	 * @param sessionName
	 * @return
	 */
	public SessionMappingConfigs getSessionMapping(String sessionName) {		
		return getConfigs().getSessionMapping().get(sessionName);
	}

	/**
	 * Get session mapping object by source address and target port
	 * @param inetAddress
	 * @return
	 */
	public String getSessionName(Socket socket) {
		for (Entry<String, SessionMappingConfigs> entry : getConfigs().getSessionMapping().entrySet()) {
			String name = entry.getKey();
			SessionMappingConfigs sm = entry.getValue();
			if (sm.getAllowedHosts().stream().anyMatch(a -> a.equals(socket.getInetAddress().getHostAddress()))) {
				return name;
			}
		}
		return null;
	}

	/**
	 * Set session mapping object field value by name and value.
	 * 
	 * @param sessionName
	 * @param fieldName
	 * @param value
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void setSessionMappingValue(String sessionName, String fieldName, Object value) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		SessionMappingConfigs sm = getSessionMapping(sessionName);
		Field field = sm.getClass().getDeclaredField(fieldName);
		field.set(sm, value);
	}
    /**
     * Get system message by key
     * 
     * @return
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public String getSystemMessage(String msgKey, String... params) {
        return getMessage(MSG_TYPE.system, msgKey, params);
    }

    /**
     * Get information message by key
     * 
     * @return
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    public String getInformationMessage(String msgKey, String... params) {
        return getMessage(MSG_TYPE.message, msgKey, params);
    }

    /**
     * Get error message by key
     * 
     * @return
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    public String getErrorMessage(String msgKey, Object... params) {
        return getMessage(MSG_TYPE.error, msgKey, params);
    }

    /**
     * 
     * @param msgType
     * @param params
     * @return
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public String getMessage(MSG_TYPE msgType, String msgKey, Object... params) {     
		Messages messages = getMessages();
		String methodName = "get"+msgType.name().substring(0, 1).toUpperCase()+msgType.name().substring(1);
		Class[] classes = Arrays.stream(params).map(s -> s.getClass()).collect(Collectors.toList()).toArray(new Class[0]);
		String message = null;
		try {
			Method method = messages.getClass().getMethod(methodName, null);
			message = ((Map)method.invoke(messages, null)).get(msgKey)+"";
		} catch(Exception e) {
			Logger.getInstance().throwable(e);
		}
		AtomicInteger idx = new AtomicInteger();
		if(params == null) {
			return message;
		} else {
			String[] splited = message.split("\\$", -1);
			String res = "";
			if(splited.length -1  == params.length) {
				int i;
				for(i=0; i<splited.length-1; i++) {
					res += splited[i];
				}
				return res += splited[i];
			} else {
				return message;
			}
		}
	}

    /**
     * Get error json
     * @param msgKey
     * @param params
     * @return
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    public String getErrorJson(String msgKey, Object... params) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("ERROR_CODE", msgKey);
        map.put("ERROR_MSG", getMessage(MSG_TYPE.error, msgKey, params));
        return UtilBox.getMapToJson(map);
    }

	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, IOException, NoSuchFieldException, SecurityException, ClassNotFoundException {
		Path path = Paths.get("./config.yml");
		PropertiesHelper handler = new PropertiesHelper(path);
		// handler.loadConfig();
		// System.out.println("ADMIN: "+handler.getAdminUser());
		// System.out.println("PASSWD: "+handler.getAdminPassword());
		// System.out.println("ORACLE:
		// "+handler.getSessionMapping("Oracle").toString());
		// handler.setSessionMappingValue("Oracle", "readTimeout", new Integer(150));
		String msg = "{\"simpleMessage\":[\""+""+"\"], \"toChannel\":[\"202020\"]}";
	}
}
