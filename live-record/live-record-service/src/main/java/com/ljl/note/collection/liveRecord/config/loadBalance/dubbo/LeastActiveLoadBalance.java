package com.ljl.note.collection.liveRecord.config.loadBalance.dubbo;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcStatus;
import com.alibaba.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance;

import java.util.List;
import java.util.Random;

public class LeastActiveLoadBalance extends AbstractLoadBalance {
    public static final String NAME = "leastactive";
    private final Random random = new Random();

    public LeastActiveLoadBalance() {
    }

    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        int length = invokers.size();
        int leastActive = -1;
        int leastCount = 0;
        int[] leastIndexs = new int[length];
        int totalWeight = 0;
        int firstWeight = 0;
        boolean sameWeight = true;

        int offsetWeight;
        int leastIndex;
        for(offsetWeight = 0; offsetWeight < length; ++offsetWeight) {
            Invoker<T> invoker = (Invoker)invokers.get(offsetWeight);
            leastIndex = RpcStatus.getStatus(invoker.getUrl(), invocation.getMethodName()).getActive();
            int weight = invoker.getUrl().getMethodParameter(invocation.getMethodName(), "weight", 100);
            if (leastActive != -1 && leastIndex >= leastActive) {
                if (leastIndex == leastActive) {
                    leastIndexs[leastCount++] = offsetWeight;
                    totalWeight += weight;
                    if (sameWeight && offsetWeight > 0 && weight != firstWeight) {
                        sameWeight = false;
                    }
                }
            } else {
                leastActive = leastIndex;
                leastCount = 1;
                leastIndexs[0] = offsetWeight;
                totalWeight = weight;
                firstWeight = weight;
                sameWeight = true;
            }
        }

        if (leastCount == 1) {
            return (Invoker)invokers.get(leastIndexs[0]);
        } else {
            if (!sameWeight && totalWeight > 0) {
                offsetWeight = this.random.nextInt(totalWeight);

                for(int i = 0; i < leastCount; ++i) {
                    leastIndex = leastIndexs[i];
                    offsetWeight -= this.getWeight((Invoker)invokers.get(leastIndex), invocation);
                    if (offsetWeight <= 0) {
                        return (Invoker)invokers.get(leastIndex);
                    }
                }
            }

            return (Invoker)invokers.get(leastIndexs[this.random.nextInt(leastCount)]);
        }
    }
}
