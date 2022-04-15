package br.com.neto.sparkwebpot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static spark.Spark.*;

public class Router {

    private static final Logger log = LogManager.getLogger();

    public void configure() {
        before("/*", (req, res) -> log.info("[{}] {}: {}", this, req.requestMethod(), req.uri()));

        get("/", (req, res) -> "Working" );

        after("/*", (req, res) -> log.info(res.body()));
    }
}
