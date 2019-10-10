package com.ljl.note.collection.liveRecord.qcloud.sdk2018;

import com.alibaba.fastjson.JSON;
import com.ljl.note.collection.common.exception.BaseException;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.live.v20180801.LiveClient;
import com.tencentcloudapi.live.v20180801.models.CreateLiveRecordRequest;
import com.tencentcloudapi.live.v20180801.models.CreateLiveRecordResponse;
import com.tencentcloudapi.live.v20180801.models.StopLiveRecordRequest;
import com.tencentcloudapi.live.v20180801.models.StopLiveRecordResponse;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.*;
import com.ljl.note.collection.common.enums.BaseErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *2018版SDK(云直播、云点播)
 */
@Slf4j
@Service
public class QCloudLiveRecordService {
    @Value("${qcloud.secretId:sss}")
    private String secretId;

    @Value("${qcloud.secretKey:sss}")
    private String secretKey;

    private static final String LIVEENDPOINT="live.tencentcloudapi.com";

    private static final String VODENDPOINT="vod.tencentcloudapi.com";


    /**
     * 创建录播任务
     * @param request
     * @return
     */
    public CreateLiveRecordResponse createLiveRecordTask(CreateLiveRecordRequest request){
        try{
            Credential cred = new Credential(secretId, secretKey);
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint(LIVEENDPOINT);
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            LiveClient client = new LiveClient(cred, "ap-shanghai", clientProfile);
            CreateLiveRecordResponse resp = client.CreateLiveRecord(request);
            log.info("CreateLiveRecord success!request:{},response:{}",JSON.toJSONString(request),JSON.toJSONString(resp));
            return resp;
        } catch (TencentCloudSDKException e) {
            log.error("CreateLiveRecord error!request:{},error:{}", JSON.toJSONString(request),e.getMessage());
            throw new BaseException(BaseErrorCode.CREATE_LIVERECORDTASK_FAILED);
        }
    }

    /**
     * 终止录播任务
     */
    public Boolean endLiveRecordTask(StopLiveRecordRequest request){
        try{
            Credential cred = new Credential(secretId, secretKey);
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint(LIVEENDPOINT);
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            LiveClient client = new LiveClient(cred, "ap-shanghai", clientProfile);
            StopLiveRecordResponse resp = client.StopLiveRecord(request);
            log.info("StopLiveRecord success!request:{},response:{}",JSON.toJSONString(request),JSON.toJSONString(resp));
            return true;
        } catch (TencentCloudSDKException e) {
            log.error("StopLiveRecord error!:request:{},error:{}", JSON.toJSONString(request),e.getMessage());
            throw new BaseException(BaseErrorCode.END_LIVERECORDTASK_FAILED);
        }
    }

    /**
     * 搜索媒体文件
     * @param request
     * @return
     */
    public SearchMediaResponse searchMediaIO(SearchMediaRequest request){
        try{
            Credential cred = new Credential(secretId, secretKey);
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint(VODENDPOINT);
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            VodClient client = new VodClient(cred, "ap-shanghai", clientProfile);
            SearchMediaResponse resp = client.SearchMedia(request);
            log.info("SearchMedia success!request:{},response:{}",JSON.toJSONString(request),JSON.toJSONString(resp));
            return resp;
        } catch (TencentCloudSDKException e) {
            log.error("SearchMedia error!:request:{},error:{}", JSON.toJSONString(request),e.getMessage());
            throw new BaseException(BaseErrorCode.SEARCHMEDIA_FAILED);
        }
    }

    /**
     * 编辑视频文件
     */
    public EditMediaResponse editMedia(EditMediaRequest request){
        try{
            Credential cred = new Credential(secretId, secretKey);
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint(VODENDPOINT);
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            VodClient client = new VodClient(cred, "ap-shanghai", clientProfile);
            EditMediaResponse resp = client.EditMedia(request);
            log.info("EditMedia success!request:{},response:{}",JSON.toJSONString(request),JSON.toJSONString(resp));
            return resp;
        } catch (TencentCloudSDKException e) {
            log.error("EditMedia error!:request:{},error:{}", JSON.toJSONString(request),e.getMessage());
            throw new BaseException(BaseErrorCode.EDITMEDIA_FAILED);
        }
    }

    /**
     * 查询任务详情
     * @param request
     * @return
     */
    public DescribeTaskDetailResponse describeTaskDetail(DescribeTaskDetailRequest request){
        try{
            Credential cred = new Credential(secretId, secretKey);
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint(VODENDPOINT);
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            VodClient client = new VodClient(cred, "ap-shanghai", clientProfile);
            DescribeTaskDetailResponse resp = client.DescribeTaskDetail(request);
            log.info("DescribeTaskDetail success!request:{},response:{}",JSON.toJSONString(request),JSON.toJSONString(resp));
            return resp;
        } catch (TencentCloudSDKException e) {
            log.error("DescribeTaskDetail error!:request:{},error:{}", JSON.toJSONString(request),e.getMessage());
            throw new BaseException(BaseErrorCode.DESCRIBETASKDETAIL_FAILED);
        }

    }

    /**
     * 查询文件详细信息
     */
    public DescribeMediaInfosResponse describeMediaInfos(DescribeMediaInfosRequest request){
        try{
            Credential cred = new Credential(secretId, secretKey);
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint(VODENDPOINT);
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            VodClient client = new VodClient(cred, "ap-shanghai", clientProfile);
            DescribeMediaInfosResponse resp = client.DescribeMediaInfos(request);
            log.info("DescribeMediaInfos success!request:{},response:{}",JSON.toJSONString(request),JSON.toJSONString(resp));
            return resp;
        } catch (TencentCloudSDKException e) {
            log.error("DescribeMediaInfos error!:request:{},error:{}", JSON.toJSONString(request),e.getMessage());
            throw new BaseException(BaseErrorCode.DESCRIBEMEDIAINFOS_FAILED);
        }
    }

    /**
     * 删除视频文件
     */
    public DeleteMediaResponse deleteMedia(DeleteMediaRequest request){
        try{
            Credential cred = new Credential(secretId, secretKey);
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint(VODENDPOINT);
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            VodClient client = new VodClient(cred, "ap-shanghai", clientProfile);
            DeleteMediaResponse resp = client.DeleteMedia(request);
            log.info("DeleteMedia success!request:{},response:{}",JSON.toJSONString(request),JSON.toJSONString(resp));
            return resp;
        } catch (TencentCloudSDKException e) {
            log.error("DeleteMedia error!:request:{},error:{}", JSON.toJSONString(request),e.getMessage());
            throw new BaseException(BaseErrorCode.DELETEMEDIA_FAILED);
        }
    }
}
