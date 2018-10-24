var PotalCommonJS = (function(window, document, undefined) {
    var ROOT_URL = 'http://127.0.0.1:80/test/';

    function PotalCommonJS() {
        //this.i = 0;
    }
    
    function addZero(i) {
        var rtn = i + 100;

        return rtn.toString().substring(1, 3);
    }
    
    PotalCommonJS.prototype.appendRequestParams = function(param){
		var result = {};
		var list = window.location.search.substring(1).split("&");
		
		for(var p in param){
			if(param.hasOwnProperty(p)) result[p] = param[p];
		}
		
		for(var ii = 0; ii < list.length; ii++){
			var row = list[ii].split("=");
			if(!param.hasOwnProperty(list[ii][0])){
				result[row[0]] = decodeURI(row[1]);
			}
		}
		return result;
	};
	
    function switchIndicator(isOn) {
    	if (isOn) {
    		var loading = '<div id="ajax_indicator" style="display:none; z-index:99999;">'
				+ 	'<p><img src="../img/ajax-loader.gif" /></p>'
				+ '</div>';
        	
    		
    		
    		
        	var isFullWidth = (document.body.clientWidth > 767 ? true : false);
        	if ($('body').find('#ajax_indicator').length == 0) {
        		$(loading).appendTo('body');
        		
        		var $loading = $('#ajax_indicator');
        		$("#ajax_indicator").css({
        			position:"fixed",
        			left: (($(window).width() - $loading.outerWidth()) / 2),
        			top: (($(window).height() - $loading.outerHeight()) / 2)
        		});
        	}
        	
        	var $loading = $('#ajax_indicator');
        	$("#ajax_indicator").css({
        		left: (($(window).width() - $loading.outerWidth()) / 2),
    			top: (($(window).height() - $loading.outerHeight()) / 2)
        	});
        	
        	$('#ajax_indicator').show().fadeIn('fast');
        	
    	} else {
    		$('#ajax_indicator').fadeOut();
    	}
    }
    
    /**
     * [getHttpParams AngularJS용 Ajax Call을 위한 파라메터 생성]
     * @param  {[String]} actionUrl
     * @param  {[Object]} param
     * @param  {[String]} method
     * @return {[Object]}
     */
    PotalCommonJS.prototype.getHttpParams = function (callUrl, param, method) {
        var _method = method || 'GET';

        return {
            'url': ROOT_URL + callUrl,
            'param': param,
            'method': _method
        };
    };

    PotalCommonJS.prototype.callBasicAjax = function (sqlId, workType, param, callback, method) {
    	param.ac = sqlId;
    	param.workType = workType;
    	
    	this.callAjax("SWNAjaxL.do", param, callback, method, true);
    };
    
    PotalCommonJS.prototype.removeCompId = function(param){
        if(param.hasOwnProperty("compId")){
        	var new_param = {};
        	console.log("경고: 사용되지 않는 속성 CompID가 제공되어 이를 자동으로 제거했습니다.");
        	for(var item in param){
        		if(param.hasOwnProperty(item) && item != "compId"){
            		new_param[item] = param[item];
        		}
        	}
        	return new_param;
        }else{
        	return param;
        }
    };
    
    /**
     * [callAjax Ajax Call]
     * @param  {[type]}   callUrl
     * @param  {[type]}   param
     * @param  {Function} callback
     * @param  {[type]}   method
     * @return {[type]}
     */
    PotalCommonJS.prototype.callAjax = function (callUrl, param, callback, method, indicator) {
        var _method = method || 'POST';
        
        param = this.removeCompId(param);
        $.ajax({
            accepts: {
                json: 'application/json; charset=UTF-8'
            },        	
            type : _method,
            url : callUrl,
            data : param,
            dataType : "json",
            
            success: function(data) {
                callback.call(this, data);
            },
            
            error: function(xhr, exception) {
            	console.log('status :' + xhr.status);
            	console.log('statusText :' + xhr.statusText);
            },
            
            beforeSend: function() {
            	if (indicator) {
            		if(window.ubsalesApp){
            			window.ubsalesApp.showProgress(true);
            		}else{
            			switchIndicator(true);
            		}
            	}
            },
            
            complete: function() {
            	if (indicator) {
            		if(window.ubsalesApp){
            			window.ubsalesApp.showProgress(false);
            		}else{
            			switchIndicator(false);
            		}
            	}
            }
        });
    };
    
    /**
     * [callAjaxJsonp Ajax Call With JSONP]
     * (유의사항 2가지)
     * 1) get요청으로 처리됨 (* type:'POST'로 해놔도 내부적으로 get요청을 사용)
     * 2) 동기식 지원안함 (* async:false 하지말길)
     */
    PotalCommonJS.prototype.callAjaxJsonp = function (callUrl, param, callback, indicator) {
        var retData = null;

        $.ajax({
            accepts: {
                json: 'application/json; charset=UTF-8'
            },        	
            url: callUrl,
    	    data: param,
    	    dataType: 'jsonp',
    	    jsonp : "callback",

            success: function(data) {
                callback.call(this, data);
            },
            
            error: function(xhr, exception) {
            	console.log('status :' + xhr.status);
            	console.log('statusText :' + xhr.statusText);
            },
            
            beforeSend: function() {
            	if (indicator) {
            		if(window.ubsalesApp){
            			window.ubsalesApp.showProgress(true);
            		}else{
            			switchIndicator(true);
            		}
            		
            	}
            },
            
            complete: function() {
            	if (indicator) {
            		if(window.ubsalesApp){
            			window.ubsalesApp.showProgress(false);
            		}else{
            			switchIndicator(false);
            		}
            	}
            }
        });

        return retData;
    };        

    /**
     * [callSyncAjax 동기식 Ajax Call]
     * @param  {[String]} callUrl
     * @param  {[Object]} param
     * @param  {[String]} method
     * @return {[JSON]}
     */
    PotalCommonJS.prototype.callSyncAjax = function (callUrl, param, method, indicator) {
        var _method = method || 'POST';
        
        var retData = null;
        param = this.removeCompId(param);
        
        $.ajaxSetup({ async: false });
        $.ajax({
            accepts: {
                json: 'application/json; charset=UTF-8'
            },        	
            type : _method,
            url : callUrl,
            data : param,
            dataType : "json",        

            success: function(data) {
            	retData = data;
            },

            error: function(xhr, exception) {
            	console.log('status :' + xhr.status);
            	console.log('statusText :' + xhr.statusText);
            },
            
            beforeSend: function() {
            	/*if (indicator) {
            		if(window.ubsalesApp){
            			window.ubsalesApp.showProgress(true);
            		}else{
            			switchIndicator(true);
            		}
            	}*/
            },
            
            complete: function() {
            	/*if (indicator) {
            		if(window.ubsalesApp){
            			window.ubsalesApp.showProgress(false);
            		}else{
            			switchIndicator(false);
            		}
            	}*/
            }    
        });
        $.ajaxSetup({ async: true });

        return retData;
    };
    
    /**
     * [callSyncAjax 파일 업로드 (multipart/form-data)]
     * @param
     * var param = new FormData();
     * param.append("file1", $("#uploadImage1").prop("files")[0]);
     */
    PotalCommonJS.prototype.callFileUpload = function (callUrl, param) {
        var retData = null;

        $.ajaxSetup({ async: false });
    	$.ajax({
            accepts: {
                json: 'application/json; charset=UTF-8'
            },    		
    	    url: callUrl,
    	    data: param,
    	    cache: false,
    	    contentType: false,
    	    processData: false,
    	    dataType: 'text', /* dataType은 success에서 받는 resultData타입 */
    	    type: 'POST',
    	    beforeSend: function(xhr) {
    	        xhr.setRequestHeader("AJAX", true);
	    	},
    	    success: function (data) {
    	    	retData = JSON.parse(data);
    	    },
            error: function(xhr, exception) {
            	console.log('status :' + xhr.status);
            	console.log('statusText :' + xhr.statusText);
            }
    	});
        $.ajaxSetup({ async: true });

        return retData;
    };

    /**
     * [commify 3자리 콤마 mask]
     * @param  {[String or Number]} n
     * @return {[String]}
     */
    PotalCommonJS.prototype.commify = function (n) {
        var reg = /(^[+-]?\d+)(\d{3})/;
        n += ''; // 숫자인 경우 문자열로 변환

        while (reg.test(n))
            n = n.replace(reg, '$1' + ',' + '$2');

        return n;
    };

    /**
     * [uncommify 3자리 콤마 unmask]
     * @param  {[String]} s
     * @return {[String]}
     */
    PotalCommonJS.prototype.uncommify = function (s) {
        return String(s).replace(/,/gi, "");
    };
    
    /**
     * [unmask 숫자 이외 포맷 제거]
     * @param  {[String]} s
     * @return {[String]}
     */
    PotalCommonJS.prototype.unmask = function (s) {
        return String(s).replace(/[^\d]+/g, "");
    };
    
    /**
     * [trim 좌우 공백 제거]
     * @param  {[String]} s
     * @return {[String]}
     */
    PotalCommonJS.prototype.trim = function (s) {
        return String(s).replace(/(^\s*)|(\s*$)/gi, "");
    };

    /**
     * [getRootUrl Root URL 취득]
     * @return {[String]}
     */
    PotalCommonJS.prototype.getRootUrl = function () {
        return ROOT_URL;
    };
    
    /**
     * [get 전송]
     * @param url
     * @param params
     */
    PotalCommonJS.prototype.get = function (url, params) {
        var p = url;
        if (params) {
        	p += '?';
        	var first = true;
            for(var x in params) {
            	if (!first) {
            		p += '&';
            	} else {
            		first = false;	
            	}
            	p += (x + '=' + encodeURI(params[x]));
            }
        }
        location.href = p;
    };    
    
    /**
     * [post 히든 폼 생성후 POST로 전송]
     * @param url
     * @param params
     */
    PotalCommonJS.prototype.post = function (url, params) {
        var temp = document.createElement("form");
        temp.action = url;
        temp.method = "POST";
        //temp.enctype = "multipart/form-data";
        temp.style.display="none";
        for(var x in params) {
            var opt = document.createElement("textarea");
            opt.name = x;
            opt.value = params[x];
            temp.appendChild(opt);
        }
        document.body.appendChild(temp);
        temp.submit();
        return temp;
    };
    
    /**
     * [post 히든 폼 생성후 새 창으로 전송]
     * @param url
     * @param params
     */
    PotalCommonJS.prototype.windowOpen = function (url, params, target) {
        var temp = document.createElement("form");
        temp.action = url;
        temp.method = "POST";
        temp.target = target || "TheWindow";
        temp.style.display="none";
        
        for(var x in params) {
            var opt = document.createElement("textarea");
            opt.name = x;
            opt.value = params[x];
            temp.appendChild(opt);
        }
        document.body.appendChild(temp);
        window.open('', target || "TheWindow");
        temp.submit();
        return temp;
    }; 
    
    /**
     * [파일다운로드]
     * @param url
     * @param params
     */
    PotalCommonJS.prototype.download = function (url, params) {
        var p = url;
        if (params) {
        	p += '?';
        	var first = true;
            for(var x in params) {
            	if (!first) {
            		p += '&';
            	} else {
            		first = false;	
            	}
            	p += (x + '=' + params[x]);
            }
        }        
        //window.location = p;
        window.open(p);
    };    

    /**
     * [파일다운로드시 iframe을 통해 다운로드 (* 새 창 오픈안함)]
     * @param url
     * @param params
     */
    PotalCommonJS.prototype.downloadLink = function (url, params) {
        var p = url;
        if (params) {
        	p += '?';
        	var first = true;
            for(var x in params) {
            	if (!first) {
            		p += '&';
            	} else {
            		first = false;	
            	}
            	p += (x + '=' + params[x]);
            }
        }
        $("[name=download_hidden_frame]").attr("src", url);
        var popup = window.open(p, "download_hidden_frame", "width=0, height=0, top=0, statusbar=no, scrollbars=no, toolbar=no");
        popup.focus();
    };    

    /**
     * [PopUp 오픈]
     * @param  {[String]} PopUp Element Selector
     */
    function popOn(e) {
    	e.preventDefault();
    }
    PotalCommonJS.prototype.isPopOpen = false;
    PotalCommonJS.prototype.popOpen = function (selector, opa) {
    	//$(selector).bPopup();
    	
    	util.isPopOpen = true;
    	$(selector).bPopup({zIndex: 1000,
			follow:[false, false],
			modalClose: false,
			opacity: (opa == undefined ? 0.6 : opa),
    		scrollBar : true,
			positionStyle: 'fixed' //'fixed' or 'absolute'
    	});    	
//    	$('body').css("overflow", "hidden");
//    	document.body.addEventListener('touchmove', popOn, false);
    };
    
    /**
     * [PopUp 클로즈]
     * @param  {[String]} PopUp Element Selector
     */
    PotalCommonJS.prototype.popClose = function (selector) {
    	$(selector).bPopup().close();
//    	$('body').css("overflow", "auto");
//    	document.body.removeEventListener('touchmove', popOn, false);
    };

    /**
     * [메시지 보여주기]
     * @param  {[String]} 메시지
     */
    PotalCommonJS.prototype.showMsg = function (msg, callback, title) {
    	$("#comm_msg_box_alert_yes_btn").unbind("click");
    	
    	if(title == undefined){
    		$('#comm_msg_box_alert_title').html("확인 메시지");
    	}else{
    		$('#comm_msg_box_alert_title').html(title);
    	}
    	
    	$('#comm_msg_box_alert_content').html(msg);
    	$('#comm_msg_box_alert_yes_btn').click(function() {
    		callback ? callback.call(this) : null;
    		util.popClose('#comm_msg_box_alert');
    	});
    	this.popOpen('#comm_msg_box_alert');
    };
    
    /**
     * [컨펌 받기]
     * @param  {[String]} 메시지
     */
    PotalCommonJS.prototype.showConfirm = function (msg, callback) {
    	//var result = confirm(msg);
    	$("#comm_msg_box_confirm_yes_btn").unbind("click");
    	$("#comm_msg_box_confirm_no_btn").unbind("click");
    	
    	$('#comm_msg_box_confirm_content').html(msg);
    	$('#comm_msg_box_confirm_yes_btn').click(function() {
    		callback.call(this, true);
    		util.popClose('#comm_msg_box_confirm');
    	});
    	$('#comm_msg_box_confirm_no_btn').click(function() {
    		callback.call(this, false);
    		util.popClose('#comm_msg_box_confirm');
    	});
    	
    	this.popOpen('#comm_msg_box_confirm');
    };
    
    /**
     * [Key/Value 형태 객체 Value 클리어]
     * @param  {[Object]} 
     */
    PotalCommonJS.prototype.mapValueClear = function (obj) {
    	for (var key in obj) {
    		obj[key] = "";
    	}
    };
    
    /**
     * [리스트객체를 서버 String[] 전송 포맷으로 변환]
     * @param  {[Array]} 
     */
    PotalCommonJS.prototype.convArrFormat = function (arr) {
    	if (!arr || arr.length == 0)
    		return null;
    	
    	var result = {};
    	for (var key in arr[0]) {
    		result[key] = [];
    	}
    	
    	for (var i=0, len=arr.length; i < len; i++) {
        	for (var k in arr[i]) {
        		if (k in result) {
        			result[k].push(arr[i][k] || "");
        		}
        	}
		}
    	
    	return result;
    };
    
    /**
     * [Array<String> => String(구분문자)]
     * @param  {[Array]} 
     */
    PotalCommonJS.prototype.arrToStr = function (arr, sChar) {
    	if (!arr || arr.length == 0)
    		return "";
    	
    	var result = "";
    	sChar = sChar || "|"; // 기본 구분 파이프라인('|')
    	for (var i=0, len=arr.length; i < len; i++) {
			if (i == 0)
				result += (arr[i] || "");
			else
				result += (sChar + (arr[i] || ""));
		}
    	
    	return result;
    };
    
    /**
     * [Array<Object> => String(구분문자)]
     * @param  {[Array]} 
     */
    PotalCommonJS.prototype.arrToStr2 = function (arr, key, sChar) {
    	if (!arr || arr.length == 0)
    		return "";
    	
    	var result = "";
    	sChar = sChar || "|"; // 기본 구분 파이프라인('|')
    	for (var i=0, len=arr.length; i < len; i++) {
			if (i == 0)
				result += (arr[i][key] || "");
			else
				result += (sChar + (arr[i][key] || ""));
		}
    	
    	return result;
    };
    
    /**
     * [Array<Object> => Object<String(구분문자)>]
     * @param  {[Array]}
     */
    PotalCommonJS.prototype.arrToObj = function (arr, sChar) {
    	if (!arr || arr.length == 0)
    		return null;
    	
    	var result = {};
    	for (var key in arr[0]) {
    		result[key] = "";
    	}
    	
    	sChar = sChar || "|"; // 기본 구분 파이프라인('|') 
    	for (var i=0, len=arr.length; i < len; i++) {
        	for (var k in arr[i]) {
        		if (k in result) {
        			if (i == 0)
        				result[k] += (arr[i][k] || "");
        			else
        				result[k] += (sChar + (arr[i][k] || ""));
        		}
        	}
		}
    	
    	return result;
    };

    /**
     * [toJSON]
     * @param  {[JSON Valid Object]}
     */
    PotalCommonJS.prototype.toJSON = function (obj) {
    	//return "JSON=" + JSON.stringify(obj);
    	return "JSON=" + encodeURIComponent(JSON.stringify(obj));
    };

    /**
     * [Get Today]
     * @param  {[String]} Separator ('-','/','.')
     */
    PotalCommonJS.prototype.getToday = function (sep) {    	
    	var dateData = util.callSyncAjax('SWNAjaxL.do', {'ac': 'SY0103', 'workType' : 'DATE', 'seperate': sep == undefined ? '' : sep});
    	
    	var today = dateData.resultList[0].today;
    	
    	return today;    	
    };

    /**
     * [Get Time]
     * @param  {[String]} Separator ('-','/','.')
     */
    PotalCommonJS.prototype.getTime = function (sep) {
    	var dateData = util.callSyncAjax('SWNAjaxL.do', {'ac': 'SY0103', 'workType' : 'TIME'});
    	var ts = dateData.resultList[0].time.split(' ');
    	var time = new Date(ts[0], ts[1] - 1, ts[2], ts[3], ts[4]);
    	
    	return time;
    };
    
    /**
     * ['yyyymmdd' => date]
     */
    PotalCommonJS.prototype.dtParser = function (str) {
        if(!/^(\d){8}$/.test(str)) return null;
        var y = str.substr(0,4),
            m = str.substr(4,2),
            d = str.substr(6,2);
        return new Date(y,(m-1),d);
    };
    
    /**
     * [Date Converter]
     * @param  {[String]} Separator ('-','/','.')
     */
    PotalCommonJS.prototype.dtConverter = function (dt, sep) {
    	if (!dt)
    		return;

    	return dt.getFullYear() + sep + addZero(dt.getMonth() + 1) + sep + addZero(dt.getDate());    	
    };
    
    /**
     * [Date(문자열) Formatter]
     */
    PotalCommonJS.prototype.dtFormatter = function (date, format) {
    	if (!date)
    		return;
    	
    	var r = date.replace(/-/gi, "");
    	switch (format) {
    		case 'yyyy.MM.dd':
    			r = r.substr(0, 4) + '.' + r.substr(4, 2) + '.' + r.substr(6, 2);
    			break;
    		case 'yyyy-MM-dd':
    			r = r.substr(0, 4) + '-' + r.substr(4, 2) + '-' + r.substr(6, 2);
    			break;
    		case 'yyyy.MM':
    			r = r.substr(0, 4) + '.' + r.substr(4, 2);
    			break;
    	}
    	return r;
    };
    
    /**
     * [날짜 연산]
     * @exam util.getCalcDate('20130310', -7)
     * @exam util.getCalcDate('20130310', 30)
     */
    PotalCommonJS.prototype.getCalcDate = function (yyyymmdd, calcDay) {
        if (!yyyymmdd || yyyymmdd.length != 8)
            return;

        var dt = new Date(yyyymmdd.substr(0, 4), Number(yyyymmdd.substr(4, 2)) - 1, yyyymmdd.substr(6, 2));

        dt.setDate(dt.getDate() + calcDay);

        return dt.getFullYear() + addZero(eval(dt.getMonth() + 1)) + addZero(dt.getDate());
    };

    /**
     * [월 연산]
     * @exam util.getCalcMonth('20130310', -2)
     * @exam util.getCalcMonth('20130310', 2)
     */
    PotalCommonJS.prototype.getCalcMonth = function (yyyymmdd, calcMon) {
        if (!yyyymmdd || yyyymmdd.length != 8)
            return;

        var dt = new Date(yyyymmdd.substr(0, 4), Number(yyyymmdd.substr(4, 2)) - 1, yyyymmdd.substr(6, 2));
        var year = dt.getFullYear();
        var month = dt.getMonth();

        month = month + calcMon;

        if (month <= 0)
        {
            year = year - 1;        
            month = month + 12;
        }

        dt.setYear(year);
        dt.setMonth(month);

        return dt.getFullYear() + addZero(eval(dt.getMonth() + 1)) + addZero(dt.getDate());
    };
    
    /**
     * [Object 카피]
     */
    PotalCommonJS.prototype.copyObj = function (org) {
        var clone = {};
        for (var i in org) {
            clone[i] = (typeof org[i] == 'object') ? arguments.callee(org[i]) : org[i];
        }
        return clone;
    };
    
    /**
     * [Array 카피]
     * @param org 원본 Array
     * @returns {Array} 복제된 Array
     */
    PotalCommonJS.prototype.copyArray = function (org) {
        var clone = [];
        for (var i = 0; i < org.length; i++) {
            clone.push(cloneObj(org[i]));
        }
        return clone;
        
        function cloneObj(org) {
            var clone = {};
            for (var i in org) {
                clone[i] = (typeof org[i] == 'object') ? arguments.callee(org[i]) : org[i];
            }
            return clone;
        }
    };
    
    /**
     * [Element 감시기]
     */
    PotalCommonJS.prototype.watch = function(element, property, callback) {
	   return $(element).each(function() {
	       var self = this;
	       var old_property_val = this[property];
	       var timer;

	       function watch() {
	          if($(self).data(property + '-watch-abort') === true) {
	             timer = clearInterval(timer);
	             $(self).data(property + '-watch-abort', null);
	             return;
	          }

	          if(self[property] != old_property_val) {
	             old_property_val = self[property];
	             callback.call(self);
	          }
	       }
	       timer = setInterval(watch, 500);
	   });
	};

    /**
     * [Element 감시기 해제]
     */
	PotalCommonJS.prototype.unwatch = function(element, property) {
	   return $(element).each(function() {
	       $(element).data(property + '-watch-abort', true);
	   });
	};
	
    /**
     * [Indicator On/Off]
     */
	PotalCommonJS.prototype.showIndicator = function(isOn) {
		if (isOn) {
			if(window.ubsalesApp){
    			window.ubsalesApp.showProgress(true);
    		}else{
    			switchIndicator(true);
    		}
		} else {
			if(window.ubsalesApp){
    			window.ubsalesApp.showProgress(false);
    		}else{
    			switchIndicator(false);
    		}
		}
	};
	
	/**
     * [페이징 뷰 생성]
     */
	PotalCommonJS.prototype.paging = {
		/* 페이지버튼(1,2,3,4,5) 표시 개수 */ 
		pageViewCnt: ((document.documentElement.clientWidth / 100).toFixed(0) > 10) ? 10 : (document.documentElement.clientWidth / 100).toFixed(0),
		pageLastNum: 0, /* 마지막 페이지 수 */
		currpageNum: 1, /* 현재 페이지 번호 */
		callFunc: null, /* 페이징 실행 연동 함수 */
		isInit: false,
		
		init: function() {
			$('.pagination > *').remove();
			$("<span class='pgbtn'>" +
					"<a onclick='util.paging.prevFirstPages();' class='prev_first'></a>" +
					"<a onclick='util.paging.prevPages();' class='prev_page'></a>" +
			  "</span>" +
			  "<span class='numPD'>" +
			  "</span>" +
			  "<span class='pgbtn'>" +
			  		"<a onclick='util.paging.nextPages();' class='next_page'></a>" +
			  		"<a onclick='util.paging.nextLastPages();' class='next_last'></a>" +
			  "</span>"
			 )
			 .appendTo('.pagination');
			
			$(window).resize(function() {
				util.paging.pageViewCnt = ((document.documentElement.clientWidth / 100).toFixed(0) > 10) ? 10 : (document.documentElement.clientWidth / 100).toFixed(0);
				util.paging.setPaging(util.paging.currpageNum);
			});
		},
		
		setView: function(currPage, LastPage, callback) {
			if (!this.isInit) {
				this.isInit = true;
				this.init();
			}
			this.currpageNum = currPage;
			this.pageLastNum = LastPage;
			this.setPaging(currPage);
			this.callFunc = callback;
		},
		
		setPaging: function(startPage) {
			$('.numPD > a').remove();
			for(var i = 0; i < this.pageViewCnt; i++) {
				if (startPage > this.pageLastNum) {
					continue;
				}
				$("<a class=" + (i == 0 ? 'on_menu' : '') + ">" + (startPage++) + "</a>")
				.click(function () {
					$('.numPD > .on_menu').removeClass('on_menu');
					$(this).addClass('on_menu');
					var pageNum = Number($(this).text());
					util.paging.callFunc(pageNum);
					util.paging.currpageNum = pageNum;
				})
				.appendTo('.numPD');
			}
		},
		
		prevFirstPages: function() {
			var fPage = Number($('.numPD > a:first').text());
			if (1 == fPage) {
				return;
			}
			this.setPaging(1);
			this.callFunc(1);
			this.currpageNum = 1;	
		},
		
		prevPages: function() {
			var fPage = Number($('.numPD > a:first').text());
			var pPage = fPage - this.pageViewCnt;
			if (1 == fPage) {
				return;
			}
			pPage = (pPage < 1) ? 1 : pPage;
			this.setPaging(pPage);
			this.callFunc(pPage);
			this.currpageNum = pPage;			
		},
	
		nextPages: function() {
			var lPage = Number($('.numPD > a:last').text());
			var nPage = lPage + 1;
			if (this.pageLastNum <= lPage) {
				return;
			}
			this.setPaging(nPage);
			this.callFunc(nPage);
			this.currpageNum = nPage;
		},
		
		nextLastPages: function() {
			var lPage = Number($('.numPD > a:last').text());
			var nPage = (this.pageLastNum - this.pageViewCnt) + 1;
			if (this.pageLastNum <= lPage) {
				return;
			}
			this.setPaging(nPage);
			this.callFunc(nPage);
			this.currpageNum = nPage;			
		}
	};
	
	/**
     * [문자 치환]
     */
	PotalCommonJS.prototype.replace = function(/*String*/originStr, /*String*/oldStr, /*String*/newStr){
		// 정규식 예약어 예외 처리
		oldStr = oldStr
		.replace(/\[/g, "\\[")
		.replace(/\+/g, "\\+")
		.replace(/\*/g, "\\*")
		.replace(/\^/g, "\\^")
		.replace(/\|/g, "\\|")			
		.replace(/\?/g, "\\?")
		.replace(/\./g, "\\.")
		.replace(/\(/g, "\\(");
		
		return originStr.replace(new RegExp(oldStr,"g"), newStr);
	};
	
	PotalCommonJS.prototype.convertCho = function(str) {
		var cho = ["ㄱ","ㄲ","ㄴ","ㄷ","ㄸ","ㄹ","ㅁ","ㅂ","ㅃ","ㅅ","ㅆ","ㅇ","ㅈ","ㅉ","ㅊ","ㅋ","ㅌ","ㅍ","ㅎ"];
		var rStr = "";
		var cd = null;
		for (var j = 0; j < str.length; j++) {
			cd = str.charCodeAt(j) - 44032;
			if (cd > -1 && cd < 11172) rStr += cho[Math.floor(cd / 588)]; 
		}
		return rStr;
	};
	
	PotalCommonJS.prototype.convertChoString = function (src){
		if(src == undefined)
			return '';
		else{
			return src
				.replace(/ㄱ/gi, "[가-깋]")
				.replace(/ㄴ/gi, "[나-닣]")
				.replace(/ㄷ/gi, "[다-딯]")
				.replace(/ㄹ/gi, "[라-맇]")
				.replace(/ㅁ/gi, "[마-밓]")
				.replace(/ㅂ/gi, "[바-빟]")
				.replace(/ㅅ/gi, "[사-싷]")
				.replace(/ㅇ/gi, "[아-잏]")
				.replace(/ㅈ/gi, "[자-짛]")
				.replace(/ㅊ/gi, "[차-칳]")
				.replace(/ㅋ/gi, "[카-킿]")
				.replace(/ㅌ/gi, "[타-팋]")
				.replace(/ㅍ/gi, "[파-핗]")
				.replace(/ㅎ/gi, "[하-힣]")
				.replace(/ㅃ/gi, "[빠-삫]")
				.replace(/ㅉ/gi, "[짜-찧]")
				.replace(/ㄸ/gi, "[따-띻]")
				.replace(/ㄲ/gi, "[까-낗]")
				.replace(/ㅆ/gi, "[싸-앃]");
		}
	};
	
    return PotalCommonJS;

})(window, document);

var util = new PotalCommonJS();
