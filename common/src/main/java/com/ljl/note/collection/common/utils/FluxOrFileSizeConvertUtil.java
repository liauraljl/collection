package com.ljl.note.collection.common.utils;

import com.ljl.note.collection.common.exception.BaseErrorCode;
import com.ljl.note.collection.common.exception.BizException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * 流量、文件单位转换工具类
 */
public class FluxOrFileSizeConvertUtil {
    /**
     * 流量、文件单位转换
     * @param size 当前流量、文件大小
     * @param startUnit 当前单位
     * @return
     * @throws BizException
     */
    public static String getFormatSize(Double size, FluxOrFileSizeUnitEnum startUnit) throws BizException {
        if(startUnit.getType()< FluxOrFileSizeUnitEnum.B.getType()||startUnit.getType()> FluxOrFileSizeUnitEnum.PB.getType()){
            throw new BizException(BaseErrorCode.FAILED);
        }
        if(size<0){
            return "-"+getFormatSize(-1*size,startUnit);
        }else if(size<1024||startUnit.getType()== FluxOrFileSizeUnitEnum.PB.getType()){
            return new BigDecimal(Double.toString(size)).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + FluxOrFileSizeUnitEnum.getUnit(startUnit.getType());
        }else{
            size/=1024;
            return getFormatSize(size, FluxOrFileSizeUnitEnum.of(startUnit.getType()+1));
        }
    }

    @AllArgsConstructor
    public enum  FluxOrFileSizeUnitEnum {
        B(0,"B"),
        KB(1,"KB"),
        MB(2,"MB"),
        GB(3,"GB"),
        TB(4,"TB"),
        PB(5,"PB");

        @Getter
        private int type;

        @Getter
        private String unit;

        public static String getUnit (int type) {
            for (FluxOrFileSizeUnitEnum e : FluxOrFileSizeUnitEnum.values()) {
                if (e.getType()==type) {
                    return e.getUnit();
                }
            }
            return null;
        }


        public static FluxOrFileSizeUnitEnum of(int type) {
            for (FluxOrFileSizeUnitEnum e : values()) {
                if (e.type==type) {
                    return e;
                }
            }
            return null;
        }
    }
}
