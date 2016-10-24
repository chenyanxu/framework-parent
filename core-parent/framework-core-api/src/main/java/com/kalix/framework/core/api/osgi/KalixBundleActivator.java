package com.kalix.framework.core.api.osgi;

import com.kalix.framework.core.util.ConfigUtil;

/**
 * @author chenyanxu
 */
public abstract class KalixBundleActivator extends BaseBundleActivator{
    protected String contextPath;
    protected Boolean deploy;

    public KalixBundleActivator() {
        contextPath = (String) ConfigUtil.getConfigProp("path", "ConfigWebContext");
        //if the deploy var is true
        //the child class will deploy the compress version code
        deploy = Boolean.valueOf((String)ConfigUtil.getConfigProp("deploy","ConfigWebContext"));

        if (contextPath.equals("/")) {
            contextPath = "";
        }
    }
}
