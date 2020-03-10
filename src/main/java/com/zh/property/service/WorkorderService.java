package com.zh.property.service;

import com.zh.property.dao.WorkorderDAO;
import com.zh.property.es.WorkorderESDAO;
import com.zh.property.pojo.OrderItem;
import com.zh.property.pojo.User;
import com.zh.property.pojo.Workorder;
import com.zh.property.util.Page4Navigator;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "workorders")
public class WorkorderService {
    public static final String waitReceive="waitReceive";
    public static final String waitFix="waitFix";
    public static final String waitFinish="waitFinish";
    public static final String finish="finish";

    @Autowired WorkorderDAO workorderDAO;
    @Autowired WorkorderESDAO workorderESDAO;

    @Cacheable(key="'workorders-page-'+#p0+'-'+#p1")
    public Page4Navigator<Workorder> list(int start,int size,int navigatePages){
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        Pageable pageable=new PageRequest(start,size,sort);
        Page pageFromJPA=workorderDAO.findAll(pageable);
        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }

    @CacheEvict(allEntries=true)
    public void add(Workorder bean){
        workorderDAO.save(bean);
        workorderESDAO.save(bean);
    }

    @Cacheable(key="'workorders-uid-'+#p0.id")
    public List<Workorder> listByUser(User user){
        return workorderDAO.findByUserOrderByIdDesc(user);
    }

    @Cacheable(key="'workorders-one-'+#p0")
    public Workorder get(int id){
        return workorderDAO.findOne(id);
    }

    @CacheEvict(allEntries=true)
    public void update(Workorder bean){
        workorderDAO.save(bean);
        workorderESDAO.save(bean);
    }

    public void removeWorkorderFromOrderItem(List<Workorder> workorders){
        for(Workorder workorder:workorders){
            removeWorkorderFromOrderItem(workorder);
        }
    }

    public void removeWorkorderFromOrderItem(Workorder workorder){
        List<OrderItem> orderItems=workorder.getOrderItems();
        for(OrderItem orderItem:orderItems){
            orderItem.setWorkorder(null);
        }
    }

    public List<Workorder> search(String keyword,int start,int size){
        initDatabase2ES();
//        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery()
//                .add(QueryBuilders.wildcardQuery("orderCode", keyword+"*"),
//                        ScoreFunctionBuilders.weightFactorFunction(100))
//                .scoreMode("sum")
//                .setMinScore(10);

        WildcardQueryBuilder queryBuilder1 = QueryBuilders.wildcardQuery(
                "orderCode",keyword+"*");//搜索名字中含有jack的文档
        MatchQueryBuilder queryBuilder2 = QueryBuilders.matchPhraseQuery("customer.name",keyword);//搜索interest中含有read的文档

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//name中含有jack或者interest含有read，相当于or
        boolQueryBuilder.should(queryBuilder1);
        boolQueryBuilder.should(queryBuilder2);

        Sort sort  = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(start, size,sort);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(boolQueryBuilder).build();


        Page<Workorder> page = workorderESDAO.search(searchQuery);
        System.out.println(page.getContent());
        return page.getContent();
    }

    private void initDatabase2ES(){
        Pageable pageable=new PageRequest(0,5);
        Page<Workorder> page=workorderESDAO.findAll(pageable);
        if(page.getContent().isEmpty()){
            List<Workorder> workorders=workorderDAO.findAll();
            for(Workorder workorder:workorders){
                workorderESDAO.save(workorder);
            }
        }
    }
}


