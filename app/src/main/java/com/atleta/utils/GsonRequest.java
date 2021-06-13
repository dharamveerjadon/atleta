package com.atleta.utils;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created on 7/13/17.
 */

public class GsonRequest<T> extends JsonRequest<T> {
    private final Gson gson = new Gson();
    private final Class<T> clazz;

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param method      the HTTP method to use
     * @param url         URL to fetch the JSON from
     * @param jsonRequest A {@link JSONObject} to post with the request. Null is allowed and
     *                    indicates no parameters will be posted along with request.
     * @param clazz       Relevant class object, for Gson's reflection
     */
    public GsonRequest(int method, String url, JSONObject jsonRequest, Class<T> clazz,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener,
                errorListener);
        this.clazz = clazz;
    }
    
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(
                    gson.fromJson(json, clazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}