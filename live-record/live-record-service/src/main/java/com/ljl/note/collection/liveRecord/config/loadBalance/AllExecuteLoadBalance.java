package com.ljl.note.collection.liveRecord.config.loadBalance;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
public class AllExecuteLoadBalance extends AbstractLoadBalance {
    private final Random random = new Random();
    @Override
    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {

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
                    executeAllInvoker(invokers,i, invocation);
                    return (Invoker)invokers.get(i);
                }
            }
        }
        int randomIndex = this.random.nextInt(length);
        executeAllInvoker(invokers,randomIndex, invocation);
        return (Invoker)invokers.get(randomIndex);
    }

    protected <T> void executeAllInvoker(List<Invoker<T>> invokers, int offset, Invocation invocation){
        ArrayList<String> methods = Lists.newArrayList("queryroombyroomid");
        if(invokers.size()>1 && methods.contains(invocation.getMethodName().toLowerCase())){
            for(int i=0;i<invokers.size();i++){
                if(i != offset){
                    invokers.get(i).invoke(invocation);
                }
            }
        }
    }
}
