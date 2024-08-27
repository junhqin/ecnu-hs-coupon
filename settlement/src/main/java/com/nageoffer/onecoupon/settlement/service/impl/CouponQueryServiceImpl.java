/*
 * 牛券（oneCoupon）优惠券平台项目
 *
 * 版权所有 (C) [2024-至今] [山东流年网络科技有限公司]
 *
 * 保留所有权利。
 *
 * 1. 定义和解释
 *    本文件（包括其任何修改、更新和衍生内容）是由[山东流年网络科技有限公司]及相关人员开发的。
 *    "软件"指的是与本文件相关的任何代码、脚本、文档和相关的资源。
 *
 * 2. 使用许可
 *    本软件的使用、分发和解释均受中华人民共和国法律的管辖。只有在遵守以下条件的前提下，才允许使用和分发本软件：
 *    a. 未经[山东流年网络科技有限公司]的明确书面许可，不得对本软件进行修改、复制、分发、出售或出租。
 *    b. 任何未授权的复制、分发或修改都将被视为侵犯[山东流年网络科技有限公司]的知识产权。
 *
 * 3. 免责声明
 *    本软件按"原样"提供，没有任何明示或暗示的保证，包括但不限于适销性、特定用途的适用性和非侵权性的保证。
 *    在任何情况下，[山东流年网络科技有限公司]均不对任何直接、间接、偶然、特殊、典型或间接的损害（包括但不限于采购替代商品或服务；使用、数据或利润损失）承担责任。
 *
 * 4. 侵权通知与处理
 *    a. 如果[山东流年网络科技有限公司]发现或收到第三方通知，表明存在可能侵犯其知识产权的行为，公司将采取必要的措施以保护其权利。
 *    b. 对于任何涉嫌侵犯知识产权的行为，[山东流年网络科技有限公司]可能要求侵权方立即停止侵权行为，并采取补救措施，包括但不限于删除侵权内容、停止侵权产品的分发等。
 *    c. 如果侵权行为持续存在或未能得到妥善解决，[山东流年网络科技有限公司]保留采取进一步法律行动的权利，包括但不限于发出警告信、提起民事诉讼或刑事诉讼。
 *
 * 5. 其他条款
 *    a. [山东流年网络科技有限公司]保留随时修改这些条款的权利。
 *    b. 如果您不同意这些条款，请勿使用本软件。
 *
 * 未经[山东流年网络科技有限公司]的明确书面许可，不得使用此文件的任何部分。
 *
 * 本软件受到[山东流年网络科技有限公司]及其许可人的版权保护。
 */

package com.nageoffer.onecoupon.settlement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nageoffer.onecoupon.settlement.dao.entity.UserCouponDO;
import com.nageoffer.onecoupon.settlement.dao.mapper.UserCouponMapper;
import com.nageoffer.onecoupon.settlement.dto.req.QueryCouponsReqDTO;
import com.nageoffer.onecoupon.settlement.dto.resp.CouponsRespDTO;
import com.nageoffer.onecoupon.settlement.dto.resp.QueryCouponsRespDTO;
import com.nageoffer.onecoupon.settlement.service.CouponQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 查询用户可用 / 不可用优惠券列表接口
 * <p>
 * 作者：Henry Wan
 * 加项目群：早加入就是优势！500人内部项目群，分享的知识总有你需要的 <a href="https://t.zsxq.com/cw7b9" />
 * 开发时间：2024-07-25
 */
@Service
@RequiredArgsConstructor
public class CouponQueryServiceImpl implements CouponQueryService {

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final ObjectMapper objectMapper; // 用于 JSON 序列化和反序列化
    private final StringRedisTemplate stringRedisTemplate;
    private static final String COUPON_CACHE_KEY_PREFIX = "user:coupons:";

    /**
     * 查询用户可用和不可用的优惠券列表，返回 CouponsRespDTO 对象
     *
     * @param requestParam 查询参数
     * @return CompletableFuture<CouponsRespDTO> 包含可用和不可用优惠券的分页结果
     */
    @Override
    public CompletableFuture<CouponsRespDTO> pageQueryUserCoupons(QueryCouponsReqDTO requestParam) {
        return CompletableFuture.supplyAsync(() -> {
            // 定义 Redis 操作对象
            ValueOperations<String, String> valueOps = stringRedisTemplate.opsForValue();

            // 构造缓存键
            String cacheKey = COUPON_CACHE_KEY_PREFIX + requestParam.getUserId() + ":" + requestParam.getPageNum() + ":" + requestParam.getPageSize();

            CouponsRespDTO cachedCoupons = null;

            try {
                // 尝试从缓存获取所有优惠券（获取的是JSON字符串）
                String cachedJson = valueOps.get(cacheKey);
                if (cachedJson != null) {
                    // 将 JSON 字符串反序列化为 CouponsRespDTO 对象
                    cachedCoupons = objectMapper.readValue(cachedJson, CouponsRespDTO.class);
                }
            } catch (Exception e) {
                // 记录缓存获取时的异常信息
                System.err.println("Error retrieving from Redis: " + e.getMessage());
                e.printStackTrace();
            }

            // 如果缓存命中，直接返回
            if (cachedCoupons != null) {
                return cachedCoupons;
            }

            // 查询用户所有优惠券
            IPage<UserCouponDO> allCouponsPage = queryAllUserCoupons(requestParam);

            // 区分可用和不可用优惠券
            List<QueryCouponsRespDTO> availableCoupons = new ArrayList<>();
            List<QueryCouponsRespDTO> unavailableCoupons = new ArrayList<>();

            for (UserCouponDO coupon : allCouponsPage.getRecords()) {
                QueryCouponsRespDTO dto = convertToRespDTO(coupon);
                if (coupon.getStatus() == 0) { // 状态为0表示可用
                    availableCoupons.add(dto);
                } else { // 其他状态表示不可用
                    unavailableCoupons.add(dto);
                }
            }

            // 创建分页对象
            IPage<QueryCouponsRespDTO> availableCouponsPage = new Page<>(allCouponsPage.getCurrent(), allCouponsPage.getSize(), allCouponsPage.getTotal());
            availableCouponsPage.setRecords(availableCoupons);

            IPage<QueryCouponsRespDTO> unavailableCouponsPage = new Page<>(allCouponsPage.getCurrent(), allCouponsPage.getSize(), allCouponsPage.getTotal());
            unavailableCouponsPage.setRecords(unavailableCoupons);

            // 构造返回对象
            CouponsRespDTO response = CouponsRespDTO.builder()
                    .availableCoupons(availableCouponsPage)
                    .unavailableCoupons(unavailableCouponsPage)
                    .build();

            try {
                // 将 CouponsRespDTO 对象序列化为 JSON 字符串
                String responseJson = objectMapper.writeValueAsString(response);

                valueOps.set(cacheKey, responseJson);
                // 缓存结果并设置失效时间为 1 小时
                // valueOps.set(cacheKey, response, 1, TimeUnit.HOURS);
            } catch (Exception e) {
                // 记录缓存存储时的异常信息
                System.err.println("Error storing to Redis: " + e.getMessage());
                e.printStackTrace();
            }

            return response;
        });
    }

    /**
     * 分页查询用户所有优惠券
     *
     * @param requestParam 查询参数
     * @return 用户优惠券的分页结果
     */
    private IPage<UserCouponDO> queryAllUserCoupons(QueryCouponsReqDTO requestParam) {
        // 创建分页对象
        Page<UserCouponDO> page = new Page<>(requestParam.getPageNum(), requestParam.getPageSize());

        // 创建查询条件
        QueryWrapper<UserCouponDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", requestParam.getUserId())
                .orderByDesc("id");

        // 执行分页查询
        return userCouponMapper.selectPage(page, queryWrapper);
    }

    /**
     * 转换 UserCouponDO 对象为 QueryCouponsRespDTO
     *
     * @param userCoupon 用户优惠券对象
     * @return 响应DTO
     */
    private QueryCouponsRespDTO convertToRespDTO(UserCouponDO userCoupon) {
        return QueryCouponsRespDTO.builder()
                .couponTemplateId(userCoupon.getCouponTemplateId())
                .receiveTime(userCoupon.getReceiveTime())
                .validStartTime(userCoupon.getValidStartTime())
                .validEndTime(userCoupon.getValidEndTime())
                .status(userCoupon.getStatus())
                .build();
    }
}