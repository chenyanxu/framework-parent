package com.kalix.framework.core.impl.biz;

import com.kalix.framework.core.api.biz.ITreeBizService;
import com.kalix.framework.core.api.dao.IBaseTreeEntityDao;
import com.kalix.framework.core.api.persistence.BaseTreeEntity;
import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.api.web.model.BaseTreeDTO;
import com.kalix.framework.core.util.Assert;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import javax.transaction.Transactional;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hqj
 */
public abstract class TreeShiroGenericBizServiceImpl<T extends IBaseTreeEntityDao, TP extends BaseTreeEntity> extends ShiroGenericBizServiceImpl<T, TP> implements ITreeBizService {

    protected String functionName = "实体";
    protected Class<T> baseTreeEntityDaoClass;

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public TreeShiroGenericBizServiceImpl() {
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        java.lang.reflect.Type type = genericSuperclass.getActualTypeArguments()[0];
        if (type instanceof Class) {
            this.baseTreeEntityDaoClass = (Class<T>) type;
        } else if (type instanceof ParameterizedType) {
            this.baseTreeEntityDaoClass = (Class<T>) ((ParameterizedType) type).getRawType();
        }
    }

    @Override
    public void beforeSaveEntity(TP entity, JsonStatus status) {
        Assert.notNull(entity, "实体不能为空.");

        String userName = shiroService.getCurrentUserLoginName();
        if (userName != null) {
            entity.setCreateBy(userName);
            entity.setUpdateBy(userName);
        }
    }

    @Override
    @Transactional
    public void afterSaveEntity(TP entity, JsonStatus status) {
        Assert.notNull(entity, "实体不能为空.");

        if (entity.getParentId() != -1) {
            TP parentEntity = (TP) dao.get(entity.getParentId());
            if (parentEntity != null && parentEntity.getIsLeaf() == 1) {
                parentEntity.setIsLeaf(0L);
                dao.save(parentEntity);
            }
        }
    }

    @Override
    public boolean isUpdate(TP entity, JsonStatus status) {
        Assert.notNull(entity, "实体不能为空.");

        // 校验实体名称
        List<TP> entities = dao.findByName(entity.getParentId(), entity.getId(), entity.getName());
        if (entities != null && entities.size() > 0) {
            status.setFailure(true);
            status.setMsg(functionName + "名称已经存在！");
            return false;
        }
        // 校验实体代码
        /*entities = dao.findByCode(entity.getId(), entity.getCode());
        if (entities != null && entities.size() > 0) {
            status.setFailure(true);
            status.setMsg(functionName + "代码已经存在！");
            return false;
        }*/
        return true;
    }

    @Override
    public boolean isSave(TP entity, JsonStatus status) {
        Assert.notNull(entity, "实体不能为空.");

        // 校验实体名称
        List<TP> entities = dao.findByName(entity.getParentId(), 0L, entity.getName());
        if (entities != null && entities.size() > 0) {
            status.setSuccess(false);
            status.setMsg(functionName + "名称已经存在！");
            return false;
        }
        // 校验实体代码
        /*entities = dao.findByCode(0L, entity.getCode());
        if (entities != null && entities.size() > 0) {
            status.setSuccess(false);
            status.setMsg(functionName + "代码已经存在！");
            return false;
        }*/
        String code = CodeUtil.createCode(this.baseTreeEntityDaoClass, entity.getParentId());
        if (code.isEmpty()) {
            status.setSuccess(false);
            status.setMsg(functionName + "代码生成错误！");
            return false;
        }
        else {
            entity.setCode(code);
        }
        return true;
    }

    @Override
    public boolean isDelete(Long entityId, JsonStatus status) {
        if (dao.get(entityId) == null) {
            status.setFailure(true);
            status.setMsg(functionName + "已经被删除！");
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public void doDelete(long id, JsonStatus jsonStatus) {
        try {
            TP entity = (TP) dao.get(id);
            if (entity != null) {
                // 删除子节点
                removeChildren(id);
                // 删除自己
                dao.remove(id);
                // 更新父节点
                updateParent(entity.getParentId());

                jsonStatus.setSuccess(true);
                jsonStatus.setMsg("删除" + functionName + "成功！");
            } else {
                jsonStatus.setFailure(true);
                jsonStatus.setMsg(functionName + "不存在或已被删除！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonStatus.setFailure(true);
            jsonStatus.setMsg(functionName + "删除失败！");
        }
    }

    /**
     * 如果父节点下再没有子节点,将更新父节点状态
     *
     * @param parentId
     */
    @Transactional
    public void updateParent(Long parentId) {
        if (parentId != -1) {
            // 获取父节点
            TP parentEntity = (TP) dao.get(parentId);
            if (parentEntity != null) {
                // 获取父节点下的所有子节点
                List<TP> children = dao.findByParentId(parentId);
                if (children == null || children.isEmpty()) {
                    parentEntity.setIsLeaf(1L);
                    dao.save(parentEntity);
                }
            }
        }
    }

    @Transactional
    public void removeChildren(Long id) {
        List<TP> children = dao.findByParentId(id);
        if (children != null && !children.isEmpty()) {
            children.stream()
                    .forEach(n -> {
                        removeChildren(n.getId());
                        this.deleteEntity(n.getId());
                        //dao.remove(n.getId());
                    });
        }
    }

    @Override
    public void beforeUpdateEntity(TP entity, JsonStatus status) {
        super.beforeUpdateEntity(entity, status);
    }

    @Override
    @Transactional
    public JsonStatus updateEntity(TP entity) {
        JsonStatus jsonStatus = new JsonStatus();
        try {
            if (isUpdate(entity, jsonStatus)) {
                TP oldEntity = (TP) dao.get(entity.getId());
                oldEntity.setName(entity.getName());
                /*oldEntity.setCode(entity.getCode());*/
                oldEntity.setUpdateBy(shiroService.getCurrentUserLoginName());
                dao.save(oldEntity);
                jsonStatus.setSuccess(true);
                jsonStatus.setMsg("更新" + functionName + "成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonStatus.setFailure(true);
            jsonStatus.setMsg("更新" + functionName + "失败！");
        }
        return jsonStatus;
    }

    /**
     * 获取整个树结构
     * @return
     */
    @Override
    public BaseTreeDTO getAllTree() {
        List<TP> entities = (List<TP>) dao.getAll().stream()
                .sorted(Compare.<TP>compare()
                        .thenComparing((a, b) -> a.getCode().compareTo(b.getCode())))
                .collect(Collectors.toList());

        return generateRoot(entities, -1L);
    }

    /**
     * 获取指定树节点id下的树结构
     * @param nodeId
     * @return
     */
    @Override
    public BaseTreeDTO getTreeByNodeId(Long nodeId) {
        TP entity = (TP) dao.get(nodeId);
        if (entity != null) {
            return generateRoot(dao.findByCode(entity.getCode()), nodeId);
        }
        else {
            return null;
        }
    }

    /**
     * 根据当前树节点id，找到其父id路径(包括当前节点id,不包括-1)
     * @param nodeId
     * @return
     */
    @Override
    public String getParentIdPath(Long nodeId) {
        return getParentIdPath(nodeId, "");
    }

    private BaseTreeDTO generateRoot(List<TP> entities, Long id) {

        BaseTreeDTO root = new BaseTreeDTO();
        Mapper mapper = new DozerBeanMapper();
        //String parentName = "根课程类型";
        String parentName = "根";

        for (TP entity : entities) {
            if (entity.getId() == id) {
                root = mapper.map(entity, BaseTreeDTO.class);
                root.setText(entity.getName());
                parentName = entity.getName();
                break;
            }
        }

        root.setId(id);

        if (entities != null && entities.size() > 0) {
            List<TP> rootElements = getRootElements(entities, id);
            if (rootElements != null && rootElements.size() > 0) {
                for (TP rootElement : rootElements) {
                    BaseTreeDTO entityDTO = mapper.map(rootElement, BaseTreeDTO.class);
                    entityDTO.setLeaf(rootElement.getIsLeaf() != 0);
                    entityDTO.setParentName(parentName);
                    entityDTO.setText(rootElement.getName());
                    getChilden(entityDTO, entities, mapper, true);
                    root.getChildren().add(entityDTO);
                }
            }
        }

        return root;
    }

    /**
     * 获得所有根节点
     *
     * @param elements
     * @return
     */
    private List<TP> getRootElements(List<TP> elements, Long id) {
        return elements.stream().filter(n->n.getParentId().equals(id))
                .collect(Collectors.toList());
    }

    /**
     * 递归函数加载子节点
     *
     * @param root
     * @param elements
     */
    private void getChilden(BaseTreeDTO root, List<TP> elements, Mapper mapper, boolean isRecursion) {

        List<BaseTreeDTO> children = new ArrayList<>();
        elements.stream().filter(n -> root.getId() != -1 && (root.getId() == n.getParentId()))
                .forEach(n -> {
                    BaseTreeDTO entityDTO = mapper.map(n, BaseTreeDTO.class);
                    entityDTO.setLeaf(n.getIsLeaf() != 0);
                    entityDTO.setParentName(root.getName());
                    entityDTO.setText(n.getName());
                    children.add(entityDTO);

                    if (isRecursion && n.getIsLeaf() == 0) {
                        getChilden(entityDTO, elements, mapper, isRecursion);
                    }
                });
        root.setChildren(children);
    }

    /**
     * 递归获取父节点路径，包括当前节点，不包括-1
     * @param nodeId
     * @param parentIdPath
     * @return
     */
    private String getParentIdPath(Long nodeId, String parentIdPath) {
        TP entity = this.getEntity(nodeId);
        Long parentId = entity.getParentId();
        if (parentIdPath.equals("")) {
            parentIdPath = nodeId.toString();
        } else {
            parentIdPath = nodeId.toString() + "," + parentIdPath;
        }
        if (parentId != -1L) {
            parentIdPath = getParentIdPath(parentId, parentIdPath);
        }
        return parentIdPath;
    }
}
