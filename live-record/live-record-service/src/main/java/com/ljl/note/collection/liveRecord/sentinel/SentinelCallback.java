package com.ljl.note.collection.liveRecord.sentinel;

@FunctionalInterface
public interface SentinelCallback<K,T> {
    K callBack(T t);
}
