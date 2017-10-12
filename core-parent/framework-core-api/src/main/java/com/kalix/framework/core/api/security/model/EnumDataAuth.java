package com.kalix.framework.core.api.security.model;

/**
 * Created by Administrator on 2017-10-12.
 * 数据权限枚举类
 */
public enum EnumDataAuth {
    ALL,  // 所有数据
    SELF, // 仅本人数据
    SELF_ORG, // 所在组织机构数据
    SELF_AND_CHILD_ORG // 所在组织机构及以下子机构数据
}
