package com.ljl.note.collection.common.exception;

/**
 * 通用BaseErrorCode定义
 *
 */
public enum BaseErrorCode {
    /*****************
     ***Basic Error***
     ****************/
    SUCCESS(0, "success"),
    FAILED(2900000000001L, "System Error"),
    PARMA_EXCPETION(2900000000002L, "Parameter Exception"),
    PARMA_LONG_EXCPETION(2900000000003L, "Parameter size too long!"),

    NULL_MODEL(2900000000003L, "query a null model"),
    PERMISSION_DENIED(2900000000004L, "permission denied"),
    RISK_FAILED(2900000000005L, "内容涉及敏感信息"),
    RPC_ERROR(2900000000006L, "RPC异常"),
    UPDATE_ERROR(2900000000007L, "文件上传媒体中心出现异常"),
    DATE_FROMAT_ERROR(2900000000012L, "日期转换异常"),
    CREATE_FILE_ERROR(2900000000008L, "创建文件失败"),
    URL_ENCODE_ERROR(2900000000009L, "urlencode异常"),
    PARAM_OVER_LENGTH_ERROR(2900000000010L, "参数过长"),
    POINT_TYPE_ERROR(2900000000011L, "打点类型错误"),
    EMPTY_USER(2900000000012L, "用户不存在"),
    EMPTY_MERCHANT(2900000000013L, "商户不存在"),
    SYSTEM_UPDATE_ERROR(2900000000014L, "更新失败"),
    SYSTEM_ADD_ERROR(2900000000015L, "增加失败"),
    EMPTY_SOLUTION(2900000000016L, "解决方案不存在"),
    ERROR_APOLLOCONFIG(2900000000017L, "错误的apollo配置"),
    PIC_EMPTY(2900000000018L,"上传图片为空"),
    APPLY_ERROR(2900000000019L,"申请失败，请联系客服"),


    /*****************
     ***Live Error***
     ****************/
    LIVE_ROOM_STARTED(29001001000001L, "直播进行中"),
    LIVE_ROOM_ENDED(29001001000002L, "直播场次已结束"),
    LIVE_ROOM_NO_STARTED(29001001000003L, "直播未开始"),
    LIVE_ROOM_PAUSED(29001001000004L, "直播已暂停"),
    LIVE_ROOM_DELETED(29001001000005L, "直播场次不存在"),
    LIVE_ROOM_NO_PRIVILEG(29001001000006L,"您没有此项权限"),
    LIVE_ROOM_BAN(29001001000007L,"您已经被禁播,请联系客服"),
    LIVE_ROOM_MANAGE_DELETED(29001001000008L,"直播房间不存在"),

    /**
     * 登陆信息错误码
     **/
    LIVE_LOGIN_EMPTY(2900100200001L, "用户登陆信息为空"),
    LIVE_PHONE_ERROR(2900100200002L, "手机号不合法"),
    LIVE_PHONE_EMPTY(2900100200003L, "手机号为空"),
    LIVE_VERIFYCODE_ERROR(2900100200004L, "验证码错误"),
    LIVE_VERIFYCODE_EMPTY(2900100200005L, "验证码为空"),
    LIVE_VERIFY_EMPTY(2900100200006L, "验证信息为空"),
    LIVE_QQ_ERROR(2900100200007L, "QQ不合法"),
    /**
     * 直播信息错误码
     **/
    LIVE_CODE_INVALID(2900100300001L, "直播场次code无效"),
    LIVE_OPERATER_ERROR(2900100300002L, "直播场次操作错误"),
    LIVE_CREATE_ERROR(2900100300003L, "创建直播场次失败"),
    LIVE_CREATE_ERROR_ROBOTNUM(2900100300004L, "注水数不能比之前的小"),
    LIVE_RECORD_CREATE_ERROR(2900100300005L, "创建直播间场次失败"),
    LIVE_UPDATE_ERROR(2900100300006L, "更新直播场次失败"),

    LIVE_MANAGE_USER_CREATE_ERROR(2900100300009L, "创建直播间关注人数失败"),
    LIVE_MANAGE_USER_UPDATE_ERROR(2900100300010L, "更新直播间关注人数失败"),

    /********************
     ***RoomTool Error***
     ********************/
    TOOL_TYPE_ERROR(2900300100001L, "工具类型错误"),
    TOOL_MOVE_ERROR(2900300100002L, "顺序移动失败"),
    TOOL_STATE_ERROR(2900300100003L, "状态设置有误"),
    TOOL_COUNT_LIMIT(2900300100004L, "该房间添加商品达到上限"),
    GOODS_ADD_REPEAT(2900300100005L, "商品不能重复添加!"),


    /*****************
     ***Room Error****
     *****************/
    ROOM_STATE_ERROR(2900300200001L, "当前直播场次状态不允许操作"),
    QUERY_ROOM_TOOS_ERROR(2900300200002L, "查询直播场次同步状态信息结果为空"),
    LIVE_CODE_ERROR(2900300200003L, "liveCode not exist"),
    QUERY_ROOMLIST_ERROR(2900300200004L, "查询直播场次列表服务error"),
    ROOM_MANAGE_STATE_ERROR(2900300200005L, "存在进行中的直播场次，请先结束场次后删除"),


    /**
     * 流量信息错误码
     **/
    FLUX_CREATE_ERROR(2900300300001L, "增加流量记录失败"),
    FLUX_NULL(2900300300008L,"流量不足，请先充值"),
    FLUX_LESS_THAN_TEN(2900300300009L,"剩余流量不足10GB，为了保证直播活动的顺利展开，建议进行流量充值"),
    /**
     * 验证码验证失败
     */
    LIVE_VERIFY_FAIL(2900300300101L,"验证码验证失败"),
    LIVE_VERIFY_ERROR(2900300300102L,"验证码错误"),
    LIVE_VERIFY_INVALID(2900300300103L,"验证码失效"),

    /**
     *
     */
    Add_RoomAnchor_Faild_Existent(2900300300201L,"直播场次已存在主播"),
    Add_RoomAnchor_Faild_AnchorWrongful(2900300300202L,"主播不合法"),
    Add_RoomAnchor_Faild_System(2900300300203L,"直播场次添加主播出现系统错误"),
    CORRELATION_ERROR(2900300300204L,"直播场次风控未通过不能添加主播"),
    LIVE_UPDATE_ROOMSTAT_ERROR(2900300300205L,"直播间状态更新出现系统错误"),


    ANCHOR_UPDATE_FAILD_SYSTEM(2900300400201L,"跟新主播信息出现系统错误"),
    ANCHOR_DELETED_FAILD_SYSTEM(2900300400202L,"删除主播出现系统错误"),
    ANCHOR_SELECT_FAILD_NONEXISTENT(2900300400203L,"主播不存在"),
    ANCHOR_INSERT_FAILD_SYSTEM(2900300400204L,"添加新主播信息出现系统错误"),
    ANCHOR_DELETED_BOUND(2900300400205L,"主播已绑定直播场次,不能删除"),
    ANCHOR_ADD_FAILD_EXISTENT(2900300400206L,"该主播已经认证了，请勿重复认证"),

    ADD_FACEVERIFICATION_FAILE_SYSTEM(2900300400301L,"添加人脸识别记录出现系统错误"),
    /**
     *
     */
    FIV_API_FAILD(2900300400401L,"腾讯FIV接口异常"),

    /**
     *
     */
    UPFACECOUNT_FAILD_SYSTEM(2900300400501L,"更新人脸认证次数出现系统错误"),
    UPOCRCOUNT_FAILD_SYSTEM(2900300400502L,"更新ocr次数出现系统错误"),
    OCR_ID_CARD_EXPIRED(2900300400503L,"证件已过有效期"),
    OCR_FAILD(2900300400504L,"无效的身份证照片，请重新上传"),
    /***
     * 用户相关错误码
     */
    USER_UNEXIST(2900300500501L,"用户不存在!"),
    USER_ABNORMAL_STATE(2900300500502L,"用户状态异常"),
    /**
     * 聊天室屏蔽词错误
     */
    CHAT_ROOM_INTERCEPTWORD_EXISTED(2900300500601L,"屏蔽词已存在"),

    /**
     * 广告牌错误
     */
    GOODS_UNEXIST_BUSINESS(2900300500701L,"不存在的业务方"),

    /**
     * 礼盒错误
     */
    GIFTBOX_UPDATE_DELETEPRIZE_FALED(2900300500801L,"修改礼盒删除原有奖品失败"),
    GIFTBOX_UPDATE_INSERTPRIZE_FALED(2900300500802L,"修改礼盒添加新奖品失败"),
    GIFTBOX_UPDATE_FALED(2900300500803L,"修改礼盒失败"),
    GIFTBOX_DELETE_FALED(2900300500804L, "删除礼盒失败"),
    GIFT_BOX_DELETED(2900300500805L,"礼盒已删除"),
    GIFT_BOX_UNGRANTING(2900300500806L,"礼盒未投放"),
    PRIZE_RECEIVE_FALED(2900300500807L,"卡卷领取失败"),
    GIFT_BOX_ENDOFCOLLAR(2900300500808L,"礼盒已经领完！"),
    GIFT_BOX_UP(2900300500809L,"已到达领取上限！"),
    GIFT_BOX_UNGIT(2900300500810L,"很遗憾未中奖，再接再厉！"),
    GIFT_BOX_ROOMSTATE_UNLIVING(2900300500811L,"直播未开始不能发放礼盒"),
    GIFT_BOX_GRANTFAILD(2900300500812L,"礼盒发放失败"),

    /**
     * 用户导出
     */
    USER_EXPORT_NOUSER(2900300600801L,"导出失败，不存在用户"),

    /**
     * 会员
     */
    MEMBER_RANK_INSUFFICIENT(2900300600901L,"会员等级不满足"),
    MEMBER_NOTGET_MEMBERCARDTEMPLATE(2900300600902L,"未获取到默认会员卡模板"),

    /**
     * 录播
     */
    CREATE_LIVERECORDTASK_FAILED(2900300700801L,"创建录播任务失败！"),
    END_LIVERECORDTASK_FAILED(2900300700802L,"终止录播任务失败!"),
    SEARCHMEDIA_FAILED(2900300700803L,"搜索媒体文件失败!"),
    EDITMEDIA_FAILED(2900300700804L,"编辑视频文件失败!"),
    DESCRIBETASKDETAIL_FAILED(2900300700805L,"查询任务详情失败!"),
    DESCRIBEMEDIAINFOS_FAILED(2900300700806L,"查询文件详细信息失败!"),
    DELETEMEDIA_FAILED(2900300700807L,"删除文件失败!"),
    LIVERECORD_NOT_EXIST_OR_DELETED(2900300700808L,"录播视频不存在或已删除!"),
    LIVERECORD_GETPLAYSTATLOGLIST_ERROR(2900300700809L,"获取播放统计数据文件下载地址失败!"),

    /**
     * 海报
     */
    ITEM_IMAGE_NULL_ERROR(2900300800808L, "水印图片中没有url且没有mediaId项"),
    ITEM_TEXT_NULL_ERROR(2900300800809L, "文字水印的内容不能为空"),
    WATER_MARK_SERVICE_FAIL(2900300800810L, "创建水印图片失败"),
    WX_SCENE_SERVICE_FAIL(2900300800811L, "创建小程序码短参失败"),
    GET_APPID_ERROR(2900300800812L, "获取appid失败"),
    WX_APPID_NO_FIND(2900300800813L, "创建小程序分享图片时，appid或appContent缺失"),
    WX_CODE_URL_NO_FIND(2900300800814L, "创建小程序二维码图片失败"),
    QR_CREATE_SERVICE_FAIL(2900300800815L, "创建二维码失败"),


    /**
     * 好物榜
     */
    GOOD_LIST_NULL(2900300800811L, "好物榜没数据"),
    /**
     * todo:不是会员的code是所有营销活动约定好的不能改
     */
    MEMBER_NOT_MENMBER(1052110400999L,"不是会员，无法领取"),

    /**
     * 身份互通
     */
    USER_UN_IDENTITY(2900300800901L, "用户身份未打通"),

	/**
	 * 直播活动错误（砍价活动等）
	 */
	LIMITATION_SAVE_ERROR(31000000001L, "限购信息保存错误"),
	LIMITATION_UPDATE_ERROR(31000000002L, "限购信息更新错误"),
	LIMITATION_DELETE_ERROR(31000000003L, "限购信息删除错误"),
	ACTIVITY_LIMITATION_QUERY_ERROR(31000000004L, "活动限购信息查询错误"),
	GOODS_LIMITATION_QUERY_ERROR(31000000005L, "商品限购信息查询错误"),
	QUERY_GOODS_DETAIL_ERROR(31000000006L, "商品详情查询错误"),
	LIMITATION_NOT_EXIST(31000000007L, "限购信息不存在"),
	BARGAIN_SKU_DETAIL_NOT_EXIST(31000000008L, "商品sku信息不存在"),
	EXIST_SHOWING_BARGAIN_ACTIVITY(31000000009L, "已存在活动在展示中"),


    /**
     * 直播渠道错误
     */
    LIVE_CHANEL_ERROE(32000000001L, "直播渠道错误"),
    ;

    private long code;
    private String msg;

    BaseErrorCode(long code, String msg) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public long getCode() {
        return code;
    }

    public enum FIV_API_FAILD {}

}
