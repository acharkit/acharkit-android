package ir.acharkit.android.connection;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import ir.acharkit.android.util.Log;
import ir.acharkit.android.util.helper.DateTimeHelper;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    11/9/2017
 * Email:   alirezat775@gmail.com
 */
public class ConnectionUtil {
    public static final int TIME_OUT_CONNECTION = 60 * 1000;
    public static final String boundary = DateTimeHelper.currentDateTime("UTC");
    public static final String DEFAULT_PARAMS_ENCODING = "UTF-8";

    // always verify the host - dont check for certificate
    public final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
    private static final String TAG = ConnectionUtil.class.getName();
    private static final String LINE_FEED = "\r\n"; //;
    private static PrintWriter writer;

    /**
     * @param connection
     * @param header
     * @throws UnsupportedEncodingException
     */
    public static void setHeaderParams(HttpURLConnection connection, Map<String, String> header) throws UnsupportedEncodingException {
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");

        connection.setRequestProperty("Accept-Charset", DEFAULT_PARAMS_ENCODING);
        if (header != null) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * @param connection
     * @return
     */
    public static String inputStreamToString(HttpURLConnection connection) {
        String result = null;
        StringBuffer sb = new StringBuffer();
        InputStream is = null;
        try {
            is = new BufferedInputStream(connection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            result = sb.toString();
        } catch (Exception e) {
            if (e instanceof FileNotFoundException) {
                return null;
            }
            Log.w(TAG, e);
            result = null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.w(TAG, e);
                }
            }
        }
        return result;
    }


    /**
     * @param connection
     * @param parameters
     * @throws IOException
     */
    public static void setParams(HttpURLConnection connection, String parameters) throws IOException {
        if (parameters != null) {
            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, DEFAULT_PARAMS_ENCODING));
            writer.write(parameters);
            writer.flush();
            writer.close();
            outputStream.close();
        }
    }

    /**
     * Trust every server - do not check for any certificate
     */
    public static void trustHosts() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }

    public static void copyStream(InputStream input, OutputStream output) {
        byte[] buffer = new byte[1024]; // Adjust if you want
        int bytesRead;
        try {
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            Log.w(TAG, e);
        }
    }
}
