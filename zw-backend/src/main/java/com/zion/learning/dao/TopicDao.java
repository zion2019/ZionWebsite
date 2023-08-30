package com.zion.learning.dao;

import com.zion.common.basic.Page;
import com.zion.learning.model.Topic;

import java.util.List;

public interface TopicDao {
    Topic getById(Long id);

    long conditionCount(Topic condition);

    boolean save(Topic topic);

    List<Topic> condition(Topic condition);

    Topic conditionOne(Topic condition);

    long delete(Topic condition);

    <E> Page pageQuery(Page page,Class<E> e, Topic condition);

}
