package com.terahash.arbitrage.service.impl;

import com.terahash.arbitrage.model.Config;
import com.terahash.arbitrage.repository.ConfigRepository;
import com.terahash.arbitrage.service.ConfigService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service(value = "configService")
public class ConfigServiceImpl implements ConfigService {
    @Autowired
    private ConfigRepository configRepository;
    @Autowired
    private Environment env;

    List<Config> config = new ArrayList<Config>();
    private static final Logger log = LogManager.getLogger("magistra");

    @Override
    public void load() {
        log.debug("Ricarico configurazione...");
        config = configRepository.findAll();
        dump();
    }

    private void dump() {
        for (Config c: config) {
            log.debug("[{}] {}={}", c.getGroup(), c.getKey(), c.getValue());
        }
    }

    @Override
    public String getProperty(String key) {
        return getProperty(key, "*");
    }

    @Override
    public String getProperty(String key, String group) {

        if( config.size() <= 0 ) load();

        for(Config c: config) {
            if( c.getKey().compareToIgnoreCase(key) == 0 && c.getGroup().compareToIgnoreCase(group) == 0 ) {
                return c.getValue();
            }
        }
        if(env.containsProperty(key)) return env.getProperty(key);
        return "";
    }

    @Override
    public String getProperty(String key, String group, String defaultValue) {
        String v = getProperty(key, group);
        return (v != null && !v.isEmpty() ) ? v : defaultValue;
    }

    @Override
    public Integer getPropertyAsInteger(String key, String group) {
        String v = getProperty(key, group);
        return v != null && !v.isEmpty() ? Integer.parseInt(v) : 0;
    }

    @Override
    public Double getPropertyAsDouble(String key, String group) {
        String v = getProperty(key, group);
        return v != null && !v.isEmpty() ? Double.parseDouble(v) : 0.0;
    }

    /**
     * Genera un file name presente nella cartella tmp.folder in configurazione. Effettua append del suffix che può essere un fullpathname da cui estrae il nome cercando l'ultimo /
     * @param suffix
     * @return
     */
    @Override
    public String getTempFileName(String suffix) {
        if( suffix == null ) suffix = "";
        if( suffix.contains("/"))
            suffix = suffix.substring(suffix.lastIndexOf('/')+1);
        return getProperty("tmp.folder", "*","/tmp/arbritage") + "/_tmp_" + java.lang.System.currentTimeMillis() + suffix;
    }

}
