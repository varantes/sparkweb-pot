package br.com.neto.sparkwebpot;

import com.google.inject.Guice;
import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static spark.Spark.*;

public class App {

    private static final Logger log = LogManager.getLogger();

    private final Router router;

    @Inject
    public App(Router router) {
        this.router = router;
    }

    public static void main(String[] args) {
        log.info("Iniciando...");

        Guice.createInjector(new Config())
                .getInstance(App.class)
                .run(8080);
    }

    public void run(int port) {

        port(port);

        router.configure();
    }

}
