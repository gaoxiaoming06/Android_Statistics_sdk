(function() {
	var yc = {},
		h = {
			p: "tongji.yicha.cn/y.gif",
			fs: 'fsite',
			kw: 'keyword',
			pgn: 'pageNo',
			pno: 'pno',
			protocol: "http:",
			q: 5964487627,
			Q: "dz,pr,fs,yu,kw,pno,ck,cl,ds,ep,fl,ja,ln,lt,rnd,su,v,ul,pn,iat,lc,ty,ac,na,us,ud".split(','),
			b: {}
		};
	var p = !0,
		s = null,
		t = !1;
	yc.lang = {};
	yc.lang.d = function(a, b) {
		return "[object " + b + "]" === {}.toString.call(a)
	};
	yc.cookie = {};
	yc.cookie.get = function(a) {
		return (a = RegExp("(^| )" + a + "=([^;]*)(;|$)").exec(document.cookie)) ? a[2] : s
	};
	yc.sessionStorage = {};
	yc.sessionStorage.set = function(a, b) {
		if (window.sessionStorage) try {
			window.sessionStorage.setItem(a, b)
		} catch (f) {}
	};
	yc.sessionStorage.get = function(a) {
		return window.sessionStorage ? window.sessionStorage.getItem(a) : s
	};
	yc.sessionStorage.remove = function(a) {
		window.sessionStorage && window.sessionStorage.removeItem(a)
	};
	yc.url = {};
	yc.url.k = function(a, b) {
		var f = a.match(RegExp("(^|&|\\?|#)(" + b + ")=([^&#]*)(&|$|#)", ""));
		return f ? f[3] : s
	};
	yc.url.va = function(a) {
		return (a = a.match(/^(https?:)\/\//)) ? a[1] : s
	};
	yc.url.Z = function(a) {
		return (a = a.match(/^(https?:\/\/)?([^\/\?#]*)/)) ? a[2].replace(/.*@/, "") : s
	};
	yc.url.K = function(a) {
		return (a = yc.url.Z(a)) ? a.replace(/:\d+$/, "") : a
	};
	yc.url.ua = function(a) {
		return (a = a.match(/^(https?:\/\/)?[^\/]*(.*)/)) ? a[2].replace(/[\?#].*/, "").replace(/^$/, "/") : s
	};
	yc.g = {};
	yc.g.xa = /msie (\d+\.\d+)/i.test(navigator.userAgent);
	yc.g.cookieEnabled = navigator.cookieEnabled;
	yc.g.javaEnabled = navigator.javaEnabled();
	yc.g.language = navigator.language || navigator.browserLanguage || navigator.systemLanguage || navigator.userLanguage || "";
	yc.g.ia = (window.screen.width || 0) + "x" + (window.screen.height || 0);
	yc.g.colorDepth = window.screen.colorDepth || 0;
	yc.g.pr = function() {
		var script = (document.getElementsByTagName('script'));
		var l = script.length;
		var pronum = '';
		for (var i = 0; i < l; i++) {
			pronum = !! document.querySelector ? script[i].src : script[i].getAttribute('src', 4);
			if (pronum.substr(pronum.lastIndexOf('/')).indexOf('/lg.js') !== -1 || pronum.substr(pronum.lastIndexOf('/')).indexOf('/lg.min.js') !== -1) {
				return pronum.split('?')[1];
				break
			}
		}
		return ''
	};
	yc.H = {};
	yc.H.ba = function() {
		var a = "";
		if (navigator.plugins && navigator.mimeTypes.length) {
			var b = navigator.plugins["Shockwave Flash"];
			b && b.description && (a = b.description.replace(/^.*\s+(\S+)\s+\S+$/, "$1"))
		} else if (window.ActiveXObject) try {
			if (b = new ActiveXObject("ShockwaveFlash.ShockwaveFlash"))(a = b.GetVariable("$version")) && (a = a.replace(/^.*\s+(\d+),(\d+).*$/, "$1.$2"))
		} catch (f) {}
		return a
	};
	yc.G = {};
	yc.G.log = function(a, b) {
		var f = new Image,
			d = "mini_tangram_log_" + Math.floor(2147483648 * Math.random()).toString(36);
		window[d] = f;
		f.onload = f.onerror = f.onabort = function() {
			f.onload = f.onerror = f.onabort = s;
			f = window[d] = s;
			b && b(a)
		};
		console.log(a);
		f.src = a
	};
	(function() {
		var d = yc.lang;
		var ev = {
			dz: 0,
			init: function() {
				ev.p = {
					push: function() {
						ev.D.apply(null, arguments)
					}
				};
				ev.V()
			},
			_trackEvent: function(a) {
				if (4 <= a.length && a[0].length > 0 && a[1].length > 0 && a[2].length > 0 && a[3].length > 0) {
					h.b.dz = '_trackEvent';
					h.b.iat = 0;
					h.b.lc = a[1];
					h.b.ty = a[2];
					h.b.ac = a[3];
					h.b.na = a.length > 4 ? a[4] : '';
					h.a.log()
				} else {
					console.log("数组长度为四到五，例['_trackEvent', '热图','图片','下载','小龙女'],参数不能为空字符串")
				}
			},
			_trackPageview: function(a) {
				if (3 == a.length && a[1].charAt && "/" == a[1].charAt(0) && a[2].length > 0) {
					h.b.dz = '_trackPageview';
					h.b.iat = 0;
					h.b.pn = a[2];
					h.b.ul = h.protocol + "//" + document.location.host + a[1];
					h.a.log()
				} else {
					console.log("数组长度为三，例['_trackPageview', '/simen','列表页-热门'],参数不能为空字符串")
				}
			},
			D: function(a) {
				if (d.d(a, "Array")) {
					var b = a[0];
					if (ev.hasOwnProperty(b) && d.d(ev[b], "Function")) {
						ev[b](a)
					}
				}
			},
			V: function() {
				var a = window._yct;
				if (a && a.length) {
					for (var b = 0; b < a.length; b++) {
						var d = a[b];
						switch (d[0]) {
						case "_setPageName":
							1 < d.length ? window._pageName = d[1] : "";
							break;
						case "_setAutoPageview":
							if (1 < d.length && (d = d[1], t === d || p === d)) {
								ev.dz = 2, window._autoPageview = d
							}
							break
						}
					}
					window._yct = ev.p
				} else {
					console.log('请在head中定义_yct对象')
				}
			}
		};
		ev.init()
	})();
	(function() {
		var mc = yc.cookie;
		var mu = yc.url;
		var mh = yc.H;
		var mg = yc.g;
		var ml = yc.G;
		var ms = yc.sessionStorage;

		function a() {
			this.init()
		}
		a.prototype = {
			init: function() {
				h.a = this
			},
			log: function() {
				var b = this;
				h.b.rnd = Math.round(Math.random() * h.q);
				b.sl();
				b.pb();
				var d = h.protocol + "//" + h.p + "?" + b.gl();
				ml.log(d, null);
				h.b = {}
			},
			sl: function() {
				if (h.b.dz == '_trackPageview') {
					var myDate = new Date();
					var si = ms.get('yc_usid');
					if (!si) {
						si = myDate.getTime() + '_' + 1
					} else {
						try {
							si = si.split('_')[0] + '_' + (parseInt(si.split('_')[1]) + 1)
						} catch (e) {
							si = myDate.getTime() + '_' + 1
						}
					}
					ms.set('yc_usid', si);
					h.b.us = si
				}
			},
			pb: function() {
				var url = h.b.dz == '_trackPageview' ? h.b.ul : '';
				var fsite = mc.get('fsite');
				var site = mc.get('site');
				var usite = mu.k(url, 'site');
				var pageNo = mu.k(url, 'pageNo') || "";
				var pno = mu.k(url, 'pno') || "";
				var iat = (h.b.iat == 0) ? document.location.href : document.referrer;
				var keyword = mu.k(url, 'keyword') || "";
				var key = mu.k(url, 'key') || "";
				h.b.pr = mg.pr();
				h.b.fs = site || fsite || usite || "";
				h.b.yu = mc.get('yctuid') || "";
				h.b.ud = mc.get('uid') || "";
				h.b.kw = decodeURIComponent(key || keyword || "");
				h.b.pno = pageNo || pno;
				h.b.ck = mg.cookieEnabled;
				h.b.cl = mg.colorDepth;
				h.b.ds = mg.ia;
				h.b.ep = 0;
				h.b.fl = mh.ba();
				h.b.ja = mg.javaEnabled;
				h.b.ln = mg.language;
				h.b.su = iat;
				h.b.v = '0.2'
			},
			gl: function() {
				for (var a = [], b = 0, d = h.Q.length; b < d; b++) {
					var e = h.Q[b],
						f = h.b[e];
					"undefined" != typeof f && "" !== f && null != f && a.push(e + "=" + encodeURIComponent(f))
				}
				return a.join("&")
			}
		};
		new a()
	})();
	(function() {
		var a = window._yct;
		sendLog = function() {
			if (window._pageName) {
				h.b.pn = window._pageName
			} else {
				if (window.document.title) {
					h.b.pn = window.document.title
				} else {
					h.b.pn = window._pageName = ''
				}
			}
			h.b.dz = '_trackPageview';
			h.b.iat = 1;
			h.b.ul = window.location.href;
			h.a.log()
		};
		if (a) {
			if (undefined == window._autoPageview || ('boolean' == typeof window._autoPageview && window._autoPageview == true)) {
				sendLog()
			}
		} else {
			sendLog()
		}
	})()
})();