package com.ljl.note.collection.process.es.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: EsModel  ES交互的父类
 */
@Data
public class EsModel implements Serializable {
    private static final long serialVersionUID = -4304089053858556066L;

    /**
     * ES主键ID
     */
    private String id;
}
