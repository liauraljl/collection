package com.ljl.note.collection.support.common;

import org.springframework.data.redis.core.ZSetOperations;

import java.io.Serializable;

public class ZSetModel implements Serializable, ZSetOperations.TypedTuple<String> {
    private static final long serialVersionUID = 6450499012899208955L;

    private String value;

    private double score = System.currentTimeMillis();

    public ZSetModel(String value, double score) {
        this.value = value;
        this.score = score;
    }

    @Override
    public int compareTo(ZSetOperations.TypedTuple<String> typedTuple) {
        ZSetModel zSetModel = (ZSetModel) typedTuple;
        return new Double(this.score).compareTo(zSetModel.getScore());
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public Double getScore() {
        return score;
    }
}
