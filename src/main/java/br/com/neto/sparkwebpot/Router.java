package br.com.neto.sparkwebpot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

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
        before("/*", (req, res) -> log.info("{}: {}", req.requestMethod(), req.uri()));

        get("/", (req, res) -> "Working" );
        get("/person", (req, res) -> mapper.writeValueAsString(oneService.getPerson()) );
        get("/hi", (req, res) -> oneService.sayHi());

        after("/*", (req, res) -> {
            try {
                log.info(mapper.readValue(res.body(), Map.class));
            } catch (Exception e) {
                log.info(res.body());
            }
        });
    }
}
