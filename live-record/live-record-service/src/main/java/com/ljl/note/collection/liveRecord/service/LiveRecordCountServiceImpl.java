package com.ljl.note.collection.liveRecord.service;

import com.ljl.note.collection.common.utils.DateUtils;
import com.ljl.note.collection.common.utils.FluxOrFileSizeConvertUtil;
import com.ljl.note.collection.common.utils.StringUtils;
import com.ljl.note.collection.liveRecord.common.RedisKey;
import com.ljl.note.collection.liveRecord.domain.enums.VodCountCellNameEnum;
import com.ljl.note.collection.liveRecord.mapper.LiveRecordFluxCountMapper;
import com.ljl.note.collection.liveRecord.mapper.LiveRecordMapper;
import com.ljl.note.collection.liveRecord.model.LiveRecord;
import com.ljl.note.collection.liveRecord.model.LiveRecordFluxCount;
import jodd.typeconverter.Convert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LiveRecordCountServiceImpl {

    @Autowired
    private LiveRecordMapper liveRecordMapper;

    @Autowired
    private LiveRecordFluxCountMapper liveRecordFluxCountMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    @Qualifier("fluxCountTotalHandleExecutor")
    private ThreadPoolTaskExecutor fluxCountTotalHandleExecutor;

    @Transactional(rollbackFor = Exception.class)
    public void dealVodCountFile(File file){
        List<LiveRecordFluxCount> liveRecordFluxCounts=new ArrayList<>();
        try {
            log.info("开始解析vod统计文件！");
            BufferedReader reader = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            String[] firstLine=reader.readLine().split(",");//第一行信息，为标题信息
            HashMap<String,String> cellNameMap=new HashMap<>();
            for(int i=0;i<firstLine.length;i++){
                cellNameMap.put(firstLine[i],String.valueOf(i));
            }
            String line = null;
            while((line=reader.readLine())!=null){
                String item[] = line.split(",");//CSV格式文件为逗号分隔符文件，这里根据逗号切分
                LiveRecordFluxCount liveRecordFluxCount=new LiveRecordFluxCount();
                liveRecordFluxCount.setFileId(item[Integer.parseInt(cellNameMap.get(VodCountCellNameEnum.FileId.getCell()))]);
                liveRecordFluxCount.setCountTime(DateUtils.strToDate(item[Integer.parseInt(cellNameMap.get(VodCountCellNameEnum.Date.getCell()))]+" 23:59:59","yyyy-MM-dd HH:mm:ss"));
                liveRecordFluxCount.setUseFlux(Convert.toLong(item[Integer.parseInt(cellNameMap.get(VodCountCellNameEnum.Flux.getCell()))]));
                liveRecordFluxCounts.add(liveRecordFluxCount);
            }
            log.info("vod统计文件解析结束！");
        } catch (Exception e) {
            log.error("解析文件出现错误！");
        }
        if(liveRecordFluxCounts.size()==0){
            return;
        }
        //统计数据入库
        List<String> fileList=liveRecordFluxCounts.stream().map(LiveRecordFluxCount::getFileId).collect(Collectors.toList());
        Example example=new Example(LiveRecord.class);
        example.createCriteria().andIn("qcloudFileId",fileList);
        List<LiveRecord> liveRecordList=liveRecordMapper.selectByExample(example);
        Map<String,LiveRecord> liveRecordMap=liveRecordList.stream().filter(liveRecord -> !StringUtils.isEmpty(liveRecord.getQcloudFileId()))
                                    .collect(Collectors.toMap(LiveRecord::getQcloudFileId,v->v,(oldV,newV)->oldV));
        Iterator<LiveRecordFluxCount> iterator=liveRecordFluxCounts.iterator();
        while (iterator.hasNext()){
            LiveRecordFluxCount liveRecordFluxCount=iterator.next();
            if(liveRecordMap.containsKey(liveRecordFluxCount.getFileId())){
                LiveRecord liveRecord=liveRecordMap.get(liveRecordFluxCount.getFileId());
                liveRecordFluxCount.setDeleted(0);
                liveRecordFluxCount.setPid(liveRecord.getPid());
                liveRecordFluxCount.setLiveRecordId(liveRecord.getId());
                liveRecordFluxCount.setLiveRecordName(liveRecord.getName());
                liveRecordFluxCount.setUseFluxStr(FluxOrFileSizeConvertUtil.getFormatSize(Convert.toDouble(liveRecordFluxCount.getUseFlux()),FluxOrFileSizeConvertUtil.FluxOrFileSizeUnitEnum.B));
                Long zeroLeftFlux=0L;
                String leftFluxKey=String.format(RedisKey.LIVERECORD_FLUX_ZEROCOUNT_KEY,liveRecord.getPid());
                if(redisTemplate.hasKey(leftFluxKey)){
                    zeroLeftFlux= Convert.toLong(redisTemplate.opsForValue().get(leftFluxKey));
                    redisTemplate.delete(String.format(RedisKey.LIVERECORD_FLUX_ZEROCOUNT_KEY,liveRecord.getPid()));
                }else{
                    /*FluxGetDTO fluxGetDTO=new FluxGetDTO();
                    fluxGetDTO.setPid(liveRecord.getPid());
                    zeroLeftFlux=fluxFacede.getLongFlux(fluxGetDTO).getLeftFlux();*/
                }
                liveRecordFluxCount.setLeftFlux(zeroLeftFlux*1024);//kb->b
                liveRecordFluxCount.setLeftFluxStr(FluxOrFileSizeConvertUtil.getFormatSize(Convert.toDouble(zeroLeftFlux*1024),FluxOrFileSizeConvertUtil.FluxOrFileSizeUnitEnum.B));
                //消耗流量
                /*ShopFluxRequest shopFluxRequest=new ShopFluxRequest();
                shopFluxRequest.setPid(liveRecord.getPid());
                shopFluxRequest.setUsedFlux(liveRecordFluxCount.getUseFlux()/1024);//b->kb
                shopFluxService.useFlux(shopFluxRequest);*/
            }else{
                iterator.remove();
            }
        }
        if(liveRecordFluxCounts.size()==0){
            return;
        }
        liveRecordFluxCountMapper.insertList(liveRecordFluxCounts);

        //新增流量统计总览
        Map<Long,List<LiveRecordFluxCount>> fluxCountTotalMap=liveRecordFluxCounts.stream()
                .filter(liveRecordFluxCount -> null!=liveRecordFluxCount.getPid())
                .collect(Collectors.groupingBy(LiveRecordFluxCount::getPid));
        for(Long pid:fluxCountTotalMap.keySet()){
            fluxCountTotalHandleExecutor.execute(()->{
                try {
                    addFluxCountTotalByPid(pid,fluxCountTotalMap.get(pid));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
        }

    }

    /**
     * 插入流量统计总览（回放消耗）
     * @param pid
     * @param liveRecordFluxCounts
     * @throws ParseException
     */
    private void addFluxCountTotalByPid(Long pid,List<LiveRecordFluxCount> liveRecordFluxCounts) throws ParseException {
        Long changeFlux=0L;
        for(LiveRecordFluxCount liveRecordFluxCount:liveRecordFluxCounts){
            changeFlux+=liveRecordFluxCount.getUseFlux();
        }
        //回放消耗流量总数
        String useTotalKey=String.format(RedisKey.LIVERECORD_FLUX_USETOTAL_KEY,pid);
        if(redisTemplate.hasKey(useTotalKey)){
            Long seconds=redisTemplate.getExpire(useTotalKey, TimeUnit.SECONDS);
            redisTemplate.opsForValue().increment(useTotalKey,changeFlux);
            redisTemplate.expire(useTotalKey,seconds, TimeUnit.SECONDS);
        }
        String dateStr= DateUtils.dateToStr(new Date(),"yyyy-MM-dd")+" 12:00:00";
        /*FluxTotalCountInsertDTO totalCountInsertDTO=new FluxTotalCountInsertDTO();
        totalCountInsertDTO.setPid(pid);
        totalCountInsertDTO.setCountTime(DateUtils.strToDate(dateStr,"yyyy-MM-dd HH:mm:ss"));
        totalCountInsertDTO.setCountType(FluxCountTypeEnum.LiveRecord);
        totalCountInsertDTO.setChangeFlux(changeFlux);
        totalCountInsertDTO.setChangeFluxStr("-"+ FluxOrFileSizeConvertUtil.getFormatSize(Convert.toDouble(changeFlux),FluxOrFileSizeConvertUtil.FluxOrFileSizeUnitEnum.B));
        fluxFacede.addFluxCountTotal(totalCountInsertDTO);*/
    }
}
