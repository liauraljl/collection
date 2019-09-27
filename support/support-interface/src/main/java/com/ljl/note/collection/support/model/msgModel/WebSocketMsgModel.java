package com.ljl.note.collection.support.model.msgModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 消息类型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketMsgModel<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public Integer msgType;

    public T data;
}
