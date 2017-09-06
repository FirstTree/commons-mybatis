package com.wenyiguai.commons.mybaits;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by tree on 6/9/17.
 */
public abstract class MybatisDao<T> extends BaseDao<T> {

    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.init(sqlSessionFactory);
    }
}
