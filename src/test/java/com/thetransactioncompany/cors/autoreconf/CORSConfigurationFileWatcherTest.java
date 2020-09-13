package com.thetransactioncompany.cors.autoreconf;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

import junit.framework.TestCase;

import com.thetransactioncompany.cors.CorsConfigurationLoader;
import com.thetransactioncompany.cors.MockFilterConfig;


/**
 * Tests the CORS configuration file watcher.
 */
public class CorsConfigurationFileWatcherTest extends TestCase {


	@Override
	public void setUp()
		throws Exception {

		Properties properties = new Properties();
		properties.setProperty("cors.allowOrigin", "https://www.example.org");

		OutputStream os = new FileOutputStream(new File("test.properties"));
		properties.store(os, null);
		os.close();
	}


	public void testWatchParameterName() {

		assertEquals("cors.configFilePollInterval", CorsConfigurationFileWatcher.POLL_INTERVAL_PARAM_NAME);
	}


	public void testDefaultWatchIntervalConstant() {

		assertEquals(20, CorsConfigurationFileWatcher.DEFAULT_POLL_INTERVAL_SECONDS);
	}


	public void testDefaultWatchInterval() {

		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.setInitParameter(CorsConfigurationLoader.CONFIG_FILE_PARAM_NAME, "test.properties");

		CorsConfigurationFileWatcher watcher = new CorsConfigurationFileWatcher(filterConfig);

		assertEquals(CorsConfigurationFileWatcher.DEFAULT_POLL_INTERVAL_SECONDS, watcher.getPollIntervalSeconds());
	}


	public void testDetectChange()
		throws Exception {

		// Override watch interval
		System.setProperty(CorsConfigurationFileWatcher.POLL_INTERVAL_PARAM_NAME, "1");

		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.setInitParameter(CorsConfigurationLoader.CONFIG_FILE_PARAM_NAME, "test.properties");

		CorsConfigurationFileWatcher watcher = new CorsConfigurationFileWatcher(filterConfig);

		assertEquals(1L, watcher.getPollIntervalSeconds());

		watcher.start();

		Properties properties = new Properties();
		properties.setProperty("cors.allowOrigin", "https://www.example.com");

		OutputStream os = new FileOutputStream(new File("test.properties"));
		properties.store(os, null);
		os.close();

		Thread.sleep(1100);

		assertTrue(watcher.reloadRequired());

		watcher.stop();
	}


	public void testDetectNoChange()
		throws Exception {

		// Override watch interval
		System.setProperty(CorsConfigurationFileWatcher.POLL_INTERVAL_PARAM_NAME, "1");

		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.setInitParameter(CorsConfigurationLoader.CONFIG_FILE_PARAM_NAME, "test.properties");

		CorsConfigurationFileWatcher watcher = new CorsConfigurationFileWatcher(filterConfig);

		assertEquals(1L, watcher.getPollIntervalSeconds());

		watcher.start();

		Thread.sleep(1100);

		assertFalse(watcher.reloadRequired());

		watcher.stop();
	}


	@Override
	public void tearDown() {

		System.clearProperty("cors.configFilePollInterval");

		new File("test.properties").delete();
	}
}
