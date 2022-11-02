package br.com.neto.sparkwebpot;

import br.com.neto.sparkwebpot.guiceaddons.MethodCalledMatcher;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class Config extends AbstractModule {

    private static final Logger log = LogManager.getLogger();

    @Override
    protected void configure() {
        log.info("Configuring Guice ...");
        bind(App.class).in(Singleton.class);

        bindInterceptor(
                Matchers.subclassesOf(OneService.class),
                MethodCalledMatcher.methodCalled("sayHi"),
                invocation -> {
                    log.info("invocation={}",
                            Map.of("method", invocation.getMethod().getName(),
                                    "arguments", invocation.getArguments()));
                    long initTime = System.currentTimeMillis();
                    try {
                        return invocation.proceed();
                    } finally {
                        log.info("sayHi() executed in {} ms", System.currentTimeMillis() - initTime);
                    }
                });
    }

    @Provides
    public ObjectMapper configMapper() {
        return new ObjectMapper();
    }
}
