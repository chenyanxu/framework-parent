package com.kalix.framework.core.api.dto;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @类描述： 前台表格LineChart数据模型
 * @创建人： hqj
 * @创建时间：2018/6/26
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
public class LineChartDTO implements Serializable {

    private List<Integer> expectedData = new ArrayList<Integer>();     // 期望值
    private List<Integer> actualData = new ArrayList<Integer>(); // 实际值

    public List<Integer> getExpectedData() {
        return expectedData;
    }

    public void setExpectedData(List<Integer> expectedData) {
        this.expectedData = expectedData;
    }

    public List<Integer> getActualData() {
        return actualData;
    }

    public void setActualData(List<Integer> actualData) {
        this.actualData = actualData;
    }
}
