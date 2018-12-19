package ir.acharkit.android.connection;

import android.Manifest;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.annotation.Size;

import org.json.JSONObject;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;

import ir.acharkit.android.annotation.RequestMethod;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    11/7/2017
 * Email:   alirezat775@gmail.com
 */
public abstract class ConnectionRequest {
    private static final String TAG = ConnectionRequest.class.getName();

    public interface Method {
        String GET = "GET";
        String POST = "POST";
        String PUT = "PUT";
        String DELETE = "DELETE";
        String HEAD = "HEAD";
        String PATCH = "PATCH";
    }

    public static class Builder {
        private final Request request;
        private Context context;
        private String url;
        private String method;
        private int timeOut;
        private OnRequestListener onRequestListener;
        private Map<String, String> header;
        private String parameters;
        private boolean trust = false;
        private File file;
        private String fileName;

        /**
         * @param context
         * @param method
         * @param url
         */
        public Builder(@NonNull Context context, @RequestMethod String method, @Size(min = 3) String url) {
            this.context = context;
            this.method = method;
            this.url = url;
            request = new Request();
        }

        /**
         * @return
         */
        private Context getContext() {
            return context;
        }

        /**
         * @return
         */
        private String getUrl() {
            return url;
        }

        /**
         * @return
         */
        private String getMethod() {
            return method;
        }

        /**
         * @return
         */
        private int getTimeOut() {
            return timeOut;
        }

        /**
         * @param timeOut
         * @return
         */
        public Builder setTimeOut(int timeOut) {
            this.timeOut = timeOut;
            return this;
        }

        /**
         * @return
         */
        private OnRequestListener getOnRequestListener() {
            return onRequestListener;
        }

        /**
         * @param onRequestListener
         * @return
         */
        public Builder setOnRequestListener(OnRequestListener onRequestListener) {
            this.onRequestListener = onRequestListener;
            return this;
        }

        /**
         * @return
         */
        private Map<String, String> getHeader() {
            return header;
        }

        /**
         * @param header
         * @return
         */
        public Builder setHeader(Map<String, String> header) {
            this.header = header;
            return this;
        }

        /**
         * @return
         */
        private String getParameters() {
            return parameters;
        }

        /**
         * @param parameters
         * @return
         */
        public Builder setParameters(JSONObject parameters) {
            setParameters(parameters.toString());
            return this;
        }

        /**
         * @param parameters
         * @return
         */
        public Builder setParameters(String parameters) {
            if (getMethod().equalsIgnoreCase(Method.GET) || getMethod().equalsIgnoreCase(Method.HEAD)) {
                this.parameters = parameters;
                throw new RuntimeException("The GET / HEAD request does not have a body");
            } else if (getMethod().equalsIgnoreCase(Method.PATCH) || getMethod().equalsIgnoreCase(Method.POST) || getMethod().equalsIgnoreCase(Method.PUT) || getMethod().equalsIgnoreCase(Method.DELETE)) {
                this.parameters = parameters;
            }
            return this;
        }

        /**
         * @return
         */
        @RequiresPermission(Manifest.permission.INTERNET)
        public Builder sendRequest() {
            request.execute();
            return this;
        }

        /**
         * @return
         */
        public Builder cancelRequest() {
            request.cancel(true);
            return this;
        }

        /**
         * @param trust
         * @return
         */
        @Deprecated
        public Builder trustSSL(boolean trust) {
            return this;
        }

        /**
         * @return
         */
        @Deprecated
        private boolean isTrust() {
            return false;
        }

        private class Request extends AsyncTask<Void, Void, Void> {

            private HttpURLConnection connection;
            private URL url = null;
            private String responseRequest = "";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    if (getUrl().trim().isEmpty()) {
                        throw new MalformedURLException("The entered URL is not valid");
                    }
                    url = new URL(getUrl());
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod(getMethod());
                    connection.setReadTimeout(getTimeOut() == 0 ? ConnectionUtil.TIME_OUT_CONNECTION : getTimeOut());
                    connection.setConnectTimeout(getTimeOut() == 0 ? ConnectionUtil.TIME_OUT_CONNECTION : getTimeOut());
                    if (getMethod().equals(Method.GET) || getMethod().equals(Method.HEAD)) {
                        connection.setDoOutput(false);
                    } else if (getMethod().equalsIgnoreCase(Method.PATCH) || getMethod().equalsIgnoreCase(Method.POST) || getMethod().equalsIgnoreCase(Method.PUT) || getMethod().equalsIgnoreCase(Method.DELETE)) {
                        connection.setDoOutput(true);
                    }

                    ConnectionUtil.setHeaderParams(connection, getHeader());
                    ConnectionUtil.setParams(connection, getParameters());

                    connection.connect();

                    if (connection.getResponseCode() > 400 && connection.getResponseCode() < 600) {
                        if (getOnRequestListener() != null)
                            getOnRequestListener().error(connection.getResponseCode() + " : " + connection.getResponseMessage());
                        connection.disconnect();
                        return null;
                    }

                    responseRequest = ConnectionUtil.inputStreamToString(connection);

                } catch (Exception e) {
                    if (e instanceof ProtocolException || e instanceof MalformedURLException) {
                        throw new RuntimeException("The entered protocol is not valid");
                    } else if (e instanceof SSLHandshakeException) {
                        throw new RuntimeException("Trust anchor for certification path not found");
                    }

                    if (getOnRequestListener() != null)
                        getOnRequestListener().error(String.valueOf(e));

                    connection.disconnect();
                }
                connection.disconnect();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (responseRequest != null) {
                    if (getOnRequestListener() != null)
                        getOnRequestListener().success(responseRequest);
                }
            }

            @Override
            protected void onCancelled(Void aVoid) {
                super.onCancelled(aVoid);
                if (connection != null)
                    connection.disconnect();
                if (getOnRequestListener() != null) {
                    getOnRequestListener().error("Request cancelled");
                }
            }
        }
    }
}