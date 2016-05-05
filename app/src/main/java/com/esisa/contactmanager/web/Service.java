package com.esisa.contactmanager.web;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Service extends AsyncTask<String, Integer, JSONObject> {
        private ProgressDialog pDialog;
        private Hashtable<String, String> keys;
        private Context context;
        public Service(Context context,Hashtable<String, String> keys) {
            this.keys = keys;
            this.context = context;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            String s = postData();
            try {
                return new JSONObject(s);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Chargement, Veuillez patienter ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected void onPostExecute(JSONObject result) {
            pDialog.dismiss();
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        public String postData() {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://192.168.56.1:8080/JsonServer/Controller");
            String origresponseText = "";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            for (String key : keys.keySet()) {
                nameValuePairs.add(new BasicNameValuePair(key, keys.get(key)));
            }
            HttpResponse response = null;
            try {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response = httpclient.execute(httppost);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            HttpEntity rp = response.getEntity();
            origresponseText = readContent(response);

            String responseText = origresponseText.substring(7,
                    origresponseText.length());
            return responseText;
        }

        private String readContent(HttpResponse response) {
            String text = "";
            InputStream in = null;

            try {
                in = response.getEntity().getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                text = sb.toString();
            }
            catch (IllegalStateException e) {
                e.printStackTrace();

            }
            catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                }
                catch (Exception ex) {
                }
            }

            return text;
        }
    }