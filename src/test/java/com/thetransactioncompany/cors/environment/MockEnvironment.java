package com.thetransactioncompany.cors.environment;


import java.util.Properties;

import com.thetransactioncompany.cors.CorsConfigurationLoader;


/**
 * Mock system variables environment.
 *
 * @author David Bellem
 * @author Vladimir Dzhuvinov
 */
public class MockEnvironment implements Environment {


	private String configurationFileName;

	
	@Override
	public String getProperty(String name) {

		if(name.equalsIgnoreCase(CorsConfigurationLoader.CONFIG_FILE_PARAM_NAME))
			return configurationFileName;
		
		return null;
	}


	@Override
	public Properties getProperties() {

		return null;
	}


	public String getConfigurationFileName() {

		return configurationFileName;
	}


	public void setConfigurationFileName(String configurationFileName) {

		this.configurationFileName = configurationFileName;
	}
}
