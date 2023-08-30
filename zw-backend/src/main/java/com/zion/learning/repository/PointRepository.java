package com.zion.learning.repository;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.mongodb.client.result.UpdateResult;
import com.zion.common.basic.CommonConstant;
import com.zion.common.basic.Page;
import com.zion.common.utils.BaseEntityUtil;
import com.zion.common.vo.learning.request.PointQO;
import com.zion.learning.dao.PointDao;
import com.zion.learning.model.Point;
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
public class PointRepository implements PointDao {

    private MongoTemplate mongoTemplate;

    public Point getById(Long id) {
        return mongoTemplate.findById(id, Point.class);
    }

    @Override
    public List<Point> condition(PointQO qo,String ... includeColumn) {
        Query query = getQuery(qo,includeColumn);
        return mongoTemplate.find(query,Point.class);
    }

    @Override
    public List<Point> condition(PointQO qo) {
        Query query = getQuery(qo);
        return mongoTemplate.find(query,Point.class);
    }

    @Override
    public Point conditionOne(PointQO qo) {

        Query query = getQuery(qo);

        return mongoTemplate.findOne(query,Point.class);
    }

    @Override
    public <E> Page pageQuery(Class<E> e, PointQO pointQO) {
        Page<E> page = new Page<>();
        Query query = getQuery(pointQO);

        //  query total
        long totalCount = mongoTemplate.count(query,Point.class);
        if(totalCount <= 0 ){
            return page;
        }

        // Create a Pageable object for pagination
        Pageable pageable = PageRequest.of(pointQO.getPageNo(), pointQO.getPageSize());
        query.with(pageable);

        // query
        List<Point> points = mongoTemplate.find(query, Point.class);
        page.setPageNo(pointQO.getPageNo());
        page.setPageSize(pointQO.getPageSize());
        page.setDataList(BeanUtil.copyToList(points,e));
        page.setTotal(totalCount);

        return page;
    }

    @Override
    public long delete(PointQO qo) {
        Update update = new Update().set("deleted", CommonConstant.DELETED_YES);
        update.set("updateTime",new Date());

        UpdateResult result = mongoTemplate.updateMulti(getQuery(qo), update, Point.class);
        return result.getModifiedCount();
    }

    @Override
    public long conditionCount(PointQO qo) {
        Query query = getQuery(qo);
        return mongoTemplate.count(query, Point.class);
    }

    /**
     * Override "getQuery" with assign include column
     * @param qo
     * @return
     */
    private Query getQuery(PointQO qo,String ... includeColumn) {
        Query query = getQuery(qo);
        query.fields().include(includeColumn);
        return query;
    }

    /**
     * generate query by condition
     * @param qo
     * @return
     */
    private Query getQuery(PointQO qo) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").is(CommonConstant.DELETED_NO));
        if(CharSequenceUtil.isNotBlank(qo.getTitle())){
            Pattern pattern = Pattern.compile(qo.getTitle(), Pattern.CASE_INSENSITIVE);
            query.addCriteria(Criteria.where("title").regex(pattern));
        }
        if(qo.getTopicId() != null){
            query.addCriteria(Criteria.where("topicId").is(qo.getTopicId()));
        }
        if(qo.getId() != null){
            query.addCriteria(Criteria.where("id").is(qo.getId()));
        }
        return query;
    }

    @Override
    public Point save(Point Point) {
        BaseEntityUtil.filedBasicInfo(Point);
        return mongoTemplate.save(Point);
    }


}
