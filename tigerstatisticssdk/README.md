# 数据统计sdk
为应用统计数据并发送至服务器的sdk

## 项目结构介绍
* *custom*：自定义数据事件的统计方式，例如pageview表示统计用户浏览轨迹，以及单个停留时长
* *network*：数据上传服务器的网络层
* *util*：各种信息采集的工具类
* *Constant*：常量类
* *NetStrategy*：数据上传策略类，自定义数据上传方式
* *TkAgent*：sdk对外暴漏的类，提供统计接口
* *TKLog*：日志打印类

## sdk发布maven流程

* 根目录build.gradle中填写nexus库的username及password

* （需升级maven远程库版本时）修改对应build.gradle中的versionCode（+1）

* Terminal中执行gradlew命令：

    全部发布

    gradlew uploadArchives

    只发布某个module

    gradlew :moudleName:uploadArchives


