package com.ljl.note.collection.imCommon.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * netty 消息体
 */
@Data
public class TranslatorData implements Serializable {

    private static final long serialVersionUID = 1773460827606922639L;

    private String id;

    private String name;

    private String message;
}
