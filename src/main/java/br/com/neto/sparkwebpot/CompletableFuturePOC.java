package br.com.neto.sparkwebpot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CompletableFuturePOC {

    private final static Logger log = LogManager.getLogger();
    private String result;
    private CompletableFuture<Void> heavyOperationFuture;

    public static void main(String[] args) {
        CompletableFuturePOC completableFuturePOC = new CompletableFuturePOC();
        completableFuturePOC.execute().sleep(5000);
    }

    public String getResult() {
        try {
            log.info("Executing getResult(). Waiting up to 5 sec for the result.");
            heavyOperationFuture.get(5000, TimeUnit.MILLISECONDS);
            return result;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    private CompletableFuturePOC execute() {
        log.info("Executing execute() ...");
        long initTime = System.currentTimeMillis();
        try {

            heavyOperationFuture = CompletableFuture.runAsync(this::heavyOperationWithException);
            heavyOperationFuture.handle((result, exception) -> {
                log.info("result={}, exception={}", result, exception);
                return null;
            });

            log.info("result = {}", getResult());
        } catch (RuntimeException e) {
            log.error("Error in execute() method", e);
        } finally {
            log.info("execute() finished after {} msec", System.currentTimeMillis() - initTime);
        }
        return this;
    }

    private void heavyOperation() {
        try {
            int sleepingDuration = 3000;
            log.info("Sleeping for {} msec ...", sleepingDuration);
            Thread.sleep(sleepingDuration);
            result = "Olá!";
            log.info("Woke up!");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void heavyOperationWithException() {
        try {
            int sleepingDuration = 3000;
            log.info("Sleeping for {} msec ...", sleepingDuration);
            Thread.sleep(sleepingDuration);
            log.info("Woke up!");
            throw new RuntimeException("Erro forçado");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void sleep(long duration) {
        try {
            log.info("Sleeping for {} msec ...", duration);
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
