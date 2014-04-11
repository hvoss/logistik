package de.hsbremen.kss.configuration;

import java.io.File;

public interface ConfigurationParser {
	
	/**
	 * parses the configuration out of the given file.
	 * 
	 * @param file file which contains the configuration
	 * @return the parsed configuration
	 */
	Configuration parseConfiguration(File file);
	
	/**
	 * write the given configuration the given file.
	 * 
	 * @param file file in which the configuration is to be written
	 * @param configuration the configuration to persists
	 */
	void writeConfiguration(File file, Configuration configuration);
}
