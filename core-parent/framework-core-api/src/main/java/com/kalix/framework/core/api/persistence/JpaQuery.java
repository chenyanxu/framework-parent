package com.kalix.framework.core.api.persistence;

import com.kalix.framework.core.util.DateUtil;
import com.kalix.framework.core.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *@author chenyanxu
 */
public class JpaQuery<T extends PersistentEntity> {
    private CriteriaBuilder cb;
    private Root root;

    public JpaQuery(CriteriaBuilder cb, Root root){
        this.cb=cb;
        this.root=root;
    }

    public Predicate LIKE(String key,String value){
        EntityType bean_ = root.getModel();
        SingularAttribute<T, String> attribute = (SingularAttribute<T, String>) bean_.getSingularAttribute(key.replace("%", ""));
        Predicate predicate=null;
        int firstIndex = key.indexOf("%");
        int lastIndex=key.lastIndexOf("%");

        if(0==firstIndex && lastIndex>0){
            predicate=cb.like(root.get(attribute), "%" + value + "%");
        }
        else if(0==firstIndex && 0==lastIndex){
            predicate=cb.like(root.get(attribute), "%" + value);
        }
        else {
            predicate=cb.like(root.get(attribute), value + "%");
        }

        return predicate;
    }

    public Predicate DATE(String key,String value){
        EntityType bean_ = root.getModel();
        SingularAttribute<T, Date> attribute = (SingularAttribute<T, Date>) bean_.getSingularAttribute(key.split(":")[0]);
        Predicate predicate=null;

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(value);
            if (key.contains(":begin:gt")) {
                predicate = cb.greaterThanOrEqualTo(root.get(attribute), DateUtil.getCurrentDayStartTime(date));
            } else if (key.contains(":end:lt")) {
                predicate=cb.lessThanOrEqualTo(root.get(attribute), DateUtil.getCurrentDayEndTime(date));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return predicate;
    }

    public Predicate IN(String key,String value){
        EntityType bean_ = root.getModel();
        SingularAttribute<T, Object> attribute = (SingularAttribute<T, Object>) bean_.getSingularAttribute(key.split(":")[0]);
        String attrJavaTypeName = attribute.getJavaType().getName();
        Predicate predicate=null;
        String[] s = value.split(",");

        if (attrJavaTypeName.equals(String.class.getName())){
            predicate=root.get(attribute.getName()).in(s);
        }
        else if (attrJavaTypeName.equals(long.class.getName()) || attrJavaTypeName.equals(Long.class.getName())) {
            predicate=root.get(attribute.getName()).in(StringUtils.toLongArray(s));
        }
        else if (attrJavaTypeName.equals(int.class.getName()) || attrJavaTypeName.equals(Integer.class.getName())){
            predicate=root.get(attribute.getName()).in(StringUtils.toIntArray(s));
        }
        else if (attrJavaTypeName.equals(short.class.getName()) || attrJavaTypeName.equals(Short.class.getName())){
            predicate=root.get(attribute.getName()).in(StringUtils.toShortArray(s));
        }

        return predicate;
    }

    public Predicate EQUAL(String key,String value){
        EntityType bean_ = root.getModel();
        SingularAttribute<T, Object> attribute = (SingularAttribute<T, Object>) bean_.getSingularAttribute(key.split(":")[0]);
        String attrJavaTypeName = attribute.getJavaType().getName();
        Predicate predicate=null;

        if (attrJavaTypeName.equals(String.class.getName())){
            predicate=cb.equal(root.get(attribute), value);
        }
        else if (attrJavaTypeName.equals(long.class.getName()) || attrJavaTypeName.equals(Long.class.getName())) {
            predicate=cb.equal(root.get(attribute), new Long(value));
        }
        else if (attrJavaTypeName.equals(int.class.getName()) || attrJavaTypeName.equals(Integer.class.getName())){
            predicate=cb.equal(root.get(attribute), new Integer(value));
        }
        else if (attrJavaTypeName.equals(short.class.getName()) || attrJavaTypeName.equals(Short.class.getName())){
            predicate=cb.equal(root.get(attribute), new Short(value));
        }

        return predicate;
    }

    public void SORT(CriteriaQuery criteriaQuery,String key,String value){
        if(key==null || value ==null) return;

        EntityType bean_ = root.getModel();
        String sortField = "updateDate";
        String sortDirection = "DESC";

        if(bean_.getAttributes().toString().indexOf("."+key.replace(":sort",""))>-1){
            sortField = key.replace(":sort", "");
            sortDirection = value;
        }

        switch(sortDirection){
            case "DESC":
                criteriaQuery.orderBy(cb.desc(root.get(sortField)));
                break;
            case "ASC":
                criteriaQuery.orderBy(cb.asc(root.get(sortField)));
                break;
        }
    }
}
