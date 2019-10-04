package com.ljl.note.collection.live.domain.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class LiveRoomInfoBO implements Serializable{
    private static final long serialVersionUID=1l;

        private Long roomId;

        private Long liveRoomId;

        private String liveCode;

        private Long storeId;

        private Long pid;

        private String shareContext;

        private String shareImgUrl;

        private String title;

        private String titleImgUrl;

        private String coverImgUrl;

        private String urlBeforeStart;

        private String urlAfterFinish;

        private Integer preSwitch;

        private String contextBeforeStart;

        private Integer endSwitch;

        private String contextAfterFinish;

        private Date planTime;

        private Date startTime;

        private Date finishTime;

        private Date addTime;

        private Date updateTime;

        private Byte stat;

        private Long sumFlux;

        private String minaQrcodeUrl;

        private Date sumTime;

        private Integer pauseReason;

        private Integer solutionType;

        private Integer robotNum;

        private Integer roomStyle;

        private String styleTitle;

        private String pushQrcodeUrl;

        private String chatAvatar;

        private Integer liveType;

        private String anchorName;

        private String styleContent;

        private Boolean wxMsgFlag;

        private Boolean liveRemindFlag;

        private Integer liveRemindTime;

        private Integer attentionCount;

        private String remark;

        private Date personCheckTime;

        private String idcard;

        private String memberRestrict;

        private Integer appletType;

        private Boolean recordOpened;

        private Boolean testMark;
    }
