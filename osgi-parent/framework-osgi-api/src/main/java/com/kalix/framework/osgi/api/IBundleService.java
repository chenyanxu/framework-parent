package com.kalix.framework.osgi.api;

import java.util.Map;

/**
 * @author chenyanxu
 */
public interface IBundleService {
    Map startApp(String id);

    Map stopApp(String id);

    Map getAppStatus(String appIds);//connect by '_'
}
