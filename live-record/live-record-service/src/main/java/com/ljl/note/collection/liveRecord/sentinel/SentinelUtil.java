package com.ljl.note.collection.liveRecord.sentinel;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.ljl.note.collection.liveRecord.enums.SentinelMethodTypeEnum;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SentinelUtil{

    public static Boolean getAccessKey(SentinelMethodTypeEnum sentinelMethodTypeEnum) {
        String method=sentinelMethodTypeEnum.getMethod();
        while (true){
            try(Entry entry = SphU.entry(method)){
                return true;
            }catch (BlockException e){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @PostConstruct
    public void initFlowRules(){
        List<FlowRule> rules = new ArrayList<>();
        for(SentinelMethodTypeEnum sentinelMethodTypeEnum:SentinelMethodTypeEnum.values()){
            FlowRule rule = new FlowRule();
            rule.setResource(sentinelMethodTypeEnum.getMethod());
            rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
            rule.setCount(sentinelMethodTypeEnum.getQps());
            rules.add(rule);
            FlowRuleManager.loadRules(rules);
        }
        log.info("初始化Sentinel配置成功！");
    }
}
