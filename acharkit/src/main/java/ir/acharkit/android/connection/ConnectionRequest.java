package ir.acharkit.android.connection;

import android.Manifest;
import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.annotation.Size;
import android.util.Pair;

import org.json.JSONObject;

import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import ir.acharkit.android.annotation.RequestMethod;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    11/7/2017
 * Email:   alirezat775@gmail.com
 */

public class ConnectionRequest {

    private static final String TAG = ConnectionRequest.class.getName();
    private final Request request;

    private ConnectionRequest(Request request) {
        this.request = request;
    }

    @RequiresPermission(Manifest.permission.INTERNET)
    public void sendRequest() {
        request.execute();
    }

    public void cancelRequest() {
        request.cancel(true);
    }

    public interface Method {
        String GET = "GET";
        String POST = "POST";
        String PUT = "PUT";
        String DELETE = "DELETE";
        String HEAD = "HEAD";
        String PATCH = "PATCH";
    }

    public static class Builder {

        private final Context context;
        private final String url;
        private final String method;
        private int timeOut;
        private OnRequestListener onRequestListener;
        private Map<String, String> header;
        private String parameters;

        /**
         * @param context
         * @param method
         * @param url
         */
        public Builder(@NonNull Context context, @RequestMethod String method, @Size(min = 3) String url) {
            this.context = context;
            this.method = method;
            this.url = url;
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
         * @param onRequestListener
         * @return
         */
        public Builder setOnRequestListener(OnRequestListener onRequestListener) {
            this.onRequestListener = onRequestListener;
            return this;
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
            if (method.equalsIgnoreCase(Method.GET) || method.equalsIgnoreCase(Method.HEAD)) {
                this.parameters = parameters;
                throw new RuntimeException("The GET / HEAD request does not have a body");
            } else if (method.equalsIgnoreCase(Method.PATCH) || method.equalsIgnoreCase(Method.POST) || method.equalsIgnoreCase(Method.PUT) || method.equalsIgnoreCase(Method.DELETE)) {
                this.parameters = parameters;
            }
            return this;
        }

        public ConnectionRequest build() {
            Request request = new Request();
            request.context = new SoftReference<>(context);
            request.stringUrl = url;
            request.method = method;
            request.header = header;
            request.timeOut = timeOut;
            request.parameters = parameters;
            request.onRequestListener = onRequestListener;
            return new ConnectionRequest(request);
        }
    }

    private static class Request extends AsyncTask<Void, Void, Pair<Boolean, String>> {

        public SoftReference<Context> context;
        private String stringUrl;
        private String method;
        private int timeOut;
        private OnRequestListener onRequestListener;
        private Map<String, String> header;
        private String parameters;
        private HttpURLConnection connection;
        private URL url = null;
        private String responseRequest = "";

        @Override
        protected Pair<Boolean, String> doInBackground(Void... voids) {
            try {
                if (stringUrl.trim().isEmpty()) {
                    throw new MalformedURLException("The entered URL is not valid");
                }
                URL url = new URL(stringUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(method);
                connection.setReadTimeout(timeOut == 0 ? ConnectionUtil.TIME_OUT_CONNECTION : timeOut);
                connection.setConnectTimeout(timeOut == 0 ? ConnectionUtil.TIME_OUT_CONNECTION : timeOut);
                if (method.equals(Method.GET) || method.equals(Method.HEAD)) {
                    connection.setDoOutput(false);
                } else if (method.equalsIgnoreCase(Method.PATCH) || method.equalsIgnoreCase(Method.POST) || method.equalsIgnoreCase(Method.PUT) || method.equalsIgnoreCase(Method.DELETE)) {
                    connection.setDoOutput(true);
                }
                ConnectionUtil.setHeaderParams(connection, header);
                ConnectionUtil.setParams(connection, parameters);
                connection.connect();

                if (connection.getResponseCode() > 400 && connection.getResponseCode() < 600) {
                    connection.disconnect();
                    return new Pair<>(false, connection.getResponseCode() + " : " + connection.getResponseMessage());
                }

                responseRequest = ConnectionUtil.inputStreamToString(connection);
                connection.disconnect();
                return new Pair<>(true, responseRequest);
            } catch (Exception e) {
                if (connection != null) connection.disconnect();
                return new Pair<>(false, e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(Pair<Boolean, String> result) {
            super.onPreExecute();
            if (onRequestListener != null) {
                if (result.first) {
                    onRequestListener.onSuccess(result.second);
                } else {
                    onRequestListener.onError(result.second);
                }
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (connection != null) connection.disconnect();
            if (onRequestListener != null) onRequestListener.onCancel();
        }
    }
}