package com.covid.vaccine.service;

import com.covid.vaccine.dto.VaccineTelegramDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class Bot {
    @Autowired
    Cowin cowin;

    @Autowired
    Telegram telegram;
    public void run() throws InterruptedException, IOException, URISyntaxException {
        while(true){
            List<VaccineTelegramDTO>vaccineTelegramDTOS=cowin.findByDistrict();
            if(vaccineTelegramDTOS.size()>0){
               String message="";
               for(int i=0;i<vaccineTelegramDTOS.size();++i){
                   message+=vaccineTelegramDTOS.get(i).toString();
               }
               telegram.sendMessage(message);
            }
            Thread.sleep(10000);
        }
    }
}
