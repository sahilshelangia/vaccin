package com.covid.vaccine.service;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


@Service
public class Telegram {
    @Value("${telegram.chat.id}")
    private String CHAT_ID;
    @Value("${telegram.token}")
    private String TOKEN;

    private static final String BASEURL = "https://api.telegram.org/bot";
    private static final String TEXT_FIELD = "text";
    private static final String CHATID_FIELD = "chat_id";

    public void sendMessage(String msg) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        String url = BASEURL + TOKEN + "/sendmessage";
        HttpPost httppost = new HttpPost(url);
        httppost.addHeader("Content-type", "application/x-www-form-urlencoded");
        httppost.addHeader("charset", "UTF-8");
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair(CHATID_FIELD, CHAT_ID));
        nameValuePairs.add(new BasicNameValuePair(TEXT_FIELD, msg));
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
        HttpResponse response = httpClient.execute(httppost);
    }
}
