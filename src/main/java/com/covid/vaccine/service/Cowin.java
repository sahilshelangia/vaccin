package com.covid.vaccine.service;

import com.covid.vaccine.dto.VaccineTelegramDTO;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
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

import static com.sun.tools.internal.xjc.model.CBuiltinLeafInfo.TOKEN;

@Service
public class Cowin {
    @Value("${interested.district.ids}")
    private String[] district_ids;

    @Value("${today.date}")
    private String date;

    @Value("${get.by.district}")
    private String BASEURL;

    public List<VaccineTelegramDTO> findByDistrict() throws IOException, URISyntaxException {
        List<VaccineTelegramDTO> vaccineTelegramDTOS = new ArrayList<>();
        Arrays.stream(district_ids).forEach(district_id->{
            try {
                HttpResponse response = httpGetRequest(district_id);
                JSONObject jsonObject = new JSONObject(EntityUtils.toString(response.getEntity()));
                ArrayList<HashMap> centers = (ArrayList<HashMap>) jsonObject.toMap().get("centers");

                centers.forEach(center -> {
                    ArrayList sessions = (ArrayList) ((HashMap) center).get("sessions");
                    ((ArrayList) center.get("sessions")).forEach(session -> {
                        Integer age_limit = (Integer) ((HashMap) sessions.get(0)).get("min_age_limit");
                        Integer available_capacity = (Integer) ((HashMap) session).get("available_capacity_dose1");
                        if (age_limit == 18 && available_capacity > 0) {
                            VaccineTelegramDTO vaccineTelegramDTO = VaccineTelegramDTO.builder()
                                    .address((String) ((HashMap) center).get("address"))
                                    .capacity(String.valueOf(available_capacity))
                                    .pinCode(String.valueOf(((HashMap) center).get("pincode")))
                                    .district((String) ((HashMap) center).get("district_name"))
                                    .build();
                            System.out.println(vaccineTelegramDTO.toString());
                            vaccineTelegramDTOS.add(vaccineTelegramDTO);
                        }
                    });
                });
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return vaccineTelegramDTOS;
    }

    public HttpResponse httpGetRequest(String district_id) throws URISyntaxException, IOException {
        HttpClient httpClient = HttpClients.createDefault();
        URIBuilder builder = new URIBuilder(BASEURL);
        builder.setParameter("district_id", district_id)
                .setParameter("date", date);
        HttpGet httpGet = new HttpGet(builder.build());
        httpGet.setHeader("Content-Type", "application/json; charset=utf-8");
        httpGet.setHeader("Access-Control-Allow-Origin", "*");
        httpGet.setHeader("Connection", "keep-alive");
        httpGet.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        httpGet.setHeader("origin", "https://apisetu.gov.in");
        httpGet.setHeader("referer", "https://apisetu.gov.in/public/marketplace/api/cowin");
        httpGet.setHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36");
        return httpClient.execute(httpGet);
    }
}
