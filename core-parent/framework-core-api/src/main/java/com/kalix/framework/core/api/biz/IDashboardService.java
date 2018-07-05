package com.kalix.framework.core.api.biz;

import com.kalix.framework.core.api.IService;
import com.kalix.framework.core.api.dto.PieSeriesDataDTO;
import com.kalix.framework.core.api.persistence.JsonData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

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

    JsonData getRaddarChartData(String chartKey);

    JsonData getPieChartData(String chartKey);

    JsonData getBarChartData(String chartKey);

    Integer getPanelGroupBizData(String chartKey);

    List<Integer> getLineChartBizData(String chartKey, String legend);

    List<Integer> getRaddarChartBizData(String chartKey, String legend);

    List<PieSeriesDataDTO> getPieChartBizData(String chartKey);

    List<Integer> getBarChartBizData(String chartKey, String legend);
}
