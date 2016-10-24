package com.kalix.framework.core.api.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by chenyanxu on 2016/10/10.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Relation {
    String BeanName();  //关联表实体名
    String PK();        //关联表主键
    String[] PFields(); //关联表字段
    String FK();        //主表外键
    String[] FFields(); //主表关联字段
}
