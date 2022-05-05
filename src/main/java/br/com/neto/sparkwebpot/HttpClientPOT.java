package br.com.neto.sparkwebpot;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class HttpClientPOT {

    private static final Logger log = LogManager.getLogger();

    public static void main(String[] args) {
        log.info("Starting HttpClient POT...");
        test1();
    }

    private static void test1() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet get = new HttpGet("https://httpbin.org/get");
            CloseableHttpResponse response = httpClient.execute(get);
            log.info("response={}", response);
        } catch (IOException e) {
            log.error("", e);
        }
    }
}
