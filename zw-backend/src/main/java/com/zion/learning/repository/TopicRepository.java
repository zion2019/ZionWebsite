package com.zion.learning.repository;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.mongodb.client.result.UpdateResult;
import com.zion.common.basic.CommonConstant;
import com.zion.common.basic.Page;
import com.zion.common.utils.BaseEntityUtil;
import com.zion.learning.dao.TopicDao;
import com.zion.learning.model.Topic;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@AllArgsConstructor
@Repository
public class TopicRepository implements TopicDao {

    private MongoTemplate mongoTemplate;

    public Topic getById(Long id) {
        return mongoTemplate.findById(id, Topic.class);
    }

    @Override
    public List<Topic> condition(Topic condition) {
        Query query = getQuery(condition);
        return mongoTemplate.find(query,Topic.class);
    }

    @Override
    public Topic conditionOne(Topic condition) {
        Query query = getQuery(condition);
        return mongoTemplate.findOne(query,Topic.class);
    }

    @Override
    public long conditionCount(Topic condition) {
        Query query = getQuery(condition);
        return mongoTemplate.count(query, Topic.class);
    }

    /**
     * generate query by condition
     * @param condition
     * @return
     */
    private Query getQuery(Topic condition) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").is(CommonConstant.DELETED_NO));
        if(condition.getParentId() != null){
            query.addCriteria(Criteria.where("parentId").is(condition.getParentId()));
        }
        if(CharSequenceUtil.isNotBlank(condition.getTitle())){
            Pattern pattern = Pattern.compile(condition.getTitle(), Pattern.CASE_INSENSITIVE);
            query.addCriteria(Criteria.where("title").regex(pattern));
        }
        if(condition.getUserId() != null){
            query.addCriteria(Criteria.where("userId").is(condition.getUserId()));
        }
        if(condition.getId() != null){
            query.addCriteria(Criteria.where("id").is(condition.getId()));
        }
        if(condition.getExcludeId() != null){
            query.addCriteria(Criteria.where("id").ne(condition.getExcludeId()));
        }
        if(condition.getIds() != null){
            query.addCriteria(Criteria.where("id").in(condition.getIds()));
        }
        if(condition.getIncludeFields() != null && condition.getIncludeFields().length > 0){
            query.fields().include(condition.getIncludeFields());
        }
        return query;
    }

    @Override
    public boolean save(Topic topic) {
        BaseEntityUtil.filedBasicInfo(topic);
        mongoTemplate.save(topic);
        return true;
    }

    @Override
    public long delete(Topic condition) {
        Update update = new Update().set("deleted", CommonConstant.DELETED_YES);
        update.set("updateTime",new Date());
        UpdateResult result = mongoTemplate.updateMulti(getQuery(condition), update, Topic.class);
        return result.getModifiedCount();
    }

    @Override
    public <E> Page pageQuery(Page page,Class<E> e, Topic condition) {
        Query query = getQuery(condition);

        //  query total
        long totalCount = mongoTemplate.count(query,Topic.class);
        if(totalCount <= 0 ){
            return page;
        }

        // Create a Pageable object for pagination
        Pageable pageable = PageRequest.of(page.getPageNo(), page.getPageSize());
        query.with(pageable);

        // query
        List<Topic> topics = mongoTemplate.find(query, Topic.class);
        page.setDataList(BeanUtil.copyToList(topics,e));
        page.setTotal(totalCount);

        return page;
    }


}
