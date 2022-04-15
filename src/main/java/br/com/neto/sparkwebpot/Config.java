package br.com.neto.sparkwebpot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Config extends AbstractModule {

    private static final Logger log = LogManager.getLogger();

    @Override
    protected void configure() {
        log.info("Configuring Guice ...");
        bind(App.class).in(Singleton.class);
    }

    @Provides
    public ObjectMapper configMapper() {
        return new ObjectMapper();
    }
}
