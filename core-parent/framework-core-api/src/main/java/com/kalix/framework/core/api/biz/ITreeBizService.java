package com.kalix.framework.core.api.biz;

import com.kalix.framework.core.api.IService;
import com.kalix.framework.core.api.web.model.BaseTreeDTO;

/**
 * @类描述： 对外业务服务的根接口
 * @创建人：hqj
 * @创建时间：2017-12-04 下午1:30:00
 * @修改人：sunlf
 * @修改时间：2017-12-04 下午1:30:00
 * @修改备注：
 */


public interface ITreeBizService extends IService {

    /**
     * 获取整个树结构
     * @return
     */
    BaseTreeDTO getAllTree();

    /**
     * 获取指定树节点id下的树结构
     * @param nodeId
     * @return
     */
    BaseTreeDTO getTreeByNodeId(String nodeId);

    /**
     * 根据当前树节点id，找到其父id路径
     *
     * @param nodeId
     * @return
     */
    String getParentIdPath(String nodeId);
}
