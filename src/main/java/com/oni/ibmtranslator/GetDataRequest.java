package com.oni.ibmtranslator;

import android.content.Context;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GetDataRequest {

    private Context context;
    private OnGetResultsListener listener;

    public GetDataRequest(Context context){
        this.context = context;
    }
    public void JsonRequest(final int method, final Context context, final String url, final JSONObject object, final int request_code){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(method,
                url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            listener.onGetResultsData(response, request_code);
                        }catch (Exception e){
                            listener.onGetResultsData(null, request_code);
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onGetResultsData(null, request_code);
                VolleyLog.e("JSONPost", "Error: " + error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers =  new HashMap<String, String>();
                String credentials = "apikey:" + "wfn2Cg2yvc5xRYCW68TdJjWq8L4BwiwfEUMOnJXhnY21";
                String encoded = "Basic "+ Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT);
                headers.put("Authorization", encoded);
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };


        ;
        requestQueue.add(jsonObjReq);
    }
    public void onGetResultsData(OnGetResultsListener listener) {
        this.listener = listener;
    }
    public interface OnGetResultsListener {
        public void onGetResultsData(JSONObject object, int request_code);
    }
}
