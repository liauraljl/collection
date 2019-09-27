package com.ljl.note.collection.support.framework.job;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Data
public class AuthTask implements Delayed {

    private Long start = System.currentTimeMillis();
    private Long delayTime;
    private ChannelHandlerContext context;
    public AuthTask(){
        //默认延时10秒
        this.delayTime = 10*1000L;
    }
    public AuthTask(Long delayTime){
        this.delayTime = delayTime*1000;
    }

    public AuthTask(Long delayTime,ChannelHandlerContext context){
        this.context = context;
        this.delayTime = delayTime*1000;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(start+delayTime-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        AuthTask o1 = (AuthTask) o;
        return (int) (this.getDelay(TimeUnit.MICROSECONDS)-o.getDelay(TimeUnit.MILLISECONDS));
    }
}
