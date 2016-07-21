package com.kalix.framework.webapp.cg;

import com.kalix.framework.core.api.persistence.JsonStatus;

/**
 * Created by Administrator on 2016/7/20.
 */
public class CodeGeneration {

    public JsonStatus addTemplate(TemplateBean bean){
        return JsonStatus.successResult("test for cg");
    }

    public TemplateBean getTemplate(){
        return new TemplateBean();
    }
}
