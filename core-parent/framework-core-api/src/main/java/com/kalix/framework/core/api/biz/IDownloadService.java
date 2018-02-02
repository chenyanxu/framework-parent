package com.kalix.framework.core.api.biz;

/**
 * Created by hqj on 2018/2/2.
 */
public interface IDownloadService extends IBizService {
    String[] createDownloadFile(Long entityId);
}
