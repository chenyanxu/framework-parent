package com.kalix.framework.core.api.biz;

import com.kalix.framework.core.api.IService;
import com.kalix.framework.core.api.persistence.JsonData;

/**
 * @类描述： 对外Dashboard服务的根接口
 * @创建人：hqj
 * @创建时间：2018-6-26
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
public interface IDashboardService extends IService {

    JsonData getPanelGroupData();

    JsonData getLineChartData(String chartKey);
}
