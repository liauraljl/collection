package com.ljl.note.collection.common.utils;

@FunctionalInterface
public interface DealFileCallback<T>{
    void callBack(T t);
}