package com.ljl.note.collection.process.common;

import lombok.Getter;

/**
 * @description: MsCloudPlugBaseCode
 */
@Getter
public enum MsCloudPlugBaseCode {
    SUCCESS("000000","请求成功"),
    FAIL("999999","系统异常"),
    /** 枚举验证 **/
    ACTION_TYPE_NOT_EXISIT("100001","动作类型不存在"),
    RELATION_TYPE_NOT_EXISIT("100002","关系类型不存在"),

    /** ES**/
    ES_ADDRESS_ERROR("200001","ES地址异常"),
    ES_CLIENT_NULL("200002","ESClient初始化失败，为空"),
    ES_CONFIG_NULL("200003","没有配置ES信息"),
    ES_CREATE_INDEX_ERROR("200004","ES创建索引异常"),
    ES_UPSET_MODEL_ERROR("200005","ES新增修改数据异常"),
    ES_DELETE_MODEL_ERROR("200006","ES删除数据异常"),
    ;
    private String code;

    private String codeMsg;

    MsCloudPlugBaseCode(String code, String codeMsg) {
        this.code = code;
        this.codeMsg = codeMsg;
    }
}
