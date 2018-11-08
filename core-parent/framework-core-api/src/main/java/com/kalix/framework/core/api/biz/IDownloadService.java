package com.kalix.framework.core.api.biz;

import com.kalix.framework.core.api.IService;

/**
 * Created by hqj on 2018/2/2.
 */
public interface IDownloadService extends IService {
    String[] createDownloadFile(String entityId, String fileType);
}
