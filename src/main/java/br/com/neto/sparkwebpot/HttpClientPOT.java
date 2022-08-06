package br.com.neto.sparkwebpot;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class HttpClientPOT {

    private static final Logger log = LogManager.getLogger();

    public static void main(String[] args) {
        log.info("Starting HttpClient POT...");
        test2();
    }

    @SuppressWarnings("unused")
    private static void test1() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet get = new HttpGet("https://httpbin.org/get");
            CloseableHttpResponse response = httpClient.execute(get);
            log.info("response={}", response);
        } catch (IOException e) {
            log.error("", e);
        }
    }

    private static void test2() {
        try {
            String badssl = "https://self-signed.badssl.com/";
            SocketFactoryConfiguration.configureBlindSocketFactory();
            URL url = new URL(badssl);
            byte[] bytes;
            try (InputStream inputStream = url.openStream()) {
                bytes = inputStream.readAllBytes();
            }
            log.info("bytes.length = {}", bytes.length);

            HttpGet get = new HttpGet(badssl);
            try (CloseableHttpResponse response =
                         HttpClientBuilder.create()
                                 .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                                 .build()
                                 .execute(get)) {
                System.out.printf("response.getStatusLine()=%s", response.getStatusLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
