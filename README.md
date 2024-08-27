## ©️版权告示

为了保障星球用户权益，牛券 `oneCoupon` 不再实行开源策略，而是通过邀请星球用户进入私有项目进行学习。

**严禁未经本项目原作者明确书面授权擅自分享至 GitHub、Gitee 等任何开放平台。违者将面临版权法律追究。**

- 知识星球 [《侵权责任法》、《著作权法》和《信息网络传播权保护条例》](https://support.zsxq.com/guidance.html)。
- 项目版权[《中华人民共和国著作权法实施条例》](https://gitcode.net/nageoffer/onecoupon/-/blob/main/copyright/%E4%B8%AD%E5%8D%8E%E4%BA%BA%E6%B0%91%E5%85%B1%E5%92%8C%E5%9B%BD%E8%91%97%E4%BD%9C%E6%9D%83%E6%B3%95%E5%AE%9E%E6%96%BD%E6%9D%A1%E4%BE%8B.pdf)。

## 什么是牛券 oneCoupon？

牛券是一款高性能优惠券系统，与其他网上优惠券系统不同，**牛券能够承受近十万次查询和分发请求的高并发压力**。

项目旨在帮助校招和社招的同学掌握足够的亮点，为获得理想的 offer 助力。此次代码实现非常优雅，甚至细致到参数定义都蕴含深意，值得大家学习借鉴。

其中的一些亮点部分已重点标记，大家可根据实际情况学习即可。该图会持续更新。

![](https://oss.open8gu.com/image-20240723011449928.png)

## 技术架构

我们选择了基于 Spring Boot 3 和 JDK17 进行底层建设，同时组件库的版本大多也是最新的。这样做既能享受新技术带来的性能提升，也能体验到新特性带来的惊喜。

如果用一张图来概括牛券的技术架构，其展现形态如下图所示。

![](https://oss.open8gu.com/image-20240722104707368.png)

技术架构涵盖了 SpringBoot 3、SpringCloudAlibaba、Nacos、Sentinel、Skywalking、RocketMQ 5.x、ElasticSearch、Redis、MySQL、EasyExcel、XXL-Job、Redisson 等技术。

框架技术和版本号关系如下表格所示。

|      | 技术                | 名称               | 版本           | 官网                                                         |
| ---- | ------------------- | ------------------ | -------------- | ------------------------------------------------------------ |
| 1    | Spring Boot         | 基础框架           | 3.0.7          | [https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot) |
| 2    | SpringCloud Alibaba | 分布式框架         | 2022.0.0.0-RC2 | [https://github.com/alibaba/spring-cloud-alibaba](https://github.com/alibaba/spring-cloud-alibaba) |
| 3    | SpringCloud Gateway | 网关框架           | 2022.0.3       | [https://spring.io/projects/spring-cloud-gateway](https://spring.io/projects/spring-cloud-gateway) |
| 4    | MyBatis-Plus        | 持久层框架         | 3.5.7          | [https://baomidou.com](https://baomidou.com)                 |
| 5    | MySQL               | OLTP 关系型数据库  | 5.7.36         | https://www.mysql.com/cn                                     |
| 6    | Redis               | 分布式缓存数据库   | Latest         | [https://redis.io](https://redis.io)                         |
| 7    | RocketMQ            | 消息队列           | 2.3.0          | [https://rocketmq.apache.org](https://rocketmq.apache.org)   |
| 8    | ShardingSphere      | 数据库生态系统     | 5.3.2          | [https://shardingsphere.apache.org](https://shardingsphere.apache.org) |
| 9    | FastJson2           | JSON 序列化工具    | 2.0.36         | [https://github.com/alibaba/fastjson2](https://github.com/alibaba/fastjson2) |
| 10   | Canal               | BinLog 订阅组件    | 1.1.6          | [https://github.com/alibaba/canal](https://github.com/alibaba/canal) |
| 11   | HuTool              | 小而全的工具集项目 | 5.8.27         | [https://hutool.cn](https://hutool.cn)                       |
| 12   | Maven               | 项目构建管理       | 3.9.1          | [http://maven.apache.org](http://maven.apache.org)           |
| 13   | Redisson            | Redis Java 客户端  | 3.27.2         | [https://redisson.org](https://redisson.org/)                |
| 14   | Sentinel            | 流控防护框架       | 1.8.6          | [https://github.com/alibaba/Sentinel](https://github.com/alibaba/Sentinel) |
| 15   | XXL-Job             | 分布式定时任务框架 | 2.4.1          | [http://www.xuxueli.com/xxl-job](http://www.xuxueli.com/xxl-job) |
| 16   | BizLog              | 操作日志工具       | 3.0.6          | https://github.com/mouzt/mzt-biz-log                         |
| 17   | EasyExcel           | Excel 处理工具     | 4.0.1          | https://easyexcel.opensource.alibaba.com                     |
| 18   | ElasticSearch       | 分布式搜索引擎     | TODO           | https://github.com/elastic/elasticsearch                     |

**学习项目需要什么前置技术？**

虽然上面的技术点用到的很多，但是很多只是知道框架是做什么，会使用 API 即可满足开发条件，不需要深入原理。所以看着技术点比较多，但是上手必须的框架技术却很少。

从项目学习的角度上，**大家需要至少做过一个 SpringBoot 项目，比如点评、外卖或者 SaaS 短链接**。掌握了基本开发流程，就可以上手开始项目。

`分布式缓存 Redis`、`消息队列 RocketMQ` 以及 `SpringCloud 微服务`在项目中大量运用，建议大家可以学习下（知道怎么用就行），并结合项目真实使用情况加以理解。

##  加群沟通

为了更加便捷地沟通和分享，我创建了一个专属的微信会员群。群内技术氛围浓厚，许多同学在这里交流技术和面试经验，大家共同成长。

扫描我的下方二维码，在微信中扫描添加好友。

![](https://oss.open8gu.com/1_990064918_171_84_3_716500817_c4659af930df3a2532d02b8fcc0f0cbe.png)

添加时备注：**星球编号**，好友通过后请发送截图右侧的星球个人详情页。

![](https://oss.open8gu.com/image-20240722111319147.png)

## 项目亮点&如何学习？

> 牛券 oneCoupon 先发布代码，文档资料和视频还都在录制中，大家先根据代码功能入口查看逻辑。

牛券 oneCoupon 系统拆分了六个模块，分别是引擎模块、分发模块、结算模块、后管模块、搜索模块以及网关模块。

### 1. 后管模块

#### 1.1 新增优惠券模板

代码入口：

- com.nageoffer.onecoupon.merchant.admin.controller.CouponTemplateController#createCouponTemplate

业务亮点：

- 通过自定义注解 `@NoRepeatSubmit` 防止用户重复点击创建按钮，进而导致多个重复请求同时创建相同优惠券模板。
- 通过责任链模式验证商家创建优惠券提交参数是否正确，保障验证代码高内聚、低耦合。
- 通过 BizLog 操作日志框架记录商家对优惠券的操作行为，比如创建、增加发行量、结束等逻辑，确保系统行为留痕。
- 为了支持大量商家创建优惠券记录，采用 ShardingSphere 分库分表方案，以提升优惠券模板的存储和查询效率。

#### 1.2 优惠券推送任务

代码入口：

- com.nageoffer.onecoupon.merchant.admin.controller.CouponTaskController#createCouponTask

业务亮点：

- 通过自定义注解 `@NoRepeatSubmit` 防止用户重复点击创建按钮，进而导致多个重复请求同时创建相同优惠券推送任务。
- 使用 `EasyExcel` 解析百万量级用户优惠券推送 Excel 文件，避免因文件过大导致的内存溢出问题。
- 通过线程池 `ThreadPoolExecutor` 和 Redisson 延时队列异步执行解析 Excel 文件行数，提高创建优惠券推送接口响应时间。
- 为避免系统耦合，通过消息队列 RocketMQ 解耦用户优惠券推送和通知逻辑，后管模块发送推送消息并由分发模块进行消费。
- 支持定时优惠券推送任务执行，通过 XXL-Job 定时任务扫描数据库记录，如达到发送时间调用消息队列触发执行流程。
- 解决数据库查询IN语句导致的跨库连接表不存在问题，通过数据库粒度拆分成多个单独查询，然后在应用层进行合并方式解决。

### 2. 分发模块

该模块没有 Controller 控制层，不接受外来 HTTP 调用请求，仅通过两个消息队列消费者提供执行动作。

代码入口：

- 延时推送任务消费者：com.nageoffer.onecoupon.distribution.mq.consumer.CouponTaskSendExecuteConsumer#onMessage
- 用户分发优惠券消费者：com.nageoffer.onecoupon.distribution.mq.consumer.CouponTaskExecuteConsumer#onMessage

业务亮点：

- 使用 `EasyExcel` 解析百万量级用户优惠券推送 Excel 文件，避免因文件过大导致的内存溢出问题。
- 通过执行点位和批量处理流程优化优惠券推送业务，可保障项目断电从上次执行位置开始以及更快速度将优惠券分发给用户。
- 为避免优惠券分发过程中库存不足的问题，通过乐观锁机制确保库存不会被多扣，并采用自旋重试直至成功将优惠券库存扣减至零。
- 通过创建用户优惠券表的唯一索引支持幂等逻辑，确保优惠券限制条件（如用户只能领取一次）生效。发放优惠券时，如果用户已领取，捕获数据库重复记录异常，并采用乐观自旋机制反复尝试，直至成功添加不重复的记录。

### 3. 引擎模块

#### 3.1 查询优惠券模板

代码入口：

- com.nageoffer.onecoupon.engine.controller.CouponTemplateController#findCouponTemplate

业务亮点：

- 为防止恶意请求导致的缓存击穿和穿透问题，采用布隆过滤器、缓存空值和分布式锁等方法进行解决。

#### 3.2 兑换优惠券/秒杀

代码入口：

- com.nageoffer.onecoupon.engine.controller.UserCouponController#redeemUserCoupon

业务亮点：

- 通过 Lua 脚本对获取不到优惠券的用户执行快速失败，并采用编程式事务以减少事务时间。底层使用乐观锁机制，避免优惠券模板库存的多扣减。
- 为避免数据库扣减库存成功后添加用户优惠券缓存失败，基于监听 Binlog 机制异步添加用户优惠券缓存，并采用写后查询策略应对 Redis 持久化或主从复制极端情况下的数据丢失问题。

#### 3.3 优惠券预约提醒

代码入口：

- 创建预约提醒请求：com.nageoffer.onecoupon.engine.controller.CouponTemplateRemindController#createCouponRemind
- 消费用户提醒消息：com.nageoffer.onecoupon.engine.mq.consumer.CouponRemindConsumer#onMessage

业务亮点：

- 通过 `RocketMQ 5.x` 的任意延时消息队列，实现对用户推送精准抢券提醒消息。
- 使用 `ThreadPoolExecutor` 线程池来消费消息，并通过 `Redisson` 延时队列进行兜底复查，防止线程池任务丢失。
- 使用位图存储用户预约信息，通过移位、按位与、按位或、按位异或等操作进行计算，充分利用 CPU 特性。
- 使用布隆过滤器存储已取消提醒的用户信息，以便在消费消息时快速判断。

### 4. 结算模块

#### 4.1 订单金额结算

TODO

#### 4.2 用户优惠券列表

TODO

### 5. 搜索模块

TODO

### 6. 网关模块

TODO
