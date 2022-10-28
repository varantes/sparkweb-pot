package br.com.neto.sparkwebpot;

import com.google.inject.Guice;
import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Timer;
import spark.Spark;

import static spark.Spark.port;

public class App {

    private static final Logger log = LogManager.getLogger();
    private static Timer timer;

    private final Router router;

    @Inject
    public App(Router router) {
        this.router = router;
    }

    public static void main(String[] args) {
        log.info("Starting...");

        timer = new Timer("App.main");
        timer.start();

        Guice.createInjector(new Config())
                .getInstance(App.class)
                .run(8080);
    }

    public void run(int port) {

        log.info("Executing Spark.port({})", port);
        port(port);

        log.info("Executing router.configure()");
        router.configure();

        log.info("Executing Spark.awaitInitialization()");
        Spark.awaitInitialization();

        timer.stop();

        log.info("Started after {} ms", timer.getElapsedTime());
    }

}
