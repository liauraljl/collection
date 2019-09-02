package com.ljl.note.collection.liveRecord.qcloud.vod2017;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ljl.note.collection.common.exception.BaseErrorCode;
import com.ljl.note.collection.common.exception.BizException;
import com.ljl.note.collection.common.utils.DateUtils;
import com.ljl.note.collection.common.utils.HttpRequestUtil;
import com.ljl.note.collection.common.utils.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Random;
import java.util.TreeMap;

/**
 * 2017版api (云点播)
 */
@Slf4j
@Service
public class QCloudVodCountService {

    private static final String URL="https://vod.api.qcloud.com/v2/index.php?";
    private static final String CHARSET = "UTF-8";
    private static final String SIGNATUREMETHOD="HmacSHA1";
    private static final String SRCSTR="GETvod.api.qcloud.com/v2/index.php?";

    @Value("${qcloud.secretId}")
    private String secretId;

    @Value("${qcloud.secretKey}")
    private String secretKey;

    /**
     * 获取播放统计数据文件下载地址
     * @return
     * @throws Exception
     */
    public String getPlayStatLogList(){
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("Action","GetPlayStatLogList");
        Date date = LocalDateTimeUtil.toDate(LocalDateTimeUtil.toLocalDateTime(new Date()).minusDays(1));
        String dateStr= DateUtils.dateToString(date,"yyyy-MM-dd");
        params.put("from",dateStr);// 昨日
        //params.put("from","2019-08-25");// 昨日
        params.put("to",dateStr);// 昨日
        params.put("SecretId",secretId);
        params.put("Region","sh");
        params.put("Timestamp",String.valueOf(System.currentTimeMillis()/1000));
        params.put("Nonce",String.valueOf(new Random().nextInt(Integer.MAX_VALUE)));
        params.put("SignatureMethod",SIGNATUREMETHOD);
        try {
            String fileUrl="";
            params.put("Signature", sign(getStringToSign(params), secretKey, SIGNATUREMETHOD));
            String url = getUrl(params);
            String response= HttpRequestUtil.get(url);
            log.info("getPlayStatLogList request:{},response:{}", JSON.toJSONString(params),response);
            JSONObject jsonObject=JSON.parseObject(response);
            if(jsonObject.containsKey("fileList")){
                JSONArray jsonArray=JSON.parseArray(jsonObject.get("fileList").toString());
                if(jsonArray!=null&&jsonArray.size()>0){
                    Object fileObj=jsonArray.get(0);
                    fileUrl=JSON.parseObject(fileObj.toString()).getString("url");
                }
            }
            return fileUrl;
        } catch (Exception e) {
            log.error("getPlayStatLogList error!",e.getMessage());
            throw new BizException(BaseErrorCode.LIVERECORD_GETPLAYSTATLOGLIST_ERROR);
        }
    }



    public static String sign(String s, String key, String method) throws Exception {
        Mac mac = Mac.getInstance(method);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(CHARSET), mac.getAlgorithm());
        mac.init(secretKeySpec);
        byte[] hash = mac.doFinal(s.getBytes(CHARSET));
        return DatatypeConverter.printBase64Binary(hash);
    }

    public static String getStringToSign(TreeMap<String, String> params) {
        StringBuilder s2s = new StringBuilder(SRCSTR);
        // 签名时要求对参数进行字典排序，此处用TreeMap保证顺序
        for (String k : params.keySet()) {
            s2s.append(k).append("=").append(params.get(k)).append("&");
        }
        return s2s.toString().substring(0, s2s.length() - 1);
    }

    public static String getUrl(TreeMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder url = new StringBuilder(URL);
        // 实际请求的url中对参数顺序没有要求
        for (String k : params.keySet()) {
            // 需要对请求串进行urlencode，由于key都是英文字母，故此处仅对其value进行urlencode
            url.append(k).append("=").append(URLEncoder.encode(params.get(k).toString(), CHARSET)).append("&");
        }
        return url.toString().substring(0, url.length() - 1);
    }
}
