/**
 * data collection
 * 
 * @author douxy
 */
(function(tk) {
	var version="1.0.1";
	if (tk.hasLoad)
		return;
	var tj = {};
	tj.hasLoad = true;
	tj.cfg = TK_CFG || {};
	var _cfg = {
		max_referrer_string_length : 200,
		max_string_length : 500,
		cross_subdomain : true,
		show_log : true,
		debug_mode : false,
		debug_mode_upload : false,
		// session标识
		sessionId : undefined,
		use_client_time : false,
		// 来源参数名字
		source_channel : [],
		url : ("https:" == document.location.protocol ? "https://" : "http://")
				+ "//tk.twotiger.cn/tj.gif?token=" + tj.cfg.token
	};
	var l = tk.lang;// 工具包
	detector = detector;// 终端信息
	l.extend(_cfg, tj.cfg);// 配置更新
	tj.info = {
		initPage : function() {
			var referrer = l.getReferrer();
			var referrer_host = referrer ? l.url('hostname', referrer)
					: referrer;
			var referrer_domain = referrer ? l.url('domain', referrer)
					: referrer;
			var url = location.href;
			var url_host = url ? l.url('hostname', url) : url;
			var url_domain = url ? l.url('domain', url) : url;
			this.pageProp = {
				referrer : referrer,
				referrer_host : referrer_host,
				referrer_domain : referrer_domain,
				url : url,
				url_host : url_host,
				url_path:l.url("path",url),
				url_domain : url_domain,
				url_title:l.getTitle()
			};
		},
		pageProp : {},
		// 预置属性
		properties : function() {
			return {
				$os : detector.os.name,
				$model : detector.device.name,
				$os_version : String(detector.os.version),
				$screen_height : Number(screen.height) || 0,
				$screen_width : Number(screen.width) || 0,
				$browser : detector.browser.name,
				$browser_version : String(detector.browser.version)
			};
		},
		// 保存临时的一些变量，只针对当前页面有效
		currentProps : {},
		register : function(obj) {
			l.extend(l.info.currentProps, obj);
		}
	};
	// 初始化基础参数
	tj.info.initPage();

	//
	// 发射器
	//
	tj.sender = {}
	// 成功发送数
	tj.sender._complete = 0;
	// 接受发送数
	tj.sender._receive = 0;
	tj.sender.send = function(data, callback) {
		++this._receive;
		var state = '_state' + this._receive;
		var me = this;
		// 加cache防止缓存
		data._nocache = (String(Math.random()) + String(Math.random()) + String(Math
				.random())).slice(2, 15);
		logger.info(data);
		data = JSON.stringify(data);

		this[state] = document.createElement('img');
		this[state].onload = this[state].onerror = function(e) {
			me[state].onload = null;
			me[state].onerror = null;
			delete me[state];
			++me._complete;
			logger.info(e);
			(typeof callback === 'function') && callback(data);
		};
		if (_cfg.url.indexOf('?') !== -1) {
			this[state].src = _cfg.url + '&data='
					+ encodeURIComponent(l.base64Encode(data));
		} else {
			this[state].src = _cfg.url + '?data='
					+ encodeURIComponent(l.base64Encode(data));
		}
	};

	/**
	 * 
	 * 分发设备标识
	 * 
	 */
	var tkid = l.cookie.get("TKID")
	if (!tkid) {
		tkid = l.UUID();
	}
	/**
	 * 每次更新保证持续有效性
	 */
	l.cookie.set("TKID", tkid, 365, true, false);
	/**
	 * sessionId
	 */
	var sessionId = _cfg.sessionId || l.cookie.get("TKSSID");
	if (!sessionId) {
		sessionId = l.UUID();
		l.cookie.set("TKSSID", sessionId, 0, true, false)
	}

	/**
	 * 追踪页面 pageview
	 * 
	 */
	tj.trackPV = function(data) {
		var data = l.extend({
			"type" : "track",
			"event" : "pageview",
			"version" : version,
			"tkid" : tkid,
			"ssId" : sessionId
		}, data || {});
		var properties = tj.info.properties();// 基础属性、所有请求必须携带此信息
		l.extend(properties, tj.info.pageProp);// 页面信息
		l.extend(properties,tj.info.currentProps);// 当前页面信息
		l.extend(data, {"properties" : properties})//设备信息
		tj.sender.send(data);
	};
	/**
	 * 事件追踪
	 */
	tj.trackEvt = function(data) {
		var data = l.extend({
			"type" : "track",
			"event" : "default",
			"version" : version,
			"tkid" : tkid,
		}, data || {});
		
		var properties = tj.info.properties();// 基础属性、所有请求必须携带此信息
		l.extend(properties, tj.info.pageProp);// 页面信息
		l.extend(properties, tj.info.currentProps);// 当前页面信息
		l.extend(data, {
			"properties" : properties
		})
		tj.sender.send(data);
	}

	// 加载即刻执行
	tj.trackPV();
	// 注册到域
	tk = tj;
	
})(tk || {});