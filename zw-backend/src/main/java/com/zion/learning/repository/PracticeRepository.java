package com.zion.learning.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.mongodb.client.result.UpdateResult;
import com.zion.common.basic.CommonConstant;
import com.zion.common.basic.Page;
import com.zion.common.basic.TotalSize;
import com.zion.common.utils.BaseEntityUtil;
import com.zion.common.vo.learning.response.PractiseVO;
import com.zion.learning.dao.PracticeDao;
import com.zion.learning.model.Practice;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Repository
public class PracticeRepository implements PracticeDao {

    private MongoTemplate mongoTemplate;

    public Practice getById(Long id) {
        return mongoTemplate.findById(id, Practice.class);
    }

    @Override
    public List<Practice> condition(Practice condition) {
        Query query = getQuery(condition);
        return mongoTemplate.find(query, Practice.class);
    }

    @Override
    public Practice conditionOne(Practice condition) {
        Query query = getQuery(condition);
        return mongoTemplate.findOne(query, Practice.class);
    }

    @Override
    public long conditionCount(Practice condition) {
        Query query = getQuery(condition);
        return mongoTemplate.count(query, Practice.class);
    }

    /**
     * generate query by condition
     * @param condition
     * @return
     */
    private Query getQuery(Practice condition) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").is(CommonConstant.DELETED_NO));
        if(condition.getUserId() != null){
            query.addCriteria(Criteria.where("userId").is(condition.getUserId()));
        }
        if(condition.getGtePractiseDate() != null){
            query.addCriteria(Criteria.where("practiseDate").gte(condition.getGtePractiseDate()));
        }
        if(condition.getLtePractiseDate() != null){
            query.addCriteria(Criteria.where("practiseDate").lte(condition.getLtePractiseDate()));
        }
        if(condition.getIncludeFields() != null){
            query.fields().include(condition.getIncludeFields());
        }

        if(CollUtil.isNotEmpty(condition.getTopicIds())){
            query.addCriteria(Criteria.where("topicId").in(condition.getTopicIds()));
        }

        if(condition.getPointId() != null){
            query.addCriteria(Criteria.where("pointId").is(condition.getPointId()));
        }
        if(condition.getTopicId() != null){
            query.addCriteria(Criteria.where("topicId").is(condition.getTopicId()));
        }
        if(condition.getResult() != null){
            query.addCriteria(Criteria.where("result").is(condition.getResult()));
        }

        if(CharSequenceUtil.isNotBlank(condition.getSortFiledName())){
            query.with(Sort.by(condition.getSortDirection(),condition.getSortFiledName()));
        }

        return query;
    }

    @Override
    public boolean save(Practice Practice) {
        BaseEntityUtil.filedBasicInfo(Practice);
        mongoTemplate.save(Practice);
        return true;
    }

    @Override
    public long delete(Practice condition) {
        Update update = new Update().set("deleted", CommonConstant.DELETED_YES);
        UpdateResult result = mongoTemplate.updateMulti(getQuery(condition), update, Practice.class);
        return result.getModifiedCount();
    }

    @Override
    public <E> Page pageQuery(Page page,Class<E> e, Practice condition) {




        return page;
    }

    @Override
    public Page findUndoTopicByDate(Integer pageNo, Integer pageSize, Practice condition) {
        Page page = new Page<>();
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        List<AggregationOperation> basicOperation = new ArrayList<>();
        basicOperation.add(Aggregation.match(Criteria.where("practiseDate").lte(condition.getLtePractiseDate())));
//        basicOperation.add(Aggregation.match(Criteria.where("result").is(condition.getResult())));
        basicOperation.add(Aggregation.match(Criteria.where("deleted").is(CommonConstant.DELETED_NO)));
        basicOperation.add(Aggregation.match(Criteria.where("userId").is(condition.getUserId())));

        // query total
        List<AggregationOperation> countOperation= new ArrayList<>(basicOperation);
        countOperation.add(Aggregation.group("topicId"));
        countOperation.add(Aggregation.count().as("totalSize"));
        AggregationResults<TotalSize> countResult = mongoTemplate.aggregate(Aggregation.newAggregation(Practice.class,countOperation), TotalSize.class);
        TotalSize uniqueMappedResult = countResult.getUniqueMappedResult();
        if(uniqueMappedResult == null){
            page.setTotal(0L);
            return page;
        }
        Long totalSize = countResult.getUniqueMappedResult().getTotalSize();
        if(totalSize <= 0){
            return page;
        }
        page.setTotal(totalSize);

        List<AggregationOperation> dataOperation= new ArrayList<>(basicOperation);
        dataOperation.add(Aggregation.group("topicId").count().as("undoCount").first("topicId").as("topicId"));
        dataOperation.add(Aggregation.sort(Sort.Direction.DESC, "undoCount"));
        dataOperation.add(Aggregation.skip(pageNo * pageSize));
        dataOperation.add(Aggregation.limit(pageSize));
        TypedAggregation<Practice> aggregation = Aggregation.newAggregation(Practice.class, dataOperation);

        AggregationResults<PractiseVO> result = mongoTemplate.aggregate(aggregation, PractiseVO.class);
        List<PractiseVO> resultList = result.getMappedResults();
        page.setDataList(resultList);

        return page;
    }

    @Override
    public Long remove(Practice condition) {
        Update update = new Update().set("deleted", CommonConstant.DELETED_YES);
        update.set("updateTime",new Date());
        Query query = getQuery(condition);
        UpdateResult result = mongoTemplate.updateMulti(query, update, Practice.class);
        return result.getModifiedCount();
    }
}

