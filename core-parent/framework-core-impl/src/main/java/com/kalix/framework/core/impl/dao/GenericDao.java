package com.kalix.framework.core.impl.dao;


import com.kalix.framework.core.api.annotation.Relation;
import com.kalix.framework.core.api.annotation.TableRelation;
import com.kalix.framework.core.api.dao.IGenericDao;
import com.kalix.framework.core.api.exception.KalixRuntimeException;
import com.kalix.framework.core.api.exception.SearchException;
import com.kalix.framework.core.api.persistence.JpaQuery;
import com.kalix.framework.core.api.persistence.JpaStatistic;
import com.kalix.framework.core.api.persistence.JsonData;
import com.kalix.framework.core.api.persistence.PersistentEntity;
import com.kalix.framework.core.api.web.model.QueryDTO;
import org.apache.log4j.Logger;

import javax.persistence.*;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @类描述：DAO数据访问通用实现抽象类
 * @创建人：sunlf
 * @创建时间：2014-7-3 下午1:01:59
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
//@Transactional
public abstract class GenericDao<T extends PersistentEntity, PK extends Serializable> implements IGenericDao<T, PK> {
    protected EntityManager entityManager;
    protected final Logger logger = Logger.getLogger(this.getClass());
    protected Class<T> persistentClass;//直接获取T Class，函数不需要className
    protected String className;
    protected String classSimpleName;

    public GenericDao() {
        Object obj = this.getClass().getGenericSuperclass();
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        java.lang.reflect.Type type = genericSuperclass.getActualTypeArguments()[0];
        if (type instanceof Class) {
            this.persistentClass = (Class<T>) type;
        } else if (type instanceof ParameterizedType) {
            this.persistentClass = (Class<T>) ((ParameterizedType) type).getRawType();
        }

        className = this.persistentClass.getName();
        String[] pkgSplit = className.split("\\.");
        classSimpleName = pkgSplit[pkgSplit.length - 1];
    }

    /**
     * Constructor that takes in a class to see which type of entity to persist.
     * Use this constructor when subclassing.
     *
     * @param persistentClass the class type you'd like to persist
     */
    public GenericDao(final Class<T> persistentClass) {
        this.persistentClass = persistentClass;
        className = this.persistentClass.getName();
    }

    @Override
    public List<T> getAll() {
        final Query query = entityManager.createQuery("select c from " + className + " c ");
        final List<T> resultList = query.getResultList();
        return resultList;
    }

    /**
     * JPA 分页查询
     *
     * @param page
     * @param limit
     * @return
     */
    @Override
    public JsonData getAll(int page, int limit) {
        JsonData jsonData = new JsonData();
        Class entityClass = null;
        try {
            entityClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Long count = getTotalCount(className);
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root from = criteriaQuery.from(entityClass);
        CriteriaQuery select = criteriaQuery.select(from);
        select.orderBy(criteriaBuilder.desc(from.get("creationDate")));
        TypedQuery typedQuery = entityManager.createQuery(select);

        typedQuery.setFirstResult((page - 1) * limit);
        typedQuery.setMaxResults(limit);
        jsonData.setTotalCount(count);
        jsonData.setData(typedQuery.getResultList());
        return jsonData;
    }

    /**
     * 自动根据beginDate和endDate参数查询dateField
     *
     * @param queryDTO
     * @param dateField 需要查询的时间字段
     * @return
     */
    protected CriteriaQuery buildBetweenDateCriteriaQuery(QueryDTO queryDTO, String dateField) {
        Map map = queryDTO.getJsonMap();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(persistentClass);
        Root<T> root = criteriaQuery.from(persistentClass);
        EntityType<T> bean_ = root.getModel(); //实体元数据
        List<Predicate> predicatesList = new ArrayList<Predicate>();

        if (map.get("beginDate") != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date beginDate = dateFormat.parse((String) map.get("beginDate"));
                SingularAttribute<T, Date> begin = (SingularAttribute<T, Date>) bean_.getSingularAttribute(dateField);
                predicatesList.add(criteriaBuilder.greaterThanOrEqualTo(root.get(begin), beginDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (map.get("endDate") != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date endDate = dateFormat.parse((String) map.get("endDate"));
                SingularAttribute<T, Date> end = (SingularAttribute<T, Date>) bean_.getSingularAttribute(dateField);
                predicatesList.add(criteriaBuilder.lessThanOrEqualTo(root.get(end), endDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        criteriaQuery.where(predicatesList.toArray(new Predicate[predicatesList.size()]));

        CriteriaQuery select = criteriaQuery.select(root);
        select.orderBy(criteriaBuilder.desc(root.get("creationDate")));
        return select;
    }

    @Override
    public CriteriaQuery buildCriteriaQuery(QueryDTO queryDTO) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(this.persistentClass);
        Root<T> root = criteriaQuery.from(this.persistentClass);
        EntityType<T> bean_ = root.getModel(); //实体元数据
        List<Predicate> predicatesList = new ArrayList<Predicate>();
        List<Selection<?>> selectionList = new ArrayList<>();
        Map<String, String> jsonMap = queryDTO.getJsonMap();
        JpaQuery jpaQuery = new JpaQuery(criteriaBuilder, root);
        Map<String, JpaQuery> relationJpaQueryMap = new HashMap<>();
        List<String> sortKeys = new ArrayList<String>();
        List<String> sortValues = new ArrayList<String>();

        selectionList.add(root);

        TableRelation tr = this.persistentClass.getAnnotation(TableRelation.class);

        if (tr != null) {
            Object[] managedTypes = entityManager.getMetamodel().getManagedTypes().toArray();
            Relation[] relations = tr.relations();

            for (Relation relation : relations) {
                String beanName = relation.BeanName();

                for (Object managedType : managedTypes) {
                    String fullName = managedType.toString();

                    if (fullName.contains(beanName)) {
                        try {
                            Root relationRoot = criteriaQuery.from(Class.forName(fullName));

                            relationJpaQueryMap.put(beanName, new JpaQuery(criteriaBuilder, relationRoot));

                            String[] pFields = relation.PFields();
                            String[] fFields = relation.FFields();
                            predicatesList.add(criteriaBuilder.equal(root.get(relation.FK()), relationRoot.get(relation.PK())));

                            if (pFields.length == fFields.length) {
                                for (int fIndex = 0; fIndex < pFields.length; ++fIndex) {
                                    selectionList.add(relationRoot.get(pFields[fIndex]).alias(fFields[fIndex]));
                                }
                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                        break;
                    }
                }
            }
        }

        for (Map.Entry<String, String> entry : jsonMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (value == null || value.trim().isEmpty()) {
                continue;
            }

            if (key.contains(":relation")) {
                String[] keySplit = key.split(":");
                String fieldName = keySplit[0];
                String beanName = keySplit[2];

                if (fieldName.contains("%")) {
                    predicatesList.add(relationJpaQueryMap.get(beanName).LIKE(fieldName, value));
                } else {
                    predicatesList.add(relationJpaQueryMap.get(beanName).EQUAL(fieldName, value));
                }
            } else if (key.contains("%")) {
                predicatesList.add(jpaQuery.LIKE(key, value));
            } else if (key.contains(":begin:gt") || key.contains(":end:lt")) {
                predicatesList.add(jpaQuery.DATE(key, value));
            } else if (key.contains(":in")) {
                predicatesList.add(jpaQuery.IN(key, value));
            } else if (key.contains(":gt")) {
                predicatesList.add(jpaQuery.GreaterThan(key, value));
            } else if (key.contains(":sort")) {
                sortKeys.add(key);
                sortValues.add(value);
            } else {
                predicatesList.add(jpaQuery.EQUAL(key, value));
            }
        }

        //添加条件
        if (predicatesList.size() > 0) {
            criteriaQuery.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
        }

        CriteriaQuery select = null;

        if (selectionList.size() == 1) {
            select = criteriaQuery.select(root);
        } else {
            //此操作需要实体类拥有相应参数的构造函数
            select = criteriaQuery.multiselect(selectionList);
        }

        //排序
        jpaQuery.SORT(select, sortKeys, sortValues);

        return select;
    }

    @Override
    public JsonData getAll(int page, int limit, CriteriaQuery criteriaQuery) {
        JsonData jsonData = new JsonData();
        TypedQuery typedQuery = entityManager.createQuery(criteriaQuery);

        if (0 == page && 0 == limit) {
        } else {
            typedQuery.setFirstResult((page - 1) * limit);
            typedQuery.setMaxResults(limit);
        }

        jsonData.setData(typedQuery.getResultList());
        jsonData.setTotalCount(getTotalCount(className, criteriaQuery));

        return jsonData;
    }

    @Override
    public JsonData getAll(CriteriaQuery criteriaQuery) {
        JsonData jsonData = new JsonData();
        Class entityClass = null;
        try {
            entityClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        TypedQuery typedQuery = entityManager.createQuery(criteriaQuery);
        jsonData.setData(typedQuery.getResultList());
        jsonData.setTotalCount(getTotalCount(className, criteriaQuery));
        return jsonData;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager em) {
        entityManager = em;
    }

    /**
     * 获得结果集的总数
     *
     * @param className
     * @return
     */
    private Long getTotalCount(String className) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        try {
            countQuery.select(criteriaBuilder.count(countQuery.from(Class.forName(className))));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Long count = entityManager.createQuery(countQuery).getSingleResult();
        return count;
    }


    /**
     * 获得结果集的总数
     *
     * @param className
     * @param criteriaQuery
     * @return
     */
    private Long getTotalCount(String className, CriteriaQuery criteriaQuery) {
        Long count = 0L;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//        CriteriaQuery countQuery = criteriaBuilder.createQuery();
        //构造where条件
//        countQuery.where(criteriaQuery.getRestriction());
        //构造from
        Set<Root> rootSet = criteriaQuery.getRoots();
        /*for (Root root : rootSet) {
            countQuery.from(root.getModel());
        }*/
        //构造select
        Root[] arr = new Root[rootSet.size()];
        rootSet.toArray(arr);
        criteriaQuery.select(criteriaBuilder.count(arr[0].get("id")));
        criteriaQuery.orderBy((Order[]) null);
        logger.debug("jpql statement is : " + criteriaQuery.toString());
        TypedQuery query = entityManager.createQuery(criteriaQuery);
//        System.out.println("sql statement is : "+query.unwrap(DelegatingQuery.class).getQueryString());
        List results = query.getResultList();
        if (!results.isEmpty()) {
            count = (Long) results.get(0);
        }
        return count;
//        return Long.valueOf(entityManager.createQuery(criteriaQuery).getResultList().size());
    }

    @Override
    public List<T> getAllDistinct() {
        final Query query = entityManager.createQuery("select Distinct c from " + className + " c ");
        final List<T> resultList = query.getResultList();
        return resultList;
    }

    @Override
    public List<T> search(String searchTerm) throws SearchException {
        return null;
    }

    @Override
    public T get(PK id) {

        try {
            T entity = (T) entityManager.find(Class.forName(className), id);

            if (entity == null) {
                throw new KalixRuntimeException("数据不存在", "FAIL_ON_EMPTY_BEANS");
            }

            return (T) entityManager.find(Class.forName(className), id);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean exists(PK id) {
        return false;
    }

    @Override
    @Transactional
    public T save(T object) {
        if (object.getId() == 0)//do not persist
            entityManager.persist(object);
        else {
            object.setUpdateDate(new Date());
            entityManager.merge(object);
        }

        entityManager.flush();
        return object;
    }

    @Override
    @Transactional
    public T save(T object, String userName) {
        if (object.getId() == 0)//do not persist
        {
            object.setCreateBy(userName);
            entityManager.persist(object);
        } else {
            object.setUpdateBy(userName);
            object.setUpdateDate(new Date());
            entityManager.merge(object);
        }
        entityManager.flush();
        return object;
    }

    //@Override
    public void remove(String className, T object) {
        entityManager.remove(object);
        entityManager.flush();
    }

    @Override
    @Transactional
    public void remove(PK id) {
        Object object = get(id);
        entityManager.remove(object);
        entityManager.flush();
    }

    // 批量删除
    @Override
    @Transactional
    public void removeBatch(String ids) {
        String strIds = ids;
        if (strIds != null && strIds.trim().length() > 0) {
            Object object;
            strIds = strIds.replaceAll(",", ":");
            strIds = strIds.replaceAll(";", ":");
            String[] arrayIds = strIds.split(":");
            for (int i = 0; i < arrayIds.length; i++) {
                if (arrayIds[i] != null && arrayIds[i].trim().length() > 0) {
                    object = get((PK) arrayIds[i]);
                    entityManager.remove(object);
                }
            }
            entityManager.flush();
        }
    }

    @Override
    public List<T> findByNamedQuery(String queryName, Map<String, Object> queryParams) {
        return null;
    }

    @Override
    public void reindex() {

    }

    @Override
    public void reindexAll(boolean async) {

    }

    /**
     * 按HQL查询唯一对象.
     */
    @Override
    public <T> T findUnique(String hql, Object... values) {
        List list = this.find(hql, values);
        if (list != null && list.size() > 0) {

            return (T) list.get(0);
        }
        return null;
    }

    @Override
    public List find(String hql, Object... values) {
        return createQuery(hql, values).getResultList();
    }

    /**
     * 支持原生的sql查询
     *
     * @param sql   sql语句
     * @param cls   需要返回结果的类
     * @param parms 参数
     * @return
     */
    @Override
    public List findByNativeSql(String sql, Class cls, Object... parms) {
        return createNativeQuery(sql, cls, parms).getResultList();
    }

    @Override
    public List findArrayByNativeSql(String sql, Object... parms) {
        return createNativeQuery(sql, parms).getResultList();
    }


    private Long getNativeTotalCount(String className, Query query) {
        List list = query.getResultList();
        Long count = Long.valueOf(list.size());
        return count;
    }

    @Override
    public JsonData findByNativeSql(String sql, int page, int limit, Class cls, Object... parms) {
        JsonData jsonData = new JsonData();
        Query query = createNativeQuery(sql, cls, parms);

        jsonData.setTotalCount(getNativeTotalCount(cls.getName(), query));
        query.setFirstResult((page - 1) * limit);
        query.setMaxResults(limit);
        jsonData.setData(query.getResultList());
        return jsonData;
    }

    private Query createNativeQuery(String sql, Class cls, Object[] parameter) {
        Query queryObject = entityManager.createNativeQuery(sql, cls);
        if (parameter != null) {
            for (int i = 0; i < parameter.length; i++) {
                queryObject.setParameter(i + 1, parameter[i]);
            }
        }

        return queryObject;
    }
    private Query createNativeQuery(String sql, Object[] parameter) {
        Query queryObject = entityManager.createNativeQuery(sql);
        if (parameter != null) {
            for (int i = 0; i < parameter.length; i++) {
                queryObject.setParameter(i + 1, parameter[i]);
            }
        }

        return queryObject;
    }

    /**
     * @param hql
     * @param pageNumber 从0开始的页号
     * @param pageSize
     * @param values
     * @return
     */
    @Override
    public List findbyPage(String hql, int pageNumber, int pageSize, Object... values) {
        Query queryObject = createQuery(hql, values);
        /*List result=queryObject.getResultList();//获得结果集个数
        int count=result.size();
        if(count==0)
            return result;*/
        queryObject.setFirstResult(pageNumber * pageSize);
        queryObject.setMaxResults(pageSize);
        return queryObject.getResultList();
    }

    /**
     * 根据查询函数与参数列表创建Query对象,后续可进行更多处理,辅助函数.
     */
//    @Transactional(Transactional.TxType.SUPPORTS)
    protected Query createQuery(String queryString, Object... parameter) {
        Query queryObject = entityManager.createQuery(queryString);
        if (parameter != null) {
            for (int i = 0; i < parameter.length; i++) {
                queryObject.setParameter(i + 1, parameter[i]);
            }
        }

        return queryObject;
    }

    /**
     * 更新
     *
     * @param qlString
     * @param parameter
     * @return
     */
    @Override
    @Transactional
    public int update(String qlString, Object... parameter) {
        return createQuery(qlString, parameter).executeUpdate();
    }

    /**
     * 更新，使用纯生sql
     *
     * @param sql
     * @return
     */
    @Override
    @Transactional
    public int updateNativeQuery(String sql) {
        return entityManager.createNativeQuery(sql).executeUpdate();
    }

    @Override
    public Integer getFieldMaxValue(String fieldName, String where) {
        Query query = null;

        if (where == null || where.equals("")) {
            query = createQuery("SELECT MAX(t." + fieldName + ") from " + this.persistentClass.getSimpleName() + " t");
        } else {
            query = createQuery("SELECT MAX(t." + fieldName + ") from " + this.persistentClass.getSimpleName() + " t WHERE t." + where);
        }

        Object result = query.getResultList().get(0);

        if (result == null) {
            return -1;
        } else {
            return Integer.valueOf(result.toString());
        }
    }

    @Override
    public String getTableName() {
        Table tb = persistentClass.getAnnotation(Table.class);

        if (tb != null) {
            return tb.name();
        }

        return null;
    }

    /**
     * add by yangz 2018-01-10
     * 获取统计数据,暂时实现sum，count，avg，min，max 简单方式
     * queryDTO :
     * 1.groupBys 分组字段
     * 2.selectNotStatistic select中非统计字段
     * 3.selectStatistic select中统计字段
     * 4.selectStatisticType select中统计字段类型
     *
     * @return
     */
    public JsonData getAllByStatistic(QueryDTO queryDTO) {
        JsonData jsonData = new JsonData();
        Map<String, String> jsonMap = queryDTO.getJsonMap();
        String[] groupBys = jsonMap.get(JpaStatistic.tag_groupBys).split(",");
        String[] notStatistics = jsonMap.get(JpaStatistic.tag_selectNotStatistic).split(",");
        String[] statistics = jsonMap.get(JpaStatistic.tag_selectStatistic).split(",");
        String[] statisticTypes = jsonMap.get(JpaStatistic.tag_statisticType).split(",");

        // 统计字段为空，或者统计字段类型为空，或者统计字段数与类型数不符直接返回
        if (statisticTypes == null || statisticTypes.length == 0
                || statistics == null || statistics.length == 0 || statisticTypes.length != statistics.length) {
            return jsonData;
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = criteriaBuilder.createQuery(Tuple.class);
        Root<T> root = cq.from(this.persistentClass);
        // groupby 添加
        if (groupBys != null) {
            List groupList = new ArrayList<>();
            for (String groupBy : groupBys) {
                groupList.add(root.get(groupBy));
            }
            cq.groupBy(groupList);
        }

        int selectLength = 0;
        if (notStatistics != null || notStatistics.length > 0) {
            selectLength = notStatistics.length + statisticTypes.length;
        } else {
            selectLength = statisticTypes.length;
        }
        Selection<?>[] selections = new Selection<?>[selectLength];
        if (notStatistics != null || notStatistics.length > 0) {
            // 非统计的select字段如果不空添加到select中
            for (int i = 0; i < notStatistics.length; i++) {
                selections[i] = root.get(notStatistics[i]);
            }
            // 统计字段添加到select
            for (int i = 0; i < statisticTypes.length; i++) {
                if (statisticTypes[i].equals(JpaStatistic.Statistic.COUNT.name())) {
                    selections[notStatistics.length + i] = criteriaBuilder.count(root.get(statistics[i]));
                }
                if (statisticTypes[i].equals(JpaStatistic.Statistic.SUM.name())) {
                    selections[notStatistics.length + i] = criteriaBuilder.sum(root.get(statistics[i]));
                }
                if (statisticTypes[i].equals(JpaStatistic.Statistic.MAX.name())) {
                    selections[notStatistics.length + i] = criteriaBuilder.max(root.get(statistics[i]));
                }
                if (statisticTypes[i].equals(JpaStatistic.Statistic.MIN.name())) {
                    selections[notStatistics.length + i] = criteriaBuilder.min(root.get(statistics[i]));
                }
                if (statisticTypes[i].equals(JpaStatistic.Statistic.AVG.name())) {
                    selections[notStatistics.length + i] = criteriaBuilder.avg(root.get(statistics[i]));
                }
            }
        } else {
            // 统计字段添加到select
            for (int i = 0; i < statisticTypes.length; i++) {
                if (statisticTypes[i].equals(JpaStatistic.Statistic.COUNT.name())) {
                    selections[i] = criteriaBuilder.count(root.get(statistics[i]));
                }
                if (statisticTypes[i].equals(JpaStatistic.Statistic.SUM.name())) {
                    selections[i] = criteriaBuilder.sum(root.get(statistics[i]));
                }
                if (statisticTypes[i].equals(JpaStatistic.Statistic.MAX.name())) {
                    selections[i] = criteriaBuilder.max(root.get(statistics[i]));
                }
                if (statisticTypes[i].equals(JpaStatistic.Statistic.MIN.name())) {
                    selections[i] = criteriaBuilder.min(root.get(statistics[i]));
                }
                if (statisticTypes[i].equals(JpaStatistic.Statistic.AVG.name())) {
                    selections[i] = criteriaBuilder.avg(root.get(statistics[i]));
                }
            }
        }
        cq.select(criteriaBuilder.tuple(selections));

        // where条件
        List<Predicate> predicatesList = getPredicateWhere(jsonMap, root);
        //添加条件
        if (predicatesList.size() > 0) {
            cq.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
        }
        // having 添加
        //cq.having(criteriaBuilder.like(root.get(Employee_.name), "N%"));

        TypedQuery<Tuple> q = entityManager.createQuery(cq);
        List<Tuple> result = q.getResultList();
        jsonData.setData(result);
        return jsonData;
    }

    private List<Predicate> getPredicateWhere(Map<String, String> jsonMap, Root<T> root) {
        // where条件
        List<Predicate> predicatesList = new ArrayList<>();
        JpaQuery jpaQuery = new JpaQuery(entityManager.getCriteriaBuilder(), root);

        for (Map.Entry<String, String> entry : jsonMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key.equals(JpaStatistic.tag_groupBys) || key.equals(JpaStatistic.tag_selectNotStatistic)
                    || key.equals(JpaStatistic.tag_selectStatistic) || key.equals(JpaStatistic.tag_statisticType)) {
                continue;
            }
            if (value == null || value.trim().isEmpty()) {
                continue;
            }
            if (key.contains("%")) {
                predicatesList.add(jpaQuery.LIKE(key, value));
            } else if (key.contains(":begin:gt") || key.contains(":end:lt")) {
                predicatesList.add(jpaQuery.DATE(key, value));
            } else if (key.contains(":in")) {
                predicatesList.add(jpaQuery.IN(key, value));
            } else {
                predicatesList.add(jpaQuery.EQUAL(key, value));
            }
        }
        return predicatesList;
    }
}

