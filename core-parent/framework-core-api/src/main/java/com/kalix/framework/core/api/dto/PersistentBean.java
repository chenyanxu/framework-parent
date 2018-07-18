package com.kalix.framework.core.api.dto;

import com.kalix.framework.core.api.persistence.PersistentEntity;
import io.swagger.annotations.ApiModel;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @类描述：
 * @创建人：hqj
 * @创建时间：2018-07-09
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
@Entity
@Table(name = "sys_dashboard")
@ApiModel("数据模型")
public class PersistentBean extends PersistentEntity {
}
