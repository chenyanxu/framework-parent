package com.kalix.framework.osgi.core;

import com.kalix.framework.osgi.api.IBundleService;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenyanxu
 */
public class BundleServiceImpl implements IBundleService {
    private BundleContext bundleContext;
    //private BundleJsonStatus jsonStatus;
    private Map<String, Object> rtnMap;
    private List<Bundle> appBundles = null;

    public BundleServiceImpl() {
        this.rtnMap = new HashMap<>();
        this.appBundles = new ArrayList<>();
    }

    @Override
    public Map startApp(String id) {
        this.rtnMap.clear();
        getBundleList();

        if (appBundles.size() > 0) {
            try {
                for (Bundle bundle : this.appBundles) {
                    if (bundle.getHeaders().get("Bundle-ApplicationId").equals(id)) {
                        bundle.start();
                        rtnMap.put("success", true);
                        rtnMap.put("msg", "服务启动成功");
                        return rtnMap;
                    }
                }
            } catch (BundleException e) {
                e.printStackTrace();
                rtnMap.put("success", false);
                rtnMap.put("msg", "服务启动失败{" + e.getMessage() + "}");
            }
        }

        rtnMap.put("success", false);
        rtnMap.put("msg", "服务不存在");

        return rtnMap;

//        Bundle[] bundles = bundleContext.getBundles();
//
//        for (int idx = 0; idx < bundles.length; ++idx) {
//            if (bundles[idx].getSymbolicName().matches("\\S*" + id + ".web")) {
//                try {
//                    bundles[idx].start();
//                    //jsonStatus = BundleJsonStatus.successResult("服务启动成功");
//                    rtnMap.put("success", true);
//                    rtnMap.put("msg", "服务启动成功");
//                } catch (BundleException e) {
//                    e.printStackTrace();
//                    //jsonStatus = BundleJsonStatus.failureResult("服务启动失败");
//                    rtnMap.put("success", false);
//                    rtnMap.put("msg", "服务启动失败");
//                }
//
//                return rtnMap;
//            }
//        }


    }

    @Override
    public Map stopApp(String id) {
        this.rtnMap.clear();
        getBundleList();

        if (appBundles.size() > 0) {
            try {
                for (Bundle bundle : this.appBundles) {
                    if (bundle.getHeaders().get("Bundle-ApplicationId").equals(id)) {
                        bundle.stop();
                        rtnMap.put("success", true);
                        rtnMap.put("msg", "服务停止成功");
                        return rtnMap;
                    }
                }
            } catch (BundleException e) {
                e.printStackTrace();
                rtnMap.put("success", false);
                rtnMap.put("msg", "服务停止失败{" + e.getMessage() + "}");
            }
        }

        rtnMap.put("success", false);
        rtnMap.put("msg", "服务不存在");

        return rtnMap;
//        Bundle[] bundles = bundleContext.getBundles();
//
//        for (int idx = 0; idx < bundles.length; ++idx) {
//            if (bundles[idx].getSymbolicName().matches("\\S*" + id + ".web")) {
//                try {
//                    bundles[idx].stop();
//                    //jsonStatus = BundleJsonStatus.successResult("服务停止成功");
//                    rtnMap.put("success", true);
//                    rtnMap.put("msg", "服务停止成功");
//                } catch (BundleException e) {
//                    e.printStackTrace();
//                    //jsonStatus = BundleJsonStatus.failureResult("服务停止失败");
//                    rtnMap.put("success", false);
//                    rtnMap.put("msg", "服务停止失败");
//                }
//
//                return rtnMap;
//            }
//        }


    }

    @Override
    public Map getAppStatus(String appIds) {
        this.rtnMap.clear();
        getBundleList();
        String[] appIdArray = null;

        if (appIds != null) {
            appIdArray = appIds.split("_");

            for (int idx = 0; idx < appIdArray.length; ++idx) {
                for (Bundle bundle : this.appBundles) {
                    if (bundle.getHeaders().get("Bundle-ApplicationId").equals(appIdArray[idx])) {
                        if (bundle.getState() == Bundle.ACTIVE) {
                            this.rtnMap.put(appIdArray[idx], true);
                        } else {
                            this.rtnMap.put(appIdArray[idx], false);
                        }

                        break;
                    }
                }
            }
        }

        if (this.rtnMap.size() > 0) {
            this.rtnMap.put("success", true);
        } else {
            this.rtnMap.put("success", false);
        }

        return rtnMap;
    }


    public BundleContext getBundleContext() {
        return bundleContext;
    }

    public void setBundleContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;



    }

    /**
     * 获得IApplication服务
     */
    private void getBundleList() {
        this.appBundles.clear();
        for (Bundle bundle : this.bundleContext.getBundles()) {
            if ("IApplication".equals(bundle.getHeaders().get("Bundle-Classifier"))) {
                this.appBundles.add(bundle);
            }
        }
    }
}
