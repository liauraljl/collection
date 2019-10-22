package com.ljl.note.collection.process.service.es;

import com.ljl.note.collection.process.es.bean.EsModel;
import lombok.Data;

/**
 * @description: EsNodeUser  人群信息
 */
@Data
public class EsNodeUser extends EsModel {
    private static final long serialVersionUID = -6721196516803229170L;

    /**
     * 节点ID
     */
    private String nodeId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 加入时间
     */
    private Long createTime;

    /**
     * 获取唯一ID
     * @return
     */
    @Override
    public String getId(){
        return nodeId + userId;
    }
}
