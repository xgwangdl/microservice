package com.accenture.backendservice.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.accenture.backendservice.dto.UserInfo;

public interface UserRepository extends ElasticsearchRepository<UserInfo, Long> {

}
