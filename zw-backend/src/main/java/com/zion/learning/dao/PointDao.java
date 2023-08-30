package com.zion.learning.dao;

import com.zion.common.basic.Page;
import com.zion.common.vo.learning.request.PointQO;
import com.zion.learning.model.Point;

import java.util.List;

public interface PointDao {
    Point getById(Long id);

    List<Point> condition(PointQO qo,String ... includeColumn);

    long conditionCount(PointQO qo);

    Point save(Point Point);

    List<Point> condition(PointQO qo);

    Point conditionOne(PointQO qo);

    <E> Page pageQuery(Class<E> e, PointQO pointQO);

    long delete(PointQO build);
}
