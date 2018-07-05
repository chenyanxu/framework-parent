package com.kalix.framework.core.impl.biz;

import com.github.abel533.echarts.AxisPointer;
import com.github.abel533.echarts.Grid;
import com.github.abel533.echarts.Legend;
import com.github.abel533.echarts.Tooltip;
import com.github.abel533.echarts.axis.AxisTick;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.*;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.Pie;
import com.github.abel533.echarts.series.Series;
import com.github.abel533.echarts.style.AreaStyle;
import com.github.abel533.echarts.style.ItemStyle;
import com.github.abel533.echarts.style.LineStyle;
import com.github.abel533.echarts.style.itemstyle.Normal;
import com.kalix.framework.core.api.biz.IDashboardService;
import com.kalix.framework.core.api.dto.ChartType;
import com.kalix.framework.core.api.dto.DashboardConfigDTO;
import com.kalix.framework.core.api.dto.PanelGroupDTO;
import com.kalix.framework.core.api.dto.PieSeriesDataDTO;
import com.kalix.framework.core.api.persistence.JsonData;
import com.kalix.framework.core.util.ConfigUtil;
import com.kalix.framework.core.util.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.List;

/**
 * @类描述：
 * @创建人：hqj
 * @创建时间：2018-6-29
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
public abstract class DashboardServiceImpl implements IDashboardService {

    protected String dashboardConfigName;
    protected DashboardConfigDTO dashboardConfigDTO;

    public DashboardServiceImpl() {
    }

    public void init() {
        if (StringUtils.isNotEmpty(dashboardConfigName)) {
            dashboardConfigDTO = readConfigValue();
        } else {
            dashboardConfigDTO = setDefaultValue();
        }
    }

    @Override
    public JsonData getPanelGroupData() {
        JsonData jsonData = new JsonData();
        if (dashboardConfigDTO.getHasPanelGroup()) {
            jsonData.setData(dashboardConfigDTO.getPanels());
            jsonData.setTotalCount((long) dashboardConfigDTO.getPanelCount());
        }
        return jsonData;
    }

    @Override
    public JsonData getLineChartData(String chartKey) {
        JsonData jsonData = new JsonData();
        List<String> list = new ArrayList<String>();
        GsonOption option = new GsonOption();

        // 标题
        //option.title(chartTitle);
        // tooltip
        Tooltip tooltip = setBaseTooltip(ChartType.lineChart);
        option.setTooltip(tooltip);
        // 图例
        Legend legend = setBaseLegend(ChartType.lineChart, chartKey);
        option.setLegend(legend);
        // x轴分类
        CategoryAxis categoryX = setBaseXAxis(ChartType.lineChart, chartKey);
        option.xAxis(categoryX);
        // y轴分类
        ValueAxis categoryY = setBaseYAxis(ChartType.lineChart);
        option.yAxis(categoryY);
        // Grid
        Grid grid = setBaseGrid(ChartType.lineChart);
        option.setGrid(grid);
        // Series
        List<Series> series = setBaseSeries(ChartType.lineChart, chartKey);
        option.setSeries(series);

        list.add(option.toString());
        jsonData.setData(list);
        return jsonData;
    }

    @Override
    public JsonData getRaddarChartData(String chartKey) {
        JsonData jsonData = new JsonData();
        List<String> list = new ArrayList<String>();
        GsonOption option = new GsonOption();

        // tooltip
        Tooltip tooltip = setBaseTooltip(ChartType.raddarChart);
        option.setTooltip(tooltip);
        // 图例
        Legend legend = setBaseLegend(ChartType.raddarChart, chartKey);
        option.setLegend(legend);

        JSONObject tmp = new JSONObject(option.toString());
        // radar
        JSONObject jsonObject = setBaseRadar(chartKey);
        tmp.put("radar", jsonObject);
        // Series
        JSONArray jsonArray = setRaddarChartSeries(chartKey);
        tmp.put("series", jsonArray);

        list.add(tmp.toString());
        jsonData.setData(list);
        return jsonData;
    }

    @Override
    public JsonData getPieChartData(String chartKey) {
        JsonData jsonData = new JsonData();
        List<String> list = new ArrayList<String>();
        GsonOption option = new GsonOption();

        // tooltip
        Tooltip tooltip = setBaseTooltip(ChartType.pieChart);
        option.setTooltip(tooltip);
        // 图例
        Legend legend = setBaseLegend(ChartType.pieChart, chartKey);
        option.setLegend(legend);
        // calculable
        option.setCalculable(true);
        // Series
        List<Series> series = setBaseSeries(ChartType.pieChart, chartKey);
        option.setSeries(series);

        list.add(option.toString());
        jsonData.setData(list);
        return jsonData;
    }

    @Override
    public JsonData getBarChartData(String chartKey) {
        JsonData jsonData = new JsonData();
        List<String> list = new ArrayList<String>();
        GsonOption option = new GsonOption();

        // tooltip
        Tooltip tooltip = setBaseTooltip(ChartType.barChart);
        option.setTooltip(tooltip);
        // x轴分类
        CategoryAxis categoryX = setBaseXAxis(ChartType.barChart, chartKey);
        option.xAxis(categoryX);
        // y轴分类
        ValueAxis categoryY = setBaseYAxis(ChartType.barChart);
        option.yAxis(categoryY);
        // Grid
        Grid grid = setBaseGrid(ChartType.barChart);
        option.setGrid(grid);
        // Series
        List<Series> series = setBaseSeries(ChartType.barChart, chartKey);
        option.setSeries(series);

        list.add(option.toString());
        jsonData.setData(list);
        return jsonData;
    }

    @Override
    public Integer getPanelGroupBizData(String chartKey) {
        Integer data = -1;
        switch (chartKey) {
            case "test1":
                data = 102400;
                break;
            case "test2":
                data = 81212;
                break;
            case "test3":
                data = 9280;
                break;
            case "test4":
                data = 13600;
                break;
            default:
                break;
        }
        return data;
    }

    @Override
    public List<Integer> getLineChartBizData(String chartKey, String legend) {
        List<Integer> list = new ArrayList<Integer>();
        String condition = chartKey + "-" + legend;
        switch (condition.toLowerCase()) {
            case "test1-expected":
                list.add(100);
                list.add(120);
                list.add(161);
                list.add(134);
                list.add(105);
                list.add(160);
                list.add(165);
                break;
            case "test1-actual":
                list.add(120);
                list.add(82);
                list.add(91);
                list.add(154);
                list.add(162);
                list.add(140);
                list.add(145);
                break;
            case "test2-expected":
                list.add(200);
                list.add(192);
                list.add(120);
                list.add(144);
                list.add(160);
                list.add(130);
                list.add(140);
                break;
            case "test2-actual":
                list.add(180);
                list.add(160);
                list.add(151);
                list.add(106);
                list.add(145);
                list.add(150);
                list.add(130);
                break;
            case "test3-expected":
                list.add(80);
                list.add(100);
                list.add(121);
                list.add(104);
                list.add(105);
                list.add(90);
                list.add(100);
                break;
            case "test3-actual":
                list.add(120);
                list.add(90);
                list.add(100);
                list.add(138);
                list.add(142);
                list.add(130);
                list.add(130);
                break;
            case "test4-expected":
                list.add(130);
                list.add(140);
                list.add(141);
                list.add(142);
                list.add(145);
                list.add(150);
                list.add(160);
                break;
            case "test4-actual":
                list.add(120);
                list.add(82);
                list.add(91);
                list.add(154);
                list.add(162);
                list.add(140);
                list.add(130);
                break;
            default:
                break;
        }
        return list;
    }

    @Override
    public List<Integer> getRaddarChartBizData(String chartKey, String legend) {
        List<Integer> list = new ArrayList<Integer>();
        String condition = chartKey + "-" + legend;
        switch (condition.toLowerCase()) {
            case "test1-allocated budget":
                list.add(5000);
                list.add(7000);
                list.add(12000);
                list.add(11000);
                list.add(15000);
                list.add(14000);
                break;
            case "test1-expected spending":
                list.add(4000);
                list.add(9000);
                list.add(15000);
                list.add(15000);
                list.add(13000);
                list.add(11000);
                break;
            case "test1-actual spending":
                list.add(5500);
                list.add(11000);
                list.add(12000);
                list.add(15000);
                list.add(12000);
                list.add(12000);
                break;
            default:
                break;
        }
        return list;
    }

    @Override
    public List<PieSeriesDataDTO> getPieChartBizData(String chartKey) {
        List<PieSeriesDataDTO> list = new ArrayList<PieSeriesDataDTO>();
        switch (chartKey.toLowerCase()) {
            case "test1":
                PieSeriesDataDTO pieSeriesDataDTO = new PieSeriesDataDTO();
                pieSeriesDataDTO.setName("Industries");
                pieSeriesDataDTO.setValue(320);
                list.add(pieSeriesDataDTO);
                pieSeriesDataDTO = new PieSeriesDataDTO();
                pieSeriesDataDTO.setName("Technology");
                pieSeriesDataDTO.setValue(240);
                list.add(pieSeriesDataDTO);
                pieSeriesDataDTO = new PieSeriesDataDTO();
                pieSeriesDataDTO.setName("Forex");
                pieSeriesDataDTO.setValue(149);
                list.add(pieSeriesDataDTO);
                pieSeriesDataDTO = new PieSeriesDataDTO();
                pieSeriesDataDTO.setName("Gold");
                pieSeriesDataDTO.setValue(100);
                list.add(pieSeriesDataDTO);
                pieSeriesDataDTO = new PieSeriesDataDTO();
                pieSeriesDataDTO.setName("Forecasts");
                pieSeriesDataDTO.setValue(59);
                list.add(pieSeriesDataDTO);
                break;
            default:
                break;
        }
        return list;
    }

    @Override
    public List<Integer> getBarChartBizData(String chartKey, String legend) {
        List<Integer> list = new ArrayList<Integer>();
        String condition = chartKey + "-" + legend;
        switch (condition.toLowerCase()) {
            case "test1-pagea":
                list.add(79);
                list.add(52);
                list.add(200);
                list.add(334);
                list.add(390);
                list.add(330);
                list.add(220);
                break;
            case "test1-pageb":
                list.add(80);
                list.add(52);
                list.add(200);
                list.add(334);
                list.add(390);
                list.add(330);
                list.add(220);
                break;
            case "test1-pagec":
                list.add(30);
                list.add(52);
                list.add(200);
                list.add(334);
                list.add(390);
                list.add(330);
                list.add(220);
                break;
            default:
                break;
        }
        return list;
    }

    /**
     * 设置基本的Tooltip参数
     *
     * @param chartType
     * @return
     */
    private Tooltip setBaseTooltip(ChartType chartType) {
        Tooltip tooltip = new Tooltip();
        switch (chartType) {
            case lineChart:
                tooltip.setTrigger(Trigger.axis);
                AxisPointer axisPointer1 = new AxisPointer();
                axisPointer1.setType(PointerType.cross);
                tooltip.setAxisPointer(axisPointer1);
                Integer[] padding = new Integer[]{5, 10};
                tooltip.setPadding(padding);
                break;
            case raddarChart:
                tooltip.setTrigger(Trigger.axis);
                AxisPointer axisPointer2 = new AxisPointer();
                axisPointer2.setType(PointerType.shadow);
                tooltip.setAxisPointer(axisPointer2);
                break;
            case pieChart:
                tooltip.setTrigger(Trigger.item);
                String formatter = "{a} <br/>{b} : {c} ({d}%)";
                tooltip.setFormatter(formatter);
                break;
            case barChart:
                tooltip.setTrigger(Trigger.axis);
                AxisPointer axisPointer3 = new AxisPointer();
                axisPointer3.setType(PointerType.shadow);
                tooltip.setAxisPointer(axisPointer3);
                break;
            default:
                break;
        }
        return tooltip;
    }

    /**
     * 设置图例基本参数(测试使用)
     *
     * @param chartType
     * @return
     */
    private Legend setBaseLegend(ChartType chartType, String chartKey) {
        List<String> data = new ArrayList<String>();
        String left = "";
        String bottom = "";
        Legend legend = new Legend();
        switch (chartType) {
            case lineChart:
                String lineLegend = "expected,actual";
                if (StringUtils.isNotEmpty(dashboardConfigName)) {
                    lineLegend = (String) ConfigUtil.getConfigProp("lineLegend_" + chartKey, dashboardConfigName);
                }
                data = Arrays.asList(lineLegend.split(","));
                break;
            case raddarChart:
                String raddarLegend = "Allocated Budget,Expected Spending,Actual Spending";
                if (StringUtils.isNotEmpty(dashboardConfigName)) {
                    raddarLegend = (String) ConfigUtil.getConfigProp("raddarLegend_" + chartKey, dashboardConfigName);
                }
                data = Arrays.asList(raddarLegend.split(","));
                left = "center";
                bottom = "10";
                break;
            case pieChart:
                String pieLegend = "Industries,Technology,Forex,Gold,Forecasts";
                if (StringUtils.isNotEmpty(dashboardConfigName)) {
                    pieLegend = (String) ConfigUtil.getConfigProp("pieLegend_" + chartKey, dashboardConfigName);
                }
                data = Arrays.asList(pieLegend.split(","));
                left = "center";
                bottom = "10";
                break;
            case barChart:
                break;
            default:
                break;
        }
        legend.setData(data);
        if (StringUtils.isNotEmpty(left))
            legend.setLeft(left);
        if (StringUtils.isNotEmpty(bottom))
            legend.setBottom(bottom);
        return legend;
    }

    /**
     * 设置X轴基本分类参数
     *
     * @param chartType
     * @return
     */
    private CategoryAxis setBaseXAxis(ChartType chartType, String chartKey) {
        CategoryAxis categoryX = new CategoryAxis();
        List<String> data = new ArrayList<String>();
        switch (chartType) {
            case lineChart:
                AxisTick axisTick1 = new AxisTick();
                axisTick1.setShow(false);
                categoryX.setAxisTick(axisTick1);
                String lineXAxis = "Mon,Tue,Wed,Thu,Fri,Sat,Sun";
                if (StringUtils.isNotEmpty(dashboardConfigName)) {
                    lineXAxis = (String) ConfigUtil.getConfigProp("lineXAxis_" + chartKey, dashboardConfigName);
                }
                data = Arrays.asList(lineXAxis.split(","));
                categoryX.setData(data);
                categoryX.setBoundaryGap(false);
                break;
            case raddarChart:
                break;
            case pieChart:
                break;
            case barChart:
                categoryX.setType(AxisType.category);
                String barXAxis = "Mon,Tue,Wed,Thu,Fri,Sat,Sun";
                if (StringUtils.isNotEmpty(dashboardConfigName)) {
                    barXAxis = (String) ConfigUtil.getConfigProp("barXAxis_" + chartKey, dashboardConfigName);
                }
                data = Arrays.asList(barXAxis.split(","));
                categoryX.setData(data);
                break;
            default:
                break;
        }
        return categoryX;
    }

    /**
     * 设置Y轴基本分类参数
     *
     * @param chartType
     * @return
     */
    private ValueAxis setBaseYAxis(ChartType chartType) {
        ValueAxis categoryY = new ValueAxis();
        switch (chartType) {
            case lineChart:
                AxisTick axisTick1 = new AxisTick();
                axisTick1.setShow(false);
                categoryY.setAxisTick(axisTick1);
                break;
            case raddarChart:
                break;
            case pieChart:
                break;
            case barChart:
                categoryY.setType(AxisType.value);
                AxisTick axisTick2 = new AxisTick();
                axisTick2.setShow(false);
                categoryY.setAxisTick(axisTick2);
                break;
            default:
                break;
        }
        return categoryY;
    }

    /**
     * 设置Grid基本参数
     *
     * @param chartType
     * @return
     */
    private Grid setBaseGrid(ChartType chartType) {
        Grid grid = new Grid();
        switch (chartType) {
            case lineChart:
                grid.setTop(30);
                grid.setBottom(20);
                grid.setLeft(10);
                grid.setRight(10);
                grid.setContainLabel(true);
                break;
            case raddarChart:
                break;
            case pieChart:
                break;
            case barChart:
                grid.setTop(10);
                grid.setBottom("3%");
                grid.setLeft("2%");
                grid.setRight("2%");
                grid.setContainLabel(true);
                break;
            default:
                break;
        }
        return grid;
    }

    private JSONObject setBaseRadar(String chartKey) {
        JSONObject radar = new JSONObject();
        radar.put("radius", "66%");
        JSONArray center = new JSONArray();
        center.put("50%");
        center.put("42%");
        radar.put("center", center);
        radar.put("splitNumber", 8);

        JSONObject areaStyle = new JSONObject();
        areaStyle.put("color", "rgba(127,95,132,.3)");
        areaStyle.put("opacity", 1);
        areaStyle.put("shadowBlur", 45);
        areaStyle.put("shadowColor", "rgba(0,0,0,.5)");
        areaStyle.put("shadowOffsetX", 0);
        areaStyle.put("shadowOffsetY", 15);
        JSONObject splitArea = new JSONObject();
        splitArea.put("areaStyle", areaStyle);
        radar.put("splitArea", splitArea);

        String indicatorName = "Sales,Administration,Information Techology,Customer Support,Development,Marketing";
        String indicatorMax = "10000,20000,20000,20000,20000,20000";
        if (StringUtils.isNotEmpty(dashboardConfigName)) {
            indicatorName = (String) ConfigUtil.getConfigProp("indicatorName_" + chartKey, dashboardConfigName);
            indicatorMax = (String) ConfigUtil.getConfigProp("indicatorMax_" + chartKey, dashboardConfigName);
        }
        String[] indicatorNames = indicatorName.split(",");
        String[] indicatorMaxs = indicatorMax.split(",");
        JSONArray indicator = new JSONArray();
        for (int i = 0; i < indicatorNames.length; i++) {
            JSONObject indicatorProp = new JSONObject();
            indicatorProp.put("name", indicatorNames[i]);
            indicatorProp.put("max", Integer.parseInt(indicatorMaxs[i]));
            indicator.put(indicatorProp);
        }
        radar.put("indicator", indicator);
        return radar;
    }

    /**
     * 设置Series基本参数
     *
     * @param chartType
     * @param chartKey
     * @return
     */
    private List<Series> setBaseSeries(ChartType chartType, String chartKey) {
        List<Series> series = new ArrayList<Series>();
        switch (chartType) {
            case lineChart:
                String lineLegend = "expected,actual";
                if (StringUtils.isNotEmpty(dashboardConfigName)) {
                    lineLegend = (String) ConfigUtil.getConfigProp("lineLegend_" + chartKey, dashboardConfigName);
                }
                String[] lineLegends = lineLegend.split(",");
                Line line1 = new Line();
                line1.setName(lineLegends[0]);
                ItemStyle itemStyle = new ItemStyle();
                Normal normal = new Normal();
                normal.setColor("#FF005A");
                LineStyle lineStyle = new LineStyle();
                lineStyle.setColor("#FF005A");
                lineStyle.setWidth(2);
                normal.setLineStyle(lineStyle);
                itemStyle.setNormal(normal);
                line1.setItemStyle(itemStyle);
                line1.setSmooth(true);
                line1.setType(SeriesType.line);
                List<Integer> list1 = getLineChartBizData(chartKey, lineLegends[0]);
                line1.setData(list1);
                line1.setAnimationDuration(2800);
                line1.setAnimationEasing("cubicInOut");
                series.add(line1);

                Line line2 = new Line();
                line2.setName(lineLegends[1]);
                itemStyle = new ItemStyle();
                normal = new Normal();
                normal.setColor("#3888fa");
                lineStyle = new LineStyle();
                lineStyle.setColor("#3888fa");
                lineStyle.setWidth(2);
                normal.setLineStyle(lineStyle);
                AreaStyle areaStyle = new AreaStyle();
                areaStyle.setColor("#f3f8ff");
                normal.setAreaStyle(areaStyle);
                itemStyle.setNormal(normal);
                line2.setItemStyle(itemStyle);
                line2.setSmooth(true);
                line2.setType(SeriesType.line);
                List<Integer> list2 = getLineChartBizData(chartKey, lineLegends[1]);
                line2.setData(list2);
                line2.setAnimationDuration(2800);
                line2.setAnimationEasing("quadraticOut");
                series.add(line2);
                break;
            case raddarChart:
                break;
            case pieChart:
                Pie pie = new Pie();
                String pieSeriesName = "WEEKLY WRITE ARTICLES";
                if (StringUtils.isNotEmpty(dashboardConfigName)) {
                    pieSeriesName = (String) ConfigUtil.getConfigProp("pieSeriesName_" + chartKey, dashboardConfigName);
                }
                pie.setName(pieSeriesName);
                pie.setType(SeriesType.pie);
                pie.setRoseType(RoseType.radius);
                Integer[] radius = new Integer[]{15, 95};
                pie.setRadius(radius);
                String[] center = new String[]{"50%", "38%"};
                pie.setCenter(center);
                List<PieSeriesDataDTO> list3 = getPieChartBizData(chartKey);
                pie.setData(list3);
                pie.setAnimationEasing("cubicInOut");
                pie.setAnimationDuration(2600);
                series.add(pie);
                break;
            case barChart:
                String barLegend = "pageA,pageB,pageC";
                if (StringUtils.isNotEmpty(dashboardConfigName)) {
                    barLegend = (String) ConfigUtil.getConfigProp("barLegend_" + chartKey, dashboardConfigName);
                }
                String[] barLegends = barLegend.split(",");
                for (int i = 0; i < barLegends.length; i++) {
                    Bar bar = new Bar();
                    bar.setName(barLegends[i]);
                    bar.setType(SeriesType.bar);
                    bar.setStack("vistors");
                    bar.setBarWidth(60);
                    bar.setAnimationDuration(6000);
                    List<Integer> list4 = getBarChartBizData(chartKey, barLegends[i]);
                    bar.setData(list4);
                    series.add(bar);
                }
                break;
            default:
                break;
        }
        return series;
    }

    private JSONArray setRaddarChartSeries(String chartKey) {
        JSONArray series = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "radar");
        jsonObject.put("symbolSize", 0);

        JSONObject normal = new JSONObject();
        normal.put("shadowBlur", 13);
        normal.put("shadowColor", "rgba(0,0,0,.2)");
        normal.put("shadowOffsetX", 0);
        normal.put("shadowOffsetY", 10);
        normal.put("opacity", 1);
        JSONObject areaStyle = new JSONObject();
        areaStyle.put("normal", normal);
        jsonObject.put("areaStyle", areaStyle);

        JSONArray jsonArray = new JSONArray();
        String raddarLegend = "Allocated Budget,Expected Spending,Actual Spending";
        if (StringUtils.isNotEmpty(dashboardConfigName)) {
            raddarLegend = (String) ConfigUtil.getConfigProp("raddarLegend_" + chartKey, dashboardConfigName);
        }
        String[] raddarLegends = raddarLegend.split(",");
        for (int i = 0; i < raddarLegends.length; i++) {
            String key = raddarLegends[i];
            List<Integer> value = getRaddarChartBizData(chartKey, key);
            JSONObject data = new JSONObject();
            data.put("name", key);
            data.put("value", value);
            jsonArray.put(data);
        }

        jsonObject.put("data", jsonArray);
        jsonObject.put("animationDuration", 3000);

        series.put(jsonObject);
        return series;
    }

    private DashboardConfigDTO setDefaultValue() {
        DashboardConfigDTO config = new DashboardConfigDTO();
        config.setHasPanelGroup(true);
        config.setPanelCount(4);
        List<PanelGroupDTO> list = new ArrayList<PanelGroupDTO>();
        PanelGroupDTO panelGroupDTO = new PanelGroupDTO();
        panelGroupDTO.setKey("test1");
        panelGroupDTO.setIconName("peoples");
        panelGroupDTO.setText("Test1");
        panelGroupDTO.setEndVal(102400);
        panelGroupDTO.setDuration(2600);
        list.add(panelGroupDTO);
        panelGroupDTO = new PanelGroupDTO();
        panelGroupDTO.setKey("test2");
        panelGroupDTO.setIconName("message");
        panelGroupDTO.setText("Test2");
        panelGroupDTO.setEndVal(81212);
        panelGroupDTO.setDuration(3000);
        list.add(panelGroupDTO);
        panelGroupDTO = new PanelGroupDTO();
        panelGroupDTO.setKey("test3");
        panelGroupDTO.setIconName("money");
        panelGroupDTO.setText("Text3");
        panelGroupDTO.setEndVal(9280);
        panelGroupDTO.setDuration(3200);
        list.add(panelGroupDTO);
        panelGroupDTO = new PanelGroupDTO();
        panelGroupDTO.setKey("test4");
        panelGroupDTO.setIconName("shoppingCard");
        panelGroupDTO.setText("Text4");
        panelGroupDTO.setEndVal(13600);
        panelGroupDTO.setDuration(3600);
        list.add(panelGroupDTO);
        config.setPanels(list);
        return config;
    }

    private DashboardConfigDTO readConfigValue() {
        DashboardConfigDTO config = new DashboardConfigDTO();
        Dictionary<String, Object> dictionary = ConfigUtil.getAllConfig(dashboardConfigName);
        Boolean hasPanelGroup = Boolean.parseBoolean((String) dictionary.get("hasPanelGroup"));
        config.setHasPanelGroup(hasPanelGroup);
        Integer panelCount = Integer.parseInt((String) dictionary.get("panelCount"));
        config.setPanelCount(panelCount);
        List<PanelGroupDTO> list = new ArrayList<PanelGroupDTO>();
        for (int i = 0; i < panelCount; i++) {
            String key = (String) dictionary.get("panelKey" + String.valueOf(i + 1));
            String iconName = (String) dictionary.get("iconName" + String.valueOf(i + 1));
            String text = (String) dictionary.get("text" + String.valueOf(i + 1));
            Integer duration = Integer.parseInt((String) dictionary.get("duration" + String.valueOf(i + 1)));
            PanelGroupDTO panelGroupDTO = new PanelGroupDTO();
            panelGroupDTO.setKey(key);
            panelGroupDTO.setIconName(iconName);
            panelGroupDTO.setText(text);
            panelGroupDTO.setDuration(duration);
            Integer endVal = getPanelGroupBizData(key);
            panelGroupDTO.setEndVal(endVal);
            list.add(panelGroupDTO);
        }
        config.setPanels(list);
        return config;
    }

    public void setDashboardConfigName(String dashboardConfigName) {
        this.dashboardConfigName = dashboardConfigName;
    }
}
