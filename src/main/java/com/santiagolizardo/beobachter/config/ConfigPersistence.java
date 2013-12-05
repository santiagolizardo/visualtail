package com.santiagolizardo.beobachter.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;

import com.santiagolizardo.beobachter.util.RecentFiles;

public class ConfigPersistence {

	public PropertiesConfiguration loadProperties(String fileName)
			throws ConfigurationException, FileNotFoundException, IOException {
		File file = new File(fileName);
		File dir = file.getParentFile();
		if (!dir.exists()) {
			dir.mkdirs();
		}
		if (!file.exists()) {
			InputStream is = ConfigData.class
					.getResourceAsStream("default_config.properties");
			IOUtils.copy(is, new FileOutputStream(file));
		}

		return new PropertiesConfiguration(fileName);
	}

	public void saveProperties(PropertiesConfiguration configuration)
			throws ConfigurationException {
		byte i = 0;
		RecentFiles arraysUtil = RecentFiles.getInstance();
		for (; i < arraysUtil.list.size(); i++) {
			String propertyName = "recent." + i + ".file_name";
			configuration.setProperty(propertyName, arraysUtil.list.get(i)
					.toString());
		}
		for (; i < 10; i++) {
			String propertyName = "recent." + i + ".file_name";
			configuration.clearProperty(propertyName);
		}

		configuration.save();
	}
}
