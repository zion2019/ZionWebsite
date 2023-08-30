package com.zion.learning.dao;

import com.zion.common.vo.resource.request.SubPointQO;
import com.zion.learning.model.SubPoint;

import java.util.List;

public interface SubPointDao {
    SubPoint getById(Long id);

    List<SubPoint> condition(SubPointQO qo,String ... includeColumn);

    long conditionCount(SubPointQO qo);

    boolean save(SubPoint SubPoint);

    List<SubPoint> condition(SubPointQO qo);

    SubPoint conditionOne(SubPointQO qo);


    void saveBatch(List<SubPointQO> subPointList);

    long delete(SubPointQO build);
}
