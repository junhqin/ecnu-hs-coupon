package com.nageoffer.onecoupon.merchant.admin.service.basics.log;

import cn.hutool.core.util.StrUtil;
import com.mzt.logapi.beans.LogRecord;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.service.ILogRecordService;
import com.nageoffer.onecoupon.merchant.admin.common.context.UserContext;
import com.nageoffer.onecoupon.merchant.admin.dao.entity.CouponTemplateLogDO;
import com.nageoffer.onecoupon.merchant.admin.dao.mapper.CouponTemplateLogMapper;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 保存操作日志新增到数据库
 * <p>
 */
@Service
@RequiredArgsConstructor
public class DBLogRecordServiceImpl implements ILogRecordService {

    private final CouponTemplateLogMapper couponTemplateLogMapper;
    private static final Logger log = LoggerFactory.getLogger(DBLogRecordServiceImpl.class);
    @Override
    public void record(LogRecord logRecord) {
        try {
            switch (logRecord.getType()) {
                case "CouponTemplate": {
                    log.info("【logRecord】log={}", logRecord);
                    CouponTemplateLogDO couponTemplateLogDO = CouponTemplateLogDO.builder()
                            .couponTemplateId(logRecord.getBizNo())
                            .shopNumber(UserContext.getShopNumber())
                            .operatorId(UserContext.getUserId())
                            .operationLog(logRecord.getAction())
                            .originalData(Optional.ofNullable(LogRecordContext.getVariable("originalData")).map(Object::toString).orElse(null))
                            .modifiedData(StrUtil.isBlank(logRecord.getExtra()) ? null : logRecord.getExtra())
                            .build();
                    couponTemplateLogMapper.insert(couponTemplateLogDO);
                }
            }
        } catch (Exception ex) {
            log.error("记录[{}]操作日志失败", logRecord.getType(), ex);
        }
    }

    @Override
    public List<LogRecord> queryLog(String bizNo, String type) {
        return List.of();
    }

    @Override
    public List<LogRecord> queryLogByBizNo(String bizNo, String type, String subType) {
        List<CouponTemplateLogDO> couponTemplateLogDOList = couponTemplateLogMapper.selectByCouponTemplateId(bizNo);
        // 将 CouponTemplateLogDO 列表转换为 LogRecord 列表
        return couponTemplateLogDOList.stream()
                .map(couponTemplateLogDO -> BeanUtil.toBean(couponTemplateLogDO, LogRecord.class))
                .collect(Collectors.toList());
    }
}
