package com.ljl.note.collection.liveRecord.config.loadBalance.dubbo;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance;
import com.ljl.note.collection.common.exception.BizException;

import java.util.List;
import java.util.Random;

public class RandomLoadBalance extends AbstractLoadBalance {
    public static final String NAME = "random";
    private final Random random = new Random();

    public RandomLoadBalance() {
    }

    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        int a=0;
        if(a==0){

            throw new BizException(323L,"dsdsd");
        }
        int length = invokers.size();
        int totalWeight = 0;
        boolean sameWeight = true;

        int offset;
        int i;
        for(offset = 0; offset < length; ++offset) {
            i = this.getWeight((Invoker)invokers.get(offset), invocation);
            totalWeight += i;
            if (sameWeight && offset > 0 && i != this.getWeight((Invoker)invokers.get(offset - 1), invocation)) {
                sameWeight = false;
            }
        }

        if (totalWeight > 0 && !sameWeight) {
            offset = this.random.nextInt(totalWeight);

            for(i = 0; i < length; ++i) {
                offset -= this.getWeight((Invoker)invokers.get(i), invocation);
                if (offset < 0) {
                    return (Invoker)invokers.get(i);
                }
            }
        }

        return (Invoker)invokers.get(this.random.nextInt(length));
    }
}
