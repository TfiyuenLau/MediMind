# 工程简介

**MediMind** 是一个**基于知识图谱的AI诊断辅助系统**，它面向医生群体且实现了与上级的HIS系统接口对接。与传统HIS不同的是，MediMind在具有医生站信息管理系统的同时也拥有医生即时通讯与知识图谱辅助诊断用药推荐的功能模块。

在**与上级HIS的兼容**替代方面，MediMind采用了**消息同步**的方式，实现了**与HIS的数据同步**。这样，医生可以在MediMind中完成所有的信息管理、即时通讯和辅助诊断等工作，而无需再使用传统的HIS系统。这样做有助于提高医生的工作效率，减少医生在不同系统间来回切换的时间和精力浪费。

在**知识图谱辅助技术**方面，MediMind利用了AI技术构建了一个**知识图谱**，在医生诊断时提供了可靠的辅助决策。通过对大量医疗文献、病例和用药数据的学习，MediMind能够根据患者的病情和病史，为医生提供精准的用药建议，帮助医生更快、更准确地诊断和治疗疾病。

在医生**团队通讯协作**方面，MediMind 采用 WebSocket 通信技术，为用户提供医护人员之间的在线即时通讯和日程安排管理等功能，提高交流频率与沟通效率，提升医生协作的效率。

总之，MediMind是一个具有创新性的、与上级HIS系统兼容的、拥有知识图谱辅助诊断用药推荐功能的医疗辅助系统。它将为医生提供更高效、更精准、更便捷的医疗服务，为患者提供更好的医疗体验。

## 技术选型与开源组件
* **编程语言**：Java11
* **项目构建管理工具**：Apache Maven
* **服务器**：Apache Tomcat
* **开发框架**：SpringBoot
* **微服务框架**：SpringCloud（Fegin、Gateway）、SpringCloudAlibaba（Nacos）
* **数据库**：MySql、Redis
* **数据库框架**：MyBatis、MybatisPlus
* **数据库连接池**：Alibaba Druid、Commons Pool2
* **图数据库**：Neo4j
* **检索引擎**：ElasticSearch
* **认证鉴权**：Sa-Token
* **消息队列**：RabbitMQ
* **性能测试**：Jmeter
* **接口文档工具**：Swagger2

## 项目结构
MediMind采用**微服务架构**以达到解耦与提高分布式部署效率与性能的目的。

~~~
MediMind:
├─ai-diagnosis  # AI辅助诊断模块
│  └─src
│      ├─main
│      │  ├─java
│      │  │  └─edu
│      │  │      └─hbmu
│      │  │          └─aidiagnosis
│      │  │              ├─config  # 配置类
│      │  │              ├─controller  # 前端控制器
│      │  │              ├─dao  # 数据访问层
│      │  │              ├─domain  # pojo层
│      │  │              │  ├─node  # Neo4j节点实体类
│      │  │              │  ├─relationship  # 关系实体类
│      │  │              │  └─response  # VO视图对象类
│      │  │              ├─service  # 服务层
│      │  │              │  └─impl  # 服务层实现类
│      │  │              └─util  # 工具类
│      │  └─resources  # 资源文件
│      │      └─dict
│      └─test  # 测试类
│          └─java
│              └─edu
│                  └─hbmu
│                      └─aidiagnosis
├─cooperation-service  # 医疗团队通讯协作模块
│  └─src
│      ├─main
│      │  ├─java
│      │  │  └─edu
│      │  │      └─hbmu
│      │  │          └─cooperation
│      │  │              ├─config
│      │  │              ├─controller
│      │  │              ├─dao
│      │  │              ├─domain
│      │  │              │  ├─entity
│      │  │              │  ├─request
│      │  │              │  └─response
│      │  │              ├─generator
│      │  │              ├─service
│      │  │              │  └─impl
│      │  │              ├─util
│      │  │              └─websocket
│      │  └─resources
│      │      └─mapper
│      └─test
│          └─java
│              └─edu
│                  └─hbmu
│                      └─cooperation
├─feign-api  # 微服务远程调用模块
│  ├─.mvn
│  │  └─wrapper
│  └─src
│      ├─main
│      │  ├─java
│      │  │  └─edu
│      │  │      └─hbmu
│      │  │          └─fegin
│      │  │              ├─client
│      │  │              └─domain
│      │  │                  ├─entity
│      │  │                  └─response
│      │  └─resources
│      └─test
│          └─java
│              └─edu
│                  └─hbmu
│                      └─fegin
├─gateway  # 微服务网关模块
│  ├─.mvn
│  │  └─wrapper
│  └─src
│      ├─main
│      │  ├─java
│      │  │  └─edu
│      │  │      └─hbmu
│      │  │          └─gateway
│      │  └─resources
│      └─test
│          └─java
│              └─edu
│                  └─hbmu
│                      └─gateway
└─outpatient-service  # 门诊服务模块
    ├─.mvn
    │  └─wrapper
    └─src
        ├─main
        │  ├─java
        │  │  └─edu
        │  │      └─hbmu
        │  │          └─outpatient
        │  │              ├─config
        │  │              ├─constants
        │  │              ├─controller
        │  │              ├─dao
        │  │              ├─domain
        │  │              │  ├─doc
        │  │              │  ├─entity
        │  │              │  ├─request
        │  │              │  └─response
        │  │              ├─generator
        │  │              ├─service
        │  │              │  └─impl
        │  │              ├─spider
        │  │              └─util
        │  └─resources
        │      ├─dict
        │      └─mapper
        └─test
            └─java
                └─edu
                    └─hbmu
                        └─outpatient
~~~

# 延伸阅读
以下是您可能会感兴趣的内容：

## demo：

## 仓库地址：

## 接口文档：

## 开发文档：
