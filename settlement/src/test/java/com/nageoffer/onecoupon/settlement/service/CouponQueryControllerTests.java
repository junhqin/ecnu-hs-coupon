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

package com.nageoffer.onecoupon.settlement.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nageoffer.onecoupon.settlement.controller.CouponQueryController;
import com.nageoffer.onecoupon.settlement.dto.req.QueryCouponsReqDTO;
import com.nageoffer.onecoupon.settlement.dto.resp.CouponsRespDTO;
import com.nageoffer.onecoupon.settlement.dto.resp.QueryCouponsRespDTO;
import com.nageoffer.onecoupon.settlement.handler.AsyncResponseHandler;
import com.nageoffer.onecoupon.settlement.dao.mapper.UserCouponMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * CouponQueryControllerTests 用于测试 CouponQueryController 中的接口逻辑
 * <p>
 * 数据库和 Redis 中的数据需要符合测试要求。
 * <p>
 * 插入语句用于生成数据库测试数据：
 * INSERT INTO t_user_coupon (
 * id, user_id, order_sn, coupon_template_id, receive_time,
 * receive_count, valid_start_time, valid_end_time, use_time,
 * source, status, create_time, update_time, del_flag
 * ) VALUES (
 * 1812833908648099852, 1812833908648099852, NULL, 1810966706881941507, '2024-07-15 16:46:05',
 * NULL, '2024-07-20 16:46:05', '2024-07-25 17:18:04', NULL,
 * NULL, 0, NULL, NULL, 0
 * );
 */
public class CouponQueryControllerTests {

    // 用于模拟 HTTP 请求的 MockMvc 对象
    private MockMvc mockMvc;

    // 模拟 CouponQueryService 服务
    @Mock
    private CouponQueryService couponQueryService;

    // 模拟 RedisTemplate，用于 Redis 交互
    @Mock
    private StringRedisTemplate stringRedisTemplate;

    // 模拟 ValueOperations，便于 Redis 操作
    @Mock
    private ValueOperations<String, String> valueOperations;

    // 模拟 AsyncResponseHandler，用于处理异步请求
    @Mock
    private AsyncResponseHandler asyncResponseHandler;

    // 模拟 UserCouponMapper，用于数据库交互
    @Mock
    private UserCouponMapper userCouponMapper;

    // 模拟 RedissonClient 和 RLock，用于分布式锁
    @Mock
    private RedissonClient redissonClient;

    @Mock
    private RLock lock;

    // 将 Mock 对象注入到 CouponQueryController 控制器
    @InjectMocks
    private CouponQueryController couponQueryController;

    // 时间格式化工具，用于解析日期
    private static final SimpleDateFormat DATE_FORMAT;

    static {
        DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    /**
     * 测试初始化，配置 Mock 环境
     */
    @BeforeEach
    public void setup() {
        // 初始化 Mockito 注解
        MockitoAnnotations.openMocks(this);

        // 创建 ObjectMapper 对象，用于 JSON 序列化
        ObjectMapper objectMapper = new ObjectMapper();

        // 配置 MockMvcBuilder，注入测试的控制器
        mockMvc = MockMvcBuilders.standaloneSetup(couponQueryController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();

        // 模拟 Redis 操作
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);

        // 模拟 Redisson 锁
        when(redissonClient.getLock(any(String.class))).thenReturn(lock);
    }

    /**
     * 解析日期字符串为 Date 对象
     *
     * @param date 日期字符串
     * @return Date 对象
     * @throws ParseException 日期解析异常
     */
    private Date parseDate(String date) throws ParseException {
        return DATE_FORMAT.parse(date);
    }

    /**
     * 测试查询用户可用和不可用优惠券的方法
     *
     * @throws Exception 可能抛出的异常
     */
    @Test
    public void testPageQueryUserCoupons() throws Exception {
        // 设置模拟数据：可用优惠券列表
        List<QueryCouponsRespDTO> availableCoupons = new ArrayList<>();
        availableCoupons.add(QueryCouponsRespDTO.builder()
                .couponTemplateId(1810966706881941507L)
                .receiveTime(parseDate("2024-07-15 16:46:05"))
                .validStartTime(parseDate("2024-07-20 16:46:05"))
                .validEndTime(parseDate("2024-07-25 17:18:04"))
                .status(0) // 状态 0 表示优惠券可用
                .build());

        // 创建可用优惠券的分页对象
        IPage<QueryCouponsRespDTO> availableCouponsPage = new Page<>();
        availableCouponsPage.setRecords(availableCoupons);
        availableCouponsPage.setTotal(1);
        availableCouponsPage.setSize(10);
        availableCouponsPage.setCurrent(1);
        availableCouponsPage.setPages(1);

        // 设置模拟数据：不可用优惠券列表
        List<QueryCouponsRespDTO> unavailableCoupons = new ArrayList<>();
        unavailableCoupons.add(QueryCouponsRespDTO.builder()
                .couponTemplateId(1810966706881941510L)
                .receiveTime(parseDate("2024-07-10 16:46:05"))
                .validStartTime(parseDate("2024-07-15 16:46:05"))
                .validEndTime(parseDate("2024-07-20 17:18:04"))
                .status(1) // 状态 1 表示优惠券不可用
                .build());

        // 创建不可用优惠券的分页对象
        IPage<QueryCouponsRespDTO> unavailableCouponsPage = new Page<>();
        unavailableCouponsPage.setRecords(unavailableCoupons);
        unavailableCouponsPage.setTotal(1);
        unavailableCouponsPage.setSize(10);
        unavailableCouponsPage.setCurrent(1);
        unavailableCouponsPage.setPages(1);

        // 构建 CouponsRespDTO 对象
        CouponsRespDTO couponsRespDTO = CouponsRespDTO.builder()
                .availableCoupons(availableCouponsPage)
                .unavailableCoupons(unavailableCouponsPage)
                .build();

        // 模拟服务层返回 CompletableFuture 对象
        CompletableFuture<CouponsRespDTO> couponsFuture = CompletableFuture.completedFuture(couponsRespDTO);
        when(couponQueryService.pageQueryUserCoupons(any(QueryCouponsReqDTO.class)))
                .thenReturn(couponsFuture);

        // 模拟 AsyncResponseHandler 的行为
        when(asyncResponseHandler.createDeferredResult(couponsFuture))
                .thenCallRealMethod();

        // 执行测试请求
        MvcResult mvcResult = mockMvc.perform(get("/api/settlement/coupon-query/page")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userId", "1812833908648099852")  // 传入用户ID
                        .param("pageNum", "1")  // 传入页码
                        .param("pageSize", "10"))  // 传入页面大小
                .andExpect(request().asyncStarted())
                .andReturn();

        // 等待异步请求完成并进行断言
        mockMvc.perform(asyncDispatch(mvcResult)) // 等待异步请求完成
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE)) // 期待返回 JSON 格式
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isNotEmpty());

        // 获取实际的 JSON 响应
        String actualJson = mvcResult.getResponse().getContentAsString();
        System.out.println("实际的 JSON 响应: " + actualJson);

        // 定义预期的 JSON 响应
        String expectedJson = """
                    {
                    "code": "0",
                    "message": null,
                    "data": {
                        "availableCoupons": {
                            "records": [
                                {
                                    "couponTemplateId": 1810966706881941507,
                                    "couponName": null,
                                    "receiveTime": "2024-07-15 16:46:05",
                                    "validStartTime": "2024-07-20 16:46:05",
                                    "validEndTime": "2024-07-25 17:18:04",
                                    "status": 0
                                }
                            ],
                            "total": 1,
                            "size": 10,
                            "current": 1,
                            "pages": 1
                        },
                        "unavailableCoupons": {
                            "records": [
                                {
                                    "couponTemplateId": 1810966706881941510,
                                    "couponName": null,
                                    "receiveTime": "2024-07-10 16:46:05",
                                    "validStartTime": "2024-07-15 16:46:05",
                                    "validEndTime": "2024-07-20 17:18:04",
                                    "status": 1
                                }
                            ],
                            "total": 1,
                            "size": 10,
                            "current": 1,
                            "pages": 1
                        }
                    },
                    "requestId": null,
                    "success": true,
                    "fail": false
                }
                """;

        // 使用 JSONAssert 验证实际响应与预期是否一致
        JSONAssert.assertEquals(expectedJson, actualJson, false);
    }
}
