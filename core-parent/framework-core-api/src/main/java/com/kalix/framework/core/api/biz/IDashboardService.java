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

    /**
     * 定义前台使用PanelGroup组件的样式与数据
     * @return
     */
    JsonData getPanelGroupData();

    /**
     * 定义前台使用LineChart组件的样式与数据
     * @param chartKey
     * @return
     */
    JsonData getLineChartData(String chartKey);

    /**
     * 定义前台使用RaddarChart组件的样式与数据
     * @param chartKey
     * @return
     */
    JsonData getRaddarChartData(String chartKey);

    /**
     * 定义前台使用PieChart组件的样式与数据
     * @param chartKey
     * @return
     */
    JsonData getPieChartData(String chartKey);

    /**
     * 定义前台使用BarChart组件的样式与数据
     * @param chartKey
     * @return
     */
    JsonData getBarChartData(String chartKey);

    /**
     * 根据chartKey组织PanelGroup数据
     * @param chartKey
     * @return
     */
    Integer getPanelGroupBizData(String chartKey);

    /**
     * 根据chartKey与legend图例组织LineChart数据
     * @param chartKey
     * @param legend
     * @return
     */
    List<Integer> getLineChartBizData(String chartKey, String legend);

    /**
     * 根据chartKey与legend图例组织RaddarChart数据
     * @param chartKey
     * @param legend
     * @return
     */
    List<Integer> getRaddarChartBizData(String chartKey, String legend);

    /**
     * 根据chartKey组织PieChart数据
     * @param chartKey
     * @return
     */
    List<PieSeriesDataDTO> getPieChartBizData(String chartKey);

    /**
     * 根据chartKey与legend图例组织BarChart数据
     * @param chartKey
     * @param legend
     * @return
     */
    List<Integer> getBarChartBizData(String chartKey, String legend);
}
