package com.nageoffer.onecoupon.merchant.admin.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nageoffer.onecoupon.merchant.admin.dao.entity.CouponTemplateLogDO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 优惠券模板操作日志数据库持久层
 */
public interface CouponTemplateLogMapper extends BaseMapper<CouponTemplateLogDO> {
    @Select("SELECT * FROM t_coupon_template_log WHERE coupon_template_id = #{couponTemplateId}")
    List<CouponTemplateLogDO> selectByCouponTemplateId(String couponTemplateId);
}
