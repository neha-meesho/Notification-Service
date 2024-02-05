package com.example.demo.rest.services;

import com.example.demo.models.EsSmsRequest;
import com.example.demo.repository.EsRepository;


import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service

public class EsService {



    @Autowired
    EsRepository esRepository;


    public void save(EsSmsRequest esModel) {

        try{
            esRepository.save(esModel);
        }
        catch (Exception e){
//            System.out.println(e.getMessage());
        }



    }


    public Page<EsSmsRequest> getSmsByPhoneNumberAndTimeRange(String phoneNumber, long start, long end, PageRequest pageable) {


        return esRepository.findByPhoneNumberAndCreatedAtBetween(phoneNumber, start, end, pageable);
    }

    public Page<EsSmsRequest> getSmsContainingMessage(String searchText, PageRequest pageable) {
        return esRepository.findByMessageContaining(searchText, pageable);
    }

    public Page<EsSmsRequest> getAllSms(Pageable pageable) {
        return esRepository.findAll(pageable);
    }
}