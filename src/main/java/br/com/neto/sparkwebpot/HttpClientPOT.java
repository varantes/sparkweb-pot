package br.com.neto.sparkwebpot;

import com.google.common.collect.Lists;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class HttpClientPOT {

    private static final Logger log = LogManager.getLogger();
    private static final String BAD_SSL = "https://self-signed.badssl.com/";
    private static URL url;

    public static void main(String[] args) throws MalformedURLException {
        log.info("Starting HttpClient POT...");
        url = new URL(BAD_SSL);
        test2();
    }

    private static void test2() {
        try {
            ExecutorService threadPool = Executors.newFixedThreadPool(10000);

            ArrayList<Integer> totalExecutionsList =
                    Lists.newArrayList(1/*, 20, 50, 100, 200, 500, 1000*/);

            totalExecutionsList.forEach(totalExecutions ->
                    IntStream.rangeClosed(1, 3).forEach(repeatCounter ->
                            buildAndExecuteAsync(threadPool, totalExecutions)));

            threadPool.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void buildAndExecuteAsync(ExecutorService threadPool, Integer totalExecutions) {
        long initTime = System.currentTimeMillis();

        List<CompletableFuture<Void>> futures = IntStream.rangeClosed(1, totalExecutions).mapToObj(i ->
                CompletableFuture.runAsync(HttpClientPOT::buildGetAndExecute, threadPool)
        ).toList();

        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[]{})).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        log.info("{} executions in {} msec", totalExecutions, System.currentTimeMillis() - initTime);
    }

    private static CloseableHttpClient buildHttpClient() {
        try {
            return HttpClientBuilder.create()
                    .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            throw new RuntimeException(e);
        }
    }

    private static void buildGetAndExecute() {
        try (CloseableHttpClient httpClient = buildHttpClient()) {
            HttpGet get = new HttpGet(BAD_SSL);
            execGetOnly(httpClient, get);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void execGetOnly(CloseableHttpClient httpClient, HttpGet get) {
        try (CloseableHttpResponse response = httpClient.execute(get)) {
            assert response.getStatusLine().getStatusCode() == 200;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
