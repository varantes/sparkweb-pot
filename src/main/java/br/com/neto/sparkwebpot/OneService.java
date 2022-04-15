package br.com.neto.sparkwebpot;

import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OneService {

    private static final Logger log = LogManager.getLogger();

    public OneDTO getPerson() {
        log.info("Executing service...");
        return new OneDTO("val", 55, Lists.newArrayList(new OneDTO("du", 89), new OneDTO("Ivanil", 84)));
    }
}
