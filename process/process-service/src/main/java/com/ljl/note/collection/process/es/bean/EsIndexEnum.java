package com.ljl.note.collection.process.es.bean;

import lombok.Getter;

/**
 * @description: EsIndexEnum
 */
@Getter
public enum EsIndexEnum {
    NODE_USER("node_user","流程节点人群"),
    ;

    private String indexName;
    private String desc;

    EsIndexEnum(String indexName, String desc) {
        this.indexName = indexName;
        this.desc = desc;
    }
}
