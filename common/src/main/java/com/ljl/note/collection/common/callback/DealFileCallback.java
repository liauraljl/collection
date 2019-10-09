package com.ljl.note.collection.common.callback;

@FunctionalInterface
public interface DealFileCallback<T>{
    void callBack(T t);
}