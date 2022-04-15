package br.com.neto.sparkwebpot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static spark.Spark.*;

public class Router {

    private static final Logger log = LogManager.getLogger();
    private final OneService oneService;
    private final ObjectMapper mapper;

    @Inject
    public Router(OneService oneService, ObjectMapper mapper) {
        this.oneService = oneService;
        this.mapper = mapper;
    }

    public void configure() {
        before("/*", (req, res) -> log.info("[{}] {}: {}", this, req.requestMethod(), req.uri()));

        get("/", (req, res) -> "Working" );
        get("/person", (req, res) -> mapper.writeValueAsString(oneService.getPerson()) );

        after("/*", (req, res) -> log.info(res.body()) );
    }
}
