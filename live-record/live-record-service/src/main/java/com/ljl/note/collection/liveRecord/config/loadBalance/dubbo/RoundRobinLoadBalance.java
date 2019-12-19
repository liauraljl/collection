package com.ljl.note.collection.liveRecord.config.loadBalance.dubbo;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.AtomicPositiveInteger;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RoundRobinLoadBalance extends AbstractLoadBalance {
    public static final String NAME = "roundrobin";
    private final ConcurrentMap<String, AtomicPositiveInteger> sequences = new ConcurrentHashMap();
    private final ConcurrentMap<String, AtomicPositiveInteger> weightSequences = new ConcurrentHashMap();

    public RoundRobinLoadBalance() {
    }

    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        String key = ((Invoker)((List)invokers).get(0)).getUrl().getServiceKey() + "." + invocation.getMethodName();
        int length = ((List)invokers).size();
        int maxWeight = 0;
        int minWeight = 2147483647;

        int currentWeight;
        for(int i = 0; i < length; ++i) {
            currentWeight = this.getWeight((Invoker)((List)invokers).get(i), invocation);
            maxWeight = Math.max(maxWeight, currentWeight);
            minWeight = Math.min(minWeight, currentWeight);
        }

        AtomicPositiveInteger sequence;
        if (maxWeight > 0 && minWeight < maxWeight) {
            sequence = (AtomicPositiveInteger)this.weightSequences.get(key);
            if (sequence == null) {
                this.weightSequences.putIfAbsent(key, new AtomicPositiveInteger());
                sequence = (AtomicPositiveInteger)this.weightSequences.get(key);
            }

            currentWeight = sequence.getAndIncrement() % maxWeight;
            List<Invoker<T>> weightInvokers = new ArrayList();
            Iterator i$ = ((List)invokers).iterator();

            while(i$.hasNext()) {
                Invoker<T> invoker = (Invoker)i$.next();
                if (this.getWeight(invoker, invocation) > currentWeight) {
                    weightInvokers.add(invoker);
                }
            }

            int weightLength = weightInvokers.size();
            if (weightLength == 1) {
                return (Invoker)weightInvokers.get(0);
            }

            if (weightLength > 1) {
                invokers = weightInvokers;
                length = weightInvokers.size();
            }
        }

        sequence = (AtomicPositiveInteger)this.sequences.get(key);
        if (sequence == null) {
            this.sequences.putIfAbsent(key, new AtomicPositiveInteger());
            sequence = (AtomicPositiveInteger)this.sequences.get(key);
        }

        return (Invoker)((List)invokers).get(sequence.getAndIncrement() % length);
    }
}
