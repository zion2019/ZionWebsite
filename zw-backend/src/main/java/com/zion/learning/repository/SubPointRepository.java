package com.zion.learning.repository;

import cn.hutool.core.bean.BeanUtil;
import com.mongodb.client.result.UpdateResult;
import com.zion.common.basic.CommonConstant;
import com.zion.common.utils.BaseEntityUtil;
import com.zion.common.vo.resource.request.SubPointQO;
import com.zion.learning.dao.SubPointDao;
import com.zion.learning.model.SubPoint;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Repository
public class SubPointRepository implements SubPointDao {

    private MongoTemplate mongoTemplate;

    public SubPoint getById(Long id) {
        return mongoTemplate.findById(id, SubPoint.class);
    }

    @Override
    public List<SubPoint> condition(SubPointQO qo,String ... includeColumn) {
        Query query = getQuery(qo,includeColumn);
        return mongoTemplate.find(query,SubPoint.class);
    }

    @Override
    public List<SubPoint> condition(SubPointQO qo) {
        Query query = getQuery(qo);
        return mongoTemplate.find(query,SubPoint.class);
    }

    @Override
    public SubPoint conditionOne(SubPointQO qo) {
        Query query = getQuery(qo);
        return mongoTemplate.findOne(query,SubPoint.class);
    }

    @Override
    public void saveBatch(List<SubPointQO> subPointList) {
        List<SubPoint> subPoints = BeanUtil.copyToList(subPointList, SubPoint.class);

        // 批量删除
        this.delete(SubPointQO.builder().pointId(subPoints.get(0).getPointId()).build());

        subPoints.forEach(subPoint -> {
            BaseEntityUtil.filedBasicInfo(subPoint);
        });

        mongoTemplate.insert(subPoints,SubPoint.class);

    }

    @Override
    public long delete(SubPointQO qo) {
        Update update = new Update().set("deleted", CommonConstant.DELETED_YES);
        update.set("updateTime",new Date());
        UpdateResult result = mongoTemplate.updateMulti(getQuery(qo), update, SubPoint.class);
        return result.getModifiedCount();
    }

    @Override
    public long conditionCount(SubPointQO qo) {
        Query query = getQuery(qo);
        return mongoTemplate.count(query, SubPoint.class);
    }

    /**
     * Override "getQuery" with assign include column
     * @param qo
     * @return
     */
    private Query getQuery(SubPointQO qo,String ... includeColumn) {
        Query query = getQuery(qo);
        query.fields().include(includeColumn);
        return query;
    }

    /**
     * generate query by condition
     * @param qo
     * @return
     */
    private Query getQuery(SubPointQO qo) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").is(CommonConstant.DELETED_NO));

        if(qo.getPointId() != null){
            query.addCriteria(Criteria.where("pointId").is(qo.getPointId()));
        }

        return query;
    }

    @Override
    public boolean save(SubPoint SubPoint) {
        BaseEntityUtil.filedBasicInfo(SubPoint);
        mongoTemplate.save(SubPoint);
        return true;
    }


}
