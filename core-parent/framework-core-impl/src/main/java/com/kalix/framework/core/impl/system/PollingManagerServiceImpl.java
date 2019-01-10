package com.kalix.framework.core.impl.system;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.kalix.framework.core.api.system.IPollingManagerService;
import com.kalix.framework.core.api.system.IPollingService;
import com.kalix.framework.core.api.system.model.PollingBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zangyanming on 2016/2/25.
 */
public class PollingManagerServiceImpl implements IPollingManagerService {
    @Override
    public List<PollingBean> getPollingList() {
        List<PollingBean> pollingBeans = new ArrayList<>();
        List<IPollingService> pollingList = PollingManager.getInstall().getPollingList();
        if (pollingList != null && pollingList.size() > 0) {

            for (IPollingService polling : pollingList) {
                PollingBean pollingBean = DozerBeanMapperBuilder.buildDefault().map(polling, PollingBean.class);
                pollingBeans.add(pollingBean);
            }
        }
        return pollingBeans;
    }
}
