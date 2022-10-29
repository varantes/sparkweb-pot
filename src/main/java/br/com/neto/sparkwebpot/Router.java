package br.com.neto.sparkwebpot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static spark.Spark.*;

public class Router {

    private static final Logger log = LogManager.getLogger();
    private static ThreadLocal<Long> longThreadLocal = new ThreadLocal<>();
    private static AtomicLong atomicLong = new AtomicLong(0);

    private final OneService oneService;
    private final ObjectMapper mapper;

    @Inject
    public Router(OneService oneService, ObjectMapper mapper) {
        this.oneService = oneService;
        this.mapper = mapper;
    }

    public void configure() {
        before("/*", (req, res) -> {
            longThreadLocal.set(System.nanoTime());
            log.info("REQUEST {}: {}", req.requestMethod(), req.uri());
        });

        get("/", (req, res) -> "Working" );
        get("/person", (req, res) -> mapper.writeValueAsString(oneService.getPerson()) );
        get("/hi", (req, res) -> oneService.sayHi());

        after("/*", (req, res) -> {
            Object respBody;
            try {
                respBody = mapper.readValue(res.body(), Map.class);
            } catch (Exception e) {
                respBody = res.body();
            }
            log.info("RESPONSE body={} [after {} ms]", respBody, ((double)(System.nanoTime() - longThreadLocal.get())) / 1000_000);
            longThreadLocal.remove();
        });
    }
}
