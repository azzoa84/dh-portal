function FingerFileBrowser() {
	var $ = this;
	$.target = null;
	$.reader, $.readerType;
	
	var ranstr = Math.random().toString(36).replace(/[^a-z]+/g, '');
	$.id = ('hdn_file_browse_' + ranstr);
	$.ids = ('#' + $.id);
	
    var fb = document.createElement('input');
    fb.id = $.id;
    fb.type = 'file';
    fb.style.width = '0px';
    fb.style.height = '0px';
    fb.style.opacity = 0;
    fb.style.display = 'none';
    
    document.body.appendChild(fb);
	
	$.browse = function(event) {
		try {
			var el = document.getElementById($.id);
			/*
				audio/* : 사운드 파일을 허용함을 나타냅니다.
				video/* : 비디오 파일을 허용함을 나타냅니다.
				image/* : 이미지 파일을 허용함을 나타냅니다.
			*/
			el.setAttribute('accept', '*');
			
			if (event && event.data) {
				if (event.data.filter && event.data.filter == 'Image') {
					el.setAttribute('accept', 'image/*');
				}
				
				if (event.data.obj) {
					$.target = event.data.obj;
				} else {
					$.target = null;
				}
			}

			// 새로운 이벤트 생성, click 이벤트를 발생시키기 위한 트리거
			var evt = document.createEvent("MouseEvents");
			// 객체 생성후에는 반드시 초기화 해주어야 한다
			evt.initEvent("click", true, false);
			el.dispatchEvent(evt);
		} catch (err) {
			console.log('FingerFileBrowser browse() : ' + err.message);
		}
	};
	
	$.change = function(event) {
		try {
			var files = jQuery(event.currentTarget).prop('files');
			if (!files || !files.length) {
				return;
			}
			
			if ($.target && $.target.fileChange) {
				$.target.fileChange(event, files);
			}
			
			// 동일한 파일로 변경하는 경우에도 change 이벤트가 발생하도록 값 비움 처리
			jQuery($.ids).val("");
		} catch (err) {
			console.log('FingerFileBrowser change() : ' + err.message);
		}
	};
	
	$.bindEvents = function() {
		var el = document.getElementById($.id);
		
		jQuery(el).on('change', $.change);
	};
	
	$.download = function (fileUrl, fileOrgName) {
		try {
			var params = {};
			params['file_path'] = fileUrl;
			params['file_name'] = fileOrgName || '';
			
			var p = (g_ContextPath + "/fileDownload.do");
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
			
		} catch (err) {
			console.log('FingerFileBrowser download() : ' + err.message);
		}
	};

	// 이벤트 바인딩
	$.bindEvents();	
}