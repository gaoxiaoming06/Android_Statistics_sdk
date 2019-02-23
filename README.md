# sdk
地址：https://tk.twotiger.cn/tj.gif?token=sss&data=content  
content要求：base64(data);
# appSdk
#### 参数约定

    token：产品-终端标识、统一由数据组分发
    data：将所有参数封装为json字符串 A；
          对A 进行 base64输出 C;
          C 作为data 提交服务器；
    data结构示例：
    data={
        deviceName:"",//手机名称 
        deviceBrand:"",//手机品牌
        deviceModel:"",//机型
        isSocketProxy:"",//是否使用Socket代理 0-未使用，1-使用
        canGps:"",//是否支持gps 0-不支持，1-支持
        language:"",//语言环境 
        networkState:"",//网络类型
        netCarrier:"",//网络运营商
        devicePixel:"",//分辨率
        osVersion:"",//系统版本号
        imsi:"",//IMSI
        wifi_mac:"",//Wifi-mac地址
        imei:"",//IMEI|AND|
        androidId:"",//AndroidId|AND|
        isRoot:"",//是否ROOT 0-未ROOT，1-ROOT
        mac:"",//MMAC地址|
        isSimulator:"",//是否是模拟器 0-不是，1-是
        cpu_abi:"",//CPU_ABI|AND;CPU_ABI+CPU_ABI2|
        fingerprint:"",//设备指纹
        serial:"",///设备序列号
        isPrisonBreak:"",//是否越狱 0-未越狱，1-越狱
        openUdid:"",//OPENUDID|IOS|
        adfa:"",//ADFA|IOS|
        adfv:"",//ADFV|IOS|
        "list": [
            {
                className: 页面名称
                time: 启动时间
                activeTime: 停留时间
                type:"track",//类型
                event:"pageview",//事件名称-pageview-页面展示事件
                version:"1.0.0",//日志版本号
                tkid:"",//用户唯一标识
                ssId:"",//会话标识，每次启动分发新标识
                uid: //用户id
                channel: //渠道号
            },
            {
                eventId: 事件id
                time: 事件时间
                type:"track",//类型
                event:"click",//事件名称-click-点击事件
                version:"1.0.0",//日志版本号
                tkid:"",//用户唯一标识
                ssId:"",//会话标识，每次启动分发新标识
                uid: //用户id
                channel: //渠道号
            },
            {
                url: 连接地址
                time: 加载时间
                type:"track",//类型
                event:"click",//事件名称-click-点击事件
                version:"1.0.0",//日志版本号
                tkid:"",//用户唯一标识
                ssId:"",//会话标识，每次启动分发新标识
                uid: //用户id
                channel: //渠道号
            }
        ]
    }
# jsSdk

