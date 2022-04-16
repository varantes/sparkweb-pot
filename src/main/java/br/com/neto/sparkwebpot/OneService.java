package br.com.neto.sparkwebpot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class OneService {

    private static final Logger log = LogManager.getLogger();

    private final ObjectMapper mapper;

    @Inject
    public OneService(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public OneDTO getPerson() {
        log.info("Executing service...");
        OneDTO oneDTO = new OneDTO("val", 55, Lists.newArrayList(new OneDTO("du", 89), new OneDTO("Ivanil", 84)));
        if (log.isInfoEnabled()) {
            log.info(mapper.convertValue(oneDTO, Map.class));
        }
        return oneDTO;
    }

    public String sayHi() {
        return "Oi";
    }
}
