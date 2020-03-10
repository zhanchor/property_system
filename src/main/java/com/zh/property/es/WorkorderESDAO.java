package com.zh.property.es;

import com.zh.property.pojo.Workorder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface WorkorderESDAO extends ElasticsearchRepository<Workorder,Integer> {
}
