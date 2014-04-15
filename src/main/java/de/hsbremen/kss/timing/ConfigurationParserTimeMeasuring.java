package de.hsbremen.kss.timing;

import java.io.File;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.ConfigurationParser;

public class ConfigurationParserTimeMeasuring extends TimeMeasuring {

    private final ConfigurationParser configurationParser;

    private final File file;

    private Configuration configuration;

    public ConfigurationParserTimeMeasuring(final ConfigurationParser configurationParser, final File file) {
        this.configurationParser = configurationParser;
        this.file = file;
    }

    @Override
    protected void run() {
        this.configuration = this.configurationParser.parseConfiguration(this.file);
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }
}
