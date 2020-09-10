package org.housework.server;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.housework.server.models.ServerConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ConfigurationService {
	public ServerConfiguration configuration;
	private ConfigurationService() {	
	}
	
	public static final ConfigurationService INSTANCE = new ConfigurationService();
	 
	public static ConfigurationService getInstance() {
		return INSTANCE;
	}	
	
	public ServerConfiguration getConfiguration() {
		return configuration;
	}
	
	

	public void loadInstance(File file) throws IOException {
		configuration = loadConfiguration(file);
	}

	private ServerConfiguration loadConfiguration(File file) throws IOException {
		if(! file.exists()) {
			configuration = new ServerConfiguration();
			configuration.publicUrl = "http://localhost:8080";						
			return configuration;
		}
		try(FileInputStream fin = new FileInputStream(file)) {
			return loadConfiguration(fin);
		}		
	}
		
	static ServerConfiguration loadConfiguration(InputStream in)  {
		ObjectMapper mapper = new ObjectMapper();		
		ServerConfiguration configuration;
		try {
			configuration = mapper.readValue(in, ServerConfiguration.class);
		} catch (Exception e) {			
			throw new RuntimeException(e);
		}
		return configuration;
	}
}
