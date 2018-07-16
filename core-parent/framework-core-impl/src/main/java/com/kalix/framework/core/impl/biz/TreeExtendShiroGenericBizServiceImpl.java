package com.kalix.framework.core.impl.biz;

import com.kalix.framework.core.api.biz.ITreeExtendBizService;
import com.kalix.framework.core.api.dao.IBaseTreeExtendEntityDao;
import com.kalix.framework.core.api.persistence.BaseTreeExtendEntity;
import com.kalix.framework.core.api.persistence.JsonData;
import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.util.SerializeUtil;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hqj
 */
public abstract class TreeExtendShiroGenericBizServiceImpl<T extends IBaseTreeExtendEntityDao, TP extends BaseTreeExtendEntity> extends ShiroGenericBizServiceImpl<T, TP> implements ITreeExtendBizService<TP> {

    protected String treeName = "树节点";
    protected String beanName = "实体";
    private JsonStatus jsonStatus = new JsonStatus();

    public TreeExtendShiroGenericBizServiceImpl() {
    }

    @Override
    public JsonData getEntitiesByFK(Long fk, Integer page, Integer limit, String jsonStr, String sort) {
        return this.dao.findByTreeId(fk, page, limit, jsonStr, sort);
    }

    @Override
    @Transactional
    public JsonStatus saveEntityByFK(Long fk, TP entity) {
        if (fk.equals(entity.getTreeId())) {
            return super.saveEntity(entity);
        } else {
            jsonStatus.setSuccess(false);
            jsonStatus.setMsg("选择的" + treeName + "不符合要求,添加" + beanName + "失败!");
            return jsonStatus;
        }
    }

    @Override
    @Transactional
    public JsonStatus updateEntityByFK(Long fk, TP entity) {
        if (fk.equals(entity.getTreeId())) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("id", String.valueOf(entity.getId()));
            params.put("treeid", String.valueOf(fk));
            String jsonStr = SerializeUtil.serializeJson(params);
            JsonData jsonData = super.getAllEntityforReport(jsonStr);
            if (jsonData.getTotalCount() > 0) {
                jsonStatus = super.updateEntity(entity);
            } else {
                jsonStatus.setSuccess(false);
                jsonStatus.setMsg("未查询到该" + beanName + ",更新失败!");
            }
        } else {
            jsonStatus = super.updateEntity(entity);
        }
        return jsonStatus;
    }

    @Override
    @Transactional
    public JsonStatus deleteEntityByFK(Long fk, Long entityId) {
        return super.deleteEntity(entityId);
    }

    @Override
    public boolean isDelete(Long entityId, JsonStatus status) {
        return true;
    }

    public void setTreeName(String treeName) {
        this.treeName = treeName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
