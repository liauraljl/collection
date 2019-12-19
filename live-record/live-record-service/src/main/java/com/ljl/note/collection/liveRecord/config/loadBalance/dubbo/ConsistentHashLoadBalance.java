package com.ljl.note.collection.liveRecord.config.loadBalance.dubbo;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConsistentHashLoadBalance extends AbstractLoadBalance {
    private final ConcurrentMap<String, ConsistentHashSelector<?>> selectors = new ConcurrentHashMap();

    public ConsistentHashLoadBalance() {
    }

    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        String key = ((Invoker)invokers.get(0)).getUrl().getServiceKey() + "." + invocation.getMethodName();
        int identityHashCode = System.identityHashCode(invokers);
        ConsistentHashLoadBalance.ConsistentHashSelector<T> selector = (ConsistentHashLoadBalance.ConsistentHashSelector)this.selectors.get(key);
        if (selector == null || selector.getIdentityHashCode() != identityHashCode) {
            this.selectors.put(key, new ConsistentHashLoadBalance.ConsistentHashSelector(invokers, invocation.getMethodName(), identityHashCode));
            selector = (ConsistentHashLoadBalance.ConsistentHashSelector)this.selectors.get(key);
        }

        return selector.select(invocation);
    }

    private static final class ConsistentHashSelector<T> {
        private final TreeMap<Long, Invoker<T>> virtualInvokers = new TreeMap();
        private final int replicaNumber;
        private final int identityHashCode;
        private final int[] argumentIndex;

        public ConsistentHashSelector(List<Invoker<T>> invokers, String methodName, int identityHashCode) {
            this.identityHashCode = System.identityHashCode(invokers);
            URL url = ((Invoker)invokers.get(0)).getUrl();
            this.replicaNumber = url.getMethodParameter(methodName, "hash.nodes", 160);
            String[] index = Constants.COMMA_SPLIT_PATTERN.split(url.getMethodParameter(methodName, "hash.arguments", "0"));
            this.argumentIndex = new int[index.length];

            for(int i = 0; i < index.length; ++i) {
                this.argumentIndex[i] = Integer.parseInt(index[i]);
            }

            Iterator i$ = invokers.iterator();

            while(i$.hasNext()) {
                Invoker<T> invoker = (Invoker)i$.next();

                for(int i = 0; i < this.replicaNumber / 4; ++i) {
                    byte[] digest = this.md5(invoker.getUrl().toFullString() + i);

                    for(int h = 0; h < 4; ++h) {
                        long m = this.hash(digest, h);
                        this.virtualInvokers.put(m, invoker);
                    }
                }
            }

        }

        public int getIdentityHashCode() {
            return this.identityHashCode;
        }

        public Invoker<T> select(Invocation invocation) {
            String key = this.toKey(invocation.getArguments());
            byte[] digest = this.md5(key);
            Invoker<T> invoker = this.sekectForKey(this.hash(digest, 0));
            return invoker;
        }

        private String toKey(Object[] args) {
            StringBuilder buf = new StringBuilder();
            int[] arr$ = this.argumentIndex;
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                int i = arr$[i$];
                if (i >= 0 && i < args.length) {
                    buf.append(args[i]);
                }
            }

            return buf.toString();
        }

        private Invoker<T> sekectForKey(long hash) {
            Long key = hash;
            if (!this.virtualInvokers.containsKey(key)) {
                SortedMap<Long, Invoker<T>> tailMap = this.virtualInvokers.tailMap(key);
                if (tailMap.isEmpty()) {
                    key = (Long)this.virtualInvokers.firstKey();
                } else {
                    key = (Long)tailMap.firstKey();
                }
            }

            Invoker<T> invoker = (Invoker)this.virtualInvokers.get(key);
            return invoker;
        }

        private long hash(byte[] digest, int number) {
            return ((long)(digest[3 + number * 4] & 255) << 24 | (long)(digest[2 + number * 4] & 255) << 16 | (long)(digest[1 + number * 4] & 255) << 8 | (long)(digest[0 + number * 4] & 255)) & 4294967295L;
        }

        private byte[] md5(String value) {
            MessageDigest md5;
            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException var6) {
                throw new IllegalStateException(var6.getMessage(), var6);
            }

            md5.reset();
            Object var3 = null;

            byte[] bytes;
            try {
                bytes = value.getBytes("UTF-8");
            } catch (UnsupportedEncodingException var5) {
                throw new IllegalStateException(var5.getMessage(), var5);
            }

            md5.update(bytes);
            return md5.digest();
        }
    }
}
