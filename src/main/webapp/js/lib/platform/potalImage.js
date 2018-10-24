function potalImage(outerId, id, width, height) {
    var $ = this;
    $.id = id;
    $.ids = ('#' + $.id);
    $.width = width;
    $.height = height;
    $.mode = 'R';
    $.fileInfo = null;
    $.isChange = false;
    $.isZooming = false;
    
    var host = document.getElementById(outerId);
    
    var box = document.createElement('div');
    box.id = $.id;
    box.style.position = 'absolute';
    box.style.width = $.width + 'px';
    box.style.height = $.height + 'px';
    box.style.lineHeight = $.height + 'px';
    box.className = 'potalImage';

    var btnAdd = document.createElement('div');
    btnAdd.style.position = 'absolute';
    btnAdd.style.top = String(Number($.height / 2).toFixed(2) - 20) + 'px';
    btnAdd.style.left = String(Number($.width / 2).toFixed(2) - 20) + 'px';
    btnAdd.style.width = '40px';
    btnAdd.style.height = '40px';
    btnAdd.className = 'func_icon add_btn_icon';
    box.appendChild(btnAdd);
    
    var btnDel = document.createElement('div');
    btnDel.style.position = 'absolute';
    btnDel.style.top = '5px';
    btnDel.style.right = '6px';
    btnDel.style.width = '26px';
    btnDel.style.height = '26px';
    btnDel.className = 'func_icon del_btn_icon';
    box.appendChild(btnDel);
    
    /*
    var btnLtrn = document.createElement('div');
    btnLtrn.style.position = 'absolute';
    btnLtrn.style.top = '5px';
    btnLtrn.style.right = '56px';
    btnLtrn.style.width = '26px';
    btnLtrn.style.height = '26px';
    btnLtrn.className = 'func_icon lturn_btn_icon';
    box.appendChild(btnLtrn);
    
    var btnRtrn = document.createElement('div');
    btnRtrn.style.position = 'absolute';
    btnRtrn.style.top = '5px';
    btnRtrn.style.right = '30px';
    btnRtrn.style.width = '26px';
    btnRtrn.style.height = '26px';
    btnRtrn.className = 'func_icon rturn_btn_icon';
    box.appendChild(btnRtrn);
    */
    
    var e = document.createElement('img');
    e.style.width = $.width + 'px';
    e.style.height = $.height + 'px';

    box.appendChild(e);
    host.appendChild(box);
    
    //var sel = jQuery($.ids);
    //sel.find('img').css('cursor', 'pointer');
	
	// 모드 변경 (R:보기전용, N:이미지신규등록, U:변경또는삭제)
	$.setMode = function(mode) {
		$.mode = mode;
		var sel = jQuery($.ids);
		sel.find('.func_icon').removeClass('on');
		
		if (mode == 'R') {
			
		} else if (mode == 'N') {
			sel.find('.add_btn_icon').addClass('on');
		} else if (mode == 'U') {
			sel.find('.del_btn_icon').addClass('on');
		}
	};
	
	// 파일 브라우저를 통한 이미지 변경
	$.fileChange = function(event, files) {
		try {
			var file = files[0];
			$.fileInfo = file;
			
			// 썸네일 이미지 표시, 이미지를 업로드 하기전 미리보기
			var reader = new FileReader();
			reader.onload = function (e) {
				var img = new Image();
				
				img.onload = function() {
					// 이미지가 일정 픽셀 이상 큰 경우, 축소하여 삽입
					$.setValue($.initSize(img).toDataURL());
					
					// 수정 모드로 변경
					$.setMode('U');
				};
				
				img.src = e.target.result;
			};

			// 컨텐츠를 특정 Blob 이나 File에서 읽어 오는 역할
			reader.readAsDataURL(file);
		} catch (ex) {
			MessageBoxShow(('파일을 불러오는중 오류가 발생하였습니다.<br/><br/>' + ex.message + '<br/><br/>' + ex.stack), 500);
		}
	};
	
	$.fileRemove = function() {
		// 이미지 제거
		$.setValue('#empty');
		
		// 추가 모드로 변경
		$.setMode('N');
		
		// 변경 여부
		$.isChange = true;
		
		// 파일 정보 제거
		$.fileInfo = null;
	};
	
	$.getUploadParam = function() {
		var src = $.getValue();
		
		if (!src) return {};
		
		var obj = {};
		obj['image0'] = (src ? src.split(',')[1] : '');
		// 이미지 원본 파일명의 경우 확장자는 .png 로 변경한다. (캔버스에서 png 형태로 전환되기 때문)
		obj['image_name0'] = ($.fileInfo ? $.fileInfo.name.replace(/\.[^/.]+$/, ".png") : ('image_' + $.id + '.png'));
		
		delete src;
		return obj;
	};

    // 이미지 클릭
	$.click = function() {
		var param = {'img_src': $.getValue()};
		if ($.mode == 'N' || !param['img_src']) {
			return;
		}
		
		if ($.isZooming) {
			var size = $.getRealSize();
			size[0] = (size[0] > 1200 ? 1200 : (size[0] + 3));  // w
			size[1] = (size[1] > 620 ? 620 : (size[1] + 37));   // h
			
			jQuery('#expandImageView').modal('show');
			jQuery('#expandImageView').find('img').attr('src', param.img_src);
		}
	};
	
	$.bindEvents = function() {
		/*
			크로스브라우져 이벤트 등록
			IE에서 사용하는 attachEvent에는 on까지 붙여줘야한다는 것을 주의하고, 
			함수가 존재하면 addEventListener부터 순서대로 적용하고, 
			마지막에는 최후의 보루로 직접 속성을 설정하는 방법을 택하는 함수다. 
			여기서는 이미지 클릭시 발생하는 이벤트 핸들러 추가
		*/

		if (e.addEventListener) {
			e.addEventListener("click", $.click, false);
		} else if (e.attachEvent) {
			e.attachEvent("onclick", $.click);
		}
		
		var sel = jQuery($.ids);
		
		// 이벤트 타입 인자 뒤에 JSON 형태로 파라미터를 추가해하면  지역변수 값을 전달 받을 수 있다. potalFileBrowser에 browse 함수에 파라미터를 넘긴다.
		sel.find('.add_btn_icon').on('click', {filter: 'Image', obj: $}, g_fileBrowser.browse);
		sel.find('.del_btn_icon').on('click', $.fileRemove);
	};
	
	// 이벤트 바인딩
	$.bindEvents();	
	
    e = null;
}

potalImage.prototype.initSize = function(original) {
	var canvas = document.createElement("canvas");
	
	// 축소 스케일 계산. (가로폭 800 이하면 축소하지않음)
	var scale = (original.width > 800) ? Number(800 / original.width).toFixed(2) : 1;

	canvas.width = original.width * scale;
	canvas.height = original.height * scale;

	canvas.getContext("2d").drawImage(original, 0, 0, canvas.width, canvas.height);
	
	return canvas;
}

potalImage.prototype.setSize = function(width, height) {
	var sel = jQuery(this.ids);
	var img = sel.find('img');
	
	img.css('width', width);
	img.css('height', height);
}

potalImage.prototype.getRealSize = function() {
	var sel = jQuery(this.ids);
	var image = new Image(); 
	image.src = this.getValue();
	
	return [image.width, image.height];
}

potalImage.prototype.setBorder = function(value) {  
	var sel = jQuery(this.ids);
	
	if (value) {
		sel.find('img').css('border', '1px solid #cacece');
	} else {
		sel.find('img').css('border', 'none');
    }
}

potalImage.prototype.setScroll = function(x, y) {
    var e = document.getElementById(this.id);
    e.style['overflow-x'] = (x === undefined ? 'hidden' : x);
    e.style['overflow-y'] = (y === undefined ? 'hidden' : y);
    
    delete e;
}

potalImage.prototype.setZooming = function(value) {  
	var sel = jQuery(this.ids);
	this.isZooming = value;
	
    if (value) {
    	this.setPointer(true);
    }
}

potalImage.prototype.setPointer = function(value) {  
	var sel = jQuery(this.ids);

    if (value) {
    	sel.find('img').css('cursor', 'pointer');
    }
    else {
    	sel.find('img').css('cursor', 'default');
    }
}

potalImage.prototype.getValue = function() {
	var sel = jQuery(this.ids);
	return sel.find('img').attr('src');
}

potalImage.prototype.setValue = function(url, isButton) {
	try {
	    var sel = jQuery(this.ids);
	    
	    if (url === undefined || url === null) {
	    	return;
	    }
	    if (url == '#empty') {
	    	sel.find('img').attr('src', 'img/empty_img.png');
	    } else if (typeof url === 'object' && url.length) {
	    	sel.find('img').attr('src', convertByteArrayToImageSource(url));
	    } else {
	    	sel.find('img').attr('src', url);
	    }
	    
	    if (isButton) {
	    	this.setButton(true);
	    }
	    if (sel.find('img').attr('src') && this.mode == 'N') {
	    	this.setMode('U');
	    }
	} catch (err) {
		console.log('potalImage setValue() : ' + err.message);
	}
}

potalImage.prototype.setDisplay = function(visible) {
    var e = document.getElementById(this.id);

    if (visible == true)
        e.style.display = 'inline-block';
    else
        e.style.display = 'none';

    delete e;
}

potalImage.prototype.setVisible = function(visible) {
    var e = document.getElementById(this.id);

    if (visible == true)
        e.style.visibility = 'visible';
    else
        e.style.visibility = 'hidden';

    delete e;
}