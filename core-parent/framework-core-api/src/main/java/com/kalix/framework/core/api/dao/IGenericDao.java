package com.kalix.framework.core.api.dao;

import com.kalix.framework.core.api.IService;
import com.kalix.framework.core.api.exception.SearchException;
import com.kalix.framework.core.api.persistence.JsonData;
import com.kalix.framework.core.api.web.model.QueryDTO;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @类描述：DAO数据访问通用接口
 * @创建人：sunlf
 * @创建时间：2014-7-3 下午1:01:59
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
public interface IGenericDao<T, PK extends Serializable> extends IService {


    /**
     * Gets all records without duplicates.
     * <p>Note that if you use this method, it is imperative that your model
     * classes correctly implement the hashcode/equals methods</p>
     *
     * @return List of populated objects
     */
    List<T> getAllDistinct();

    /**
     * Gets all records that match a search term. "*" will get them all.
     *
     * @param searchTerm the term to search for
     * @return the matching records
     * @throws SearchException
     */
    List<T> search(String searchTerm) throws SearchException;

    /**
     * Generic method to get an object based on class and identifier. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if
     * nothing is found.
     *
     * @param id the identifier (primary key) of the object to get
     * @return a populated object
     */
    T get(PK id);

    /**
     * Checks for existence of an object of type T using the id arg.
     *
     * @param id the id of the entity
     * @return - true if it exists, false if it doesn't
     */
    boolean exists(PK id);

    /**
     * Generic method to save an object - handles both update and insert.
     *
     * @param object the object to save
     * @return the persisted object
     */
    T save(T object);

    /**
     * Generic method to save an object - handles both update and insert.
     *
     * @param object   the object to save
     * @param userName 操作人员的名字
     * @return the persisted object
     */
    T save(T object, String userName);

    /**
     * Generic method to delete an object
     *
     * @param object the object to remove
     */
    // void remove(String className,T object);

    /**
     * Generic method to delete an object
     *
     * @param id the identifier (primary key) of the object to remove
     */
    void remove(PK id);

    /**
     * Generic method to delete multi object
     *
     * @param ids the identifiers (primary keys) of the objects to remove,seplated using comma,semicolon or colon
     */
    void removeBatch(String ids);
    /**
     * Find a list of records by using a named query
     *
     * @param queryName   query name of the named query
     * @param queryParams a map of the query names and the values
     * @return a list of the records found
     */
    List<T> findByNamedQuery(String queryName, Map<String, Object> queryParams);

    /**
     * Generic method to regenerate full text index of the persistence class T
     */
    void reindex();

    /**
     * Generic method to regenerate full text index of all indexed classes
     *
     * @param async true to perform the reindexing asynchronously
     */
    void reindexAll(boolean async);

    List find(String hql, Object... values);

    /**
     * 支持原生的sql查询
     *
     * @param sql   sql语句
     * @param cls   需要返回结果的类
     * @param parms 参数
     * @return
     */

    List findByNativeSql(String sql, Class cls, Object... parms);

    JsonData findByNativeSql(String sql, int page, int limit, Class cls, Object... parms);

    List findbyPage(String hql, int pageNumber, int pageSize, Object... values);


    <T> T findUnique(String hql, Object... values);

    int update(String qlString, Object... parameter);

    int updateNativeQuery(String sql);

    /**
     * Generic method used to get all objects of a particular type. This
     * is the same as lookup up all rows in a table.
     *
     * @return List of populated objects
     */
    List<T> getAll();

    JsonData getAll(int pageNumber, int pageSize);

    /**
     * 按指定条件查询
     * @param page
     * @param limit
     * @param criteriaQuery
     * @return
     */
    JsonData getAll(int page, int limit, CriteriaQuery criteriaQuery);

    JsonData getAll(CriteriaQuery criteriaQuery);

    CriteriaQuery buildCriteriaQuery(QueryDTO queryDTO);

    Integer getFieldMaxValue(String fieldName,String where);
    /**
     * 返回JPA 实体管理器
     * @return
     */
    EntityManager getEntityManager();

    //Get the entity bean table name of the dao
    String getTableName();

    /**
     * 统计查询接口
     *
     * @param queryDTO
     * @return
     */
    JsonData getAllByStatistic(QueryDTO queryDTO);


    List findByNativeSql(String sql, Object... parms);
}
