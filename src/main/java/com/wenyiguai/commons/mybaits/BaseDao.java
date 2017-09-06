package com.wenyiguai.commons.mybaits;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tree on 6/5/17.
 */
public abstract class BaseDao<T> extends SqlSessionDaoSupport {

    protected static final String CREATE = "create";
    protected static final String CREATES = "creates";
    protected static final String DELETE = "delete";
    protected static final String DELETES = "deletes";
    protected static final String UPDATE = "update";
    protected static final String LOAD = "load";
    protected static final String LOADS = "loads";
    protected static final String LIST = "list";
    protected static final String COUNT = "count";
    protected static final String PAGING = "paging";
    public final String nameSpace;

    private final static List EMPTYLIST= Lists.newArrayList();

    public BaseDao() {
        if(this.getClass().getGenericSuperclass() instanceof ParameterizedType) {
            this.nameSpace = ((Class)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]).getSimpleName();
        } else {
            this.nameSpace = ((Class)((ParameterizedType)this.getClass().getSuperclass().getGenericSuperclass()).getActualTypeArguments()[0]).getSimpleName();
        }
    }

    public void init (SqlSessionFactory factory) {
        super.setSqlSessionFactory(factory);
    }

    public abstract void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory);


//    @Autowired(
//            required = false
//    )
//    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
//        super.setSqlSessionTemplate(sqlSessionTemplate);
//    }

    public Boolean create(T t) {
        return Boolean.valueOf(this.getSqlSession().insert(this.sqlId("create"), t) == 1);
    }

    public Integer creates(List<T> ts) {
        return Integer.valueOf(this.getSqlSession().insert(this.sqlId("creates"), ts));
    }

    public Integer creates(T t0, T t1, T... tn) {
        return Integer.valueOf(this.getSqlSession().insert(this.sqlId("creates"), Arrays.asList(new Object[]{t0,t1, tn})));
    }

    public Boolean delete(Long id) {
        return Boolean.valueOf(this.getSqlSession().delete(this.sqlId("delete"), id) == 1);
    }

    public Integer deletes(List<Long> ids) {
        return Integer.valueOf(this.getSqlSession().delete(this.sqlId("deletes"), ids));
    }

    public Integer deletes(Long id0, Long id1, Long... idn) {
        return Integer.valueOf(this.getSqlSession().delete(this.sqlId("deletes"),Arrays.asList(new Serializable[]{id0,id1, idn})));
    }

    public Boolean update(T t) {
        return Boolean.valueOf(this.getSqlSession().update(this.sqlId("update"),t) == 1);
    }

    public T load(Integer id) {
        return this.load(Long.valueOf((long)id.intValue()));
    }

    public T load(Long id) {
        return this.getSqlSession().selectOne(this.sqlId("load"), id);
    }

    public List<T> loads(List<Long> ids) {
        return (List<T>) (ids.isEmpty()? EMPTYLIST:this.getSqlSession().selectList(this.sqlId("loads"), ids));
    }

    public List<T> loads(Long id0, Long id1, Long... idn) {
        return this.getSqlSession().selectList(this.sqlId("loads"), Arrays.asList(new Serializable[]{id0, id1, idn}));
    }

    public List<T> listAll() {
        return this.list((T) null);
    }

    public List<T> list(T t) {
        return this.getSqlSession().selectList(this.sqlId("list"), t);
    }

    public List<T> list(Map<?, ?> criteria) {
        return this.getSqlSession().selectList(this.sqlId("list"), criteria);
    }

    public Paging<T> paging(Integer offset, Integer limit) {
        return this.paging(offset, limit, (Map)(new HashMap()));
    }

//    public Paging<T> paging(Integer offset, Integer limit, T criteria) {
//        HashMap params = Maps.newHashMap();
//        if(criteria != null) {
//            Map total = Json.nonEmpty.convertValue(criteria, Map.class);
//            params.putAll(total);
//        }
//
//        Long total1 = (Long)this.getSqlSession().selectOne(this.sqlId("count"), criteria);
//        if(total1.longValue() <= 0L) {
//            return new Paging(Long.valueOf(0L), EMPTYLIST);
//        } else {
//            params.put("offset", offset);
//            params.put("limit", limit);
//            List datas = this.getSqlSession().selectList(this.sqlId("paging"), params);
//            return new Paging(total1, datas);
//        }
//    }

    public Paging<T> paging(Integer offset, Integer limit, Map<String, Object> criteria) {
        if(criteria == null) {
            criteria = Maps.newHashMap();
        }

        Long total = (Long)this.getSqlSession().selectOne(this.sqlId("count"), criteria);
        if(total.longValue() <= 0L) {
            return new Paging(Long.valueOf(0L), EMPTYLIST);
        } else {
            ((Map)criteria).put("offset", offset);
            ((Map)criteria).put("limit", limit);
            List datas = this.getSqlSession().selectList(this.sqlId("paging"), criteria);
            return new Paging(total, datas);
        }
    }

    public Paging<T> paging(Map<String, Object> criteria) {
        if(criteria == null) {
            criteria = Maps.newHashMap();
        }

        Long total = (Long)this.getSqlSession().selectOne(this.sqlId("count"), criteria);
        if(total.longValue() <= 0L) {
            return new Paging(Long.valueOf(0L), EMPTYLIST);
        } else {
            List datas = this.getSqlSession().selectList(this.sqlId("paging"), criteria);
            return new Paging(total, datas);
        }
    }

    protected String sqlId(String id) {
        return this.nameSpace + "." + id;
    }


}
