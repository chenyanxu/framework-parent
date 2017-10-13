package com.kalix.framework.core.api.security.model;

/**
 * Created by Administrator on 2017-10-12.
 * 数据权限枚举类
 */
public enum EnumDataAuth {
    SELF(1), // 仅本人数据
    ALL(2),  // 所有数据
    SELF_ORG(3), // 所在组织机构数据
    SELF_AND_CHILD_ORG(4); // 所在组织机构及以下子机构数据

    private final int value;

    //构造方法必须是private或者默认
    EnumDataAuth(int value) {
        this.value = value;
    }
}
