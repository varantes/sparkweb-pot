package br.com.neto.sparkwebpot;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class SocketFactoryConfiguration {

    private static class BlindTrustManager implements X509TrustManager {

        public static final String GET_ACCEPTED_ISSUERS = "*** getAcceptedIssuers";

        public X509Certificate[] getAcceptedIssuers() {
            System.out.println(GET_ACCEPTED_ISSUERS);
            return new X509Certificate[]{};
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) {
            System.out.println(GET_ACCEPTED_ISSUERS);
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) {
            System.out.println(GET_ACCEPTED_ISSUERS);
        }
    }

    public static void configureBlindSocketFactory() throws KeyManagementException, NoSuchAlgorithmException {
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, new TrustManager[]{new BlindTrustManager()}, null);
        SSLContext.setDefault(ctx);
    }
}
