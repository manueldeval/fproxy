package org.deman.fproxy.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.vavr.jackson.datatype.VavrModule;

import java.io.File;

public class ConfigLoader {
    public static Config load(String filename) throws ConfigException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.registerModule(new VavrModule());
        try {
            return mapper.readValue(new File(filename), Config.class);
        } catch (Exception e) {
            throw new ConfigException("Unable to read the '"+filename+"' config file.",e);
        }
    }
}
