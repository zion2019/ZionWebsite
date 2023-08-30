package com.zion.learning.dao;

import com.zion.common.basic.Page;
import com.zion.learning.model.Practice;

import java.util.List;

public interface PracticeDao {
    Practice getById(Long id);

    long conditionCount(Practice condition);

    boolean save(Practice Practice);

    List<Practice> condition(Practice condition);

    Practice conditionOne(Practice condition);

    long delete(Practice condition);

    <E> Page pageQuery(Page page,Class<E> targetClazz, Practice condition);

    /**
     * get group topic
     * @param pageNo
     * @param pageSize
     * @param build
     * @return
     */
    Page findUndoTopicByDate(Integer pageNo, Integer pageSize, Practice build);

    Long remove(Practice condition);
}
