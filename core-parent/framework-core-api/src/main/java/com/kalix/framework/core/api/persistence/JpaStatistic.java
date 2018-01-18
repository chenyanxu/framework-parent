package com.kalix.framework.core.api.persistence;

import com.kalix.framework.core.api.exception.KalixRuntimeException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;
import java.util.Map;

@ApiModel("统计类型的参数定义")
public class JpaStatistic {
    public enum Statistic {
        COUNT, SUM, AVG, MAX, MIN
    }

    // 分组字段
    public final static String tag_groupBys = "tag_groupBys";
    // select中非统计字段
    public final static String tag_selectNotStatistic = "tag_selectNotStatistic";
    // select中统计字段
    public final static String tag_selectStatistic = "tag_selectStatistic";
    // select中统计字段类型
    public final static String tag_statisticType = "tag_selectStatisticType";

    @ApiModelProperty("分组字段")
    private String[] groupBys;
    @ApiModelProperty("select中非统计字段")
    private String[] selectNotStatistics;
    @ApiModelProperty("select中统计字段")
    private String[] selectStatistics;
    @ApiModelProperty("select中统计字段类型")
    private Statistic[] statisticTypes;

    public void setGroupBys(String... groupBys) {
        this.groupBys = groupBys;
    }

    public void setSelectNotStatistics(String... selectNotStatistics) {
        this.selectNotStatistics = selectNotStatistics;
    }

    public void setSelectStatistics(String... selectStatistics) {
        this.selectStatistics = selectStatistics;
    }

    public void setStatisticTypes(Statistic... statisticTypes) {
        this.statisticTypes = statisticTypes;
    }

    public Map<String, String> getStatisticParam() {
        if (selectStatistics == null || selectStatistics.length == 0) {
            throw new KalixRuntimeException("statistic", "select column is null");
        }
        if (statisticTypes == null || statisticTypes.length == 0) {
            throw new KalixRuntimeException("statistic", "statistic type is null");
        }
        Map<String, String> map = new HashMap<>();
        if (groupBys != null && groupBys.length > 0) {
            map.put(tag_groupBys, String.join(",", groupBys));
        }
        if (selectNotStatistics != null && selectNotStatistics.length > 0) {
            map.put(tag_selectNotStatistic, String.join(",", selectNotStatistics));
        }
        map.put(tag_selectStatistic, String.join(",", selectStatistics));
        StringBuffer types = new StringBuffer();
        for (int i = 0; i < statisticTypes.length; i++) {
            if (i != 0) {
                types.append(",");
            }
            types.append(statisticTypes[i].name());
        }
        map.put(tag_statisticType, types.toString());
        return map;
    }
}
