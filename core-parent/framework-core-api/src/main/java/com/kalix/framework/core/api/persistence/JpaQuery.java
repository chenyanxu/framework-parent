package com.kalix.framework.core.api.persistence;

import com.kalix.framework.core.util.DateUtil;
import com.kalix.framework.core.util.StringUtils;

import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author chenyanxu
 */
public class JpaQuery {
    private CriteriaBuilder cb;
    private Root root;

    public JpaQuery(CriteriaBuilder cb, Root root) {
        this.cb = cb;
        this.root = root;
    }

    public Predicate LIKE(String key, String value) {
        EntityType bean_ = root.getModel();
        //SingularAttribute<T, String> attribute = (SingularAttribute<T, String>) bean_.getSingularAttribute(key.replace("%", ""));
        String fieldName = key.replace("%", "");
        Predicate predicate = null;
        int firstIndex = key.indexOf("%");
        int lastIndex = key.lastIndexOf("%");

        if (0 == firstIndex && lastIndex > 0) {
            predicate = cb.like(root.get(fieldName), "%" + value + "%");
        } else if (0 == firstIndex && 0 == lastIndex) {
            predicate = cb.like(root.get(fieldName), "%" + value);
        } else {
            predicate = cb.like(root.get(fieldName), value + "%");
        }

        return predicate;
    }

    public Predicate DATE(String key, String value) {
        EntityType bean_ = root.getModel();
        //SingularAttribute<T, Date> attribute = (SingularAttribute<T, Date>) bean_.getSingularAttribute(key.split(":")[0]);
        String fieldName = key.split(":")[0];
        Predicate predicate = null;

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(value);
            if (key.contains(":begin:gt")) {
                predicate = cb.greaterThanOrEqualTo(root.get(fieldName), DateUtil.getCurrentDayStartTime(date));
            } else if (key.contains(":end:lt")) {
                predicate = cb.lessThanOrEqualTo(root.get(fieldName), DateUtil.getCurrentDayEndTime(date));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return predicate;
    }

    public Predicate IN(String key, String value) {
        EntityType bean_ = root.getModel();
        String fieldName = key.split(":")[0];
        SingularAttribute attribute = bean_.getSingularAttribute(fieldName);
        String attrJavaTypeName = attribute.getJavaType().getName();
        Predicate predicate = null;
        String[] s = value.split(",");

        if (attrJavaTypeName.equals(String.class.getName())) {
            predicate = root.get(fieldName).in((Object[]) s);
        } else if (attrJavaTypeName.equals(long.class.getName()) || attrJavaTypeName.equals(Long.class.getName())) {
            predicate = root.get(fieldName).in((Object[]) StringUtils.toLongArray(s));
        } else if (attrJavaTypeName.equals(int.class.getName()) || attrJavaTypeName.equals(Integer.class.getName())) {
            predicate = root.get(fieldName).in((Object[]) StringUtils.toIntArray(s));
        } else if (attrJavaTypeName.equals(short.class.getName()) || attrJavaTypeName.equals(Short.class.getName())) {
            predicate = root.get(fieldName).in((Object[]) StringUtils.toShortArray(s));
        }

        return predicate;
    }

    public Predicate EQUAL(String key, String value) {
        EntityType bean_ = root.getModel();
        String fieldName = key.split(":")[0];
        SingularAttribute attribute = bean_.getSingularAttribute(fieldName);
        String attrJavaTypeName = attribute.getJavaType().getName();
        Predicate predicate = null;

        if (attrJavaTypeName.equals(String.class.getName())) {
            predicate = cb.equal(root.get(fieldName), value);
        } else if (attrJavaTypeName.equals(long.class.getName()) || attrJavaTypeName.equals(Long.class.getName())) {
            predicate = cb.equal(root.get(fieldName), new Long(value));
        } else if (attrJavaTypeName.equals(int.class.getName()) || attrJavaTypeName.equals(Integer.class.getName())) {
            predicate = cb.equal(root.get(fieldName), new Integer(value));
        } else if (attrJavaTypeName.equals(short.class.getName()) || attrJavaTypeName.equals(Short.class.getName())) {
            predicate = cb.equal(root.get(fieldName), new Short(value));
        }

        return predicate;
    }

    public void SORT(CriteriaQuery criteriaQuery, List keys, List values) {
        String defaultSortField = "updateDate";
        String sortField = "";
        String sortDirection = "";
        List<Order> list = new ArrayList<Order>();
        if (keys != null && keys.size() > 0 && values != null && values.size() > 0) {
            for (int i=0;i<keys.size();i++) {
                String key = (String) keys.get(i);
                String value = (String) values.get(i);
                if (key != null && value != null) {
                    EntityType bean_ = root.getModel();
                    if (bean_.getAttributes().toString().indexOf("." + key.replace(":sort", "")) > -1) {
                        sortField = key.replace(":sort", "");
                        sortDirection = value;
                        Order order = null;
                        switch (sortDirection) {
                            case "DESC":
                                order = cb.desc(root.get(sortField));
                                break;
                            case "ASC":
                                order = cb.asc(root.get(sortField));
                                break;
                        }
                        if (order != null) {
                            list.add(order);
                        }
                    }
                }
            }
        }
        if (list.size() < 1) {
            list.add(cb.desc(root.get(defaultSortField)));
        }
        criteriaQuery.orderBy(list);
    }
}
