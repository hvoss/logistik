package de.hsbremen.kss.configuration;

import java.io.File;

/**
 * 
 * @author henrik
 * 
 */
public interface ConfigurationParser {

    /**
     * parses the configuration out of the given file.
     * 
     * @param file
     *            file which contains the configuration
     * @return the parsed configuration
     */
    Configuration parseConfiguration(File file);
}
