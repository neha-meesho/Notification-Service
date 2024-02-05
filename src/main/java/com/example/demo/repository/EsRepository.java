package com.example.demo.repository;



import com.example.demo.models.EsSmsRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Repository
public interface EsRepository extends ElasticsearchRepository<EsSmsRequest,String> {



    Page<EsSmsRequest> findByPhoneNumberAndCreatedAtBetween(String phoneNumber, long start, long end, PageRequest pageable);


    Page<EsSmsRequest> findByMessageContaining(String searchText, PageRequest pageable);

    void save(EsSmsRequest esModel);
//    void save(EsSmsRequest entity);

}
