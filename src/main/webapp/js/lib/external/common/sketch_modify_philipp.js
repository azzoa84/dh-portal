var __slice = Array.prototype.slice;
(function($) {
	var Sketch;

	$.fn.sketch = function() {
		var args, key, sketch;
		key = arguments[0], args = 2 <= arguments.length ? __slice.call(arguments, 1) : [];
		if (this.length > 1) {
			$.error('Sketch.js can only be called on one element at a time.');
		}
		sketch = this.data('sketch');
		if (typeof key === 'string' && sketch) {
			if (sketch[key]) {
				if (typeof sketch[key] === 'function') {
					return sketch[key].apply(sketch, args);
				} else if (args.length === 0) {
					return sketch[key];
				} else if (args.length === 1) {
					return sketch[key] = args[0];
				}
			} else {
				return $.error('Sketch.js did not recognize the given command.');
			}
		} else if (sketch) {
			return sketch;
		} else {
			this.data('sketch', new Sketch(this.get(0), key));
			return this;
		}
	};

	Sketch = (function() {
		function Sketch(el, opts) {
			this.el = el;
			this.canvas = $(el);
			this.context = el.getContext('2d');
			this.options = $.extend({
				toolLinks: false, /* mod by philipp */
				defaultTool: 'marker',
				defaultColor: '#000000',
				defaultSize: 5
			}, opts);
			this.painting = false;
			this.color = this.options.defaultColor;
			this.size = this.options.defaultSize;
			this.tool = this.options.defaultTool;
			this.actions = [];
			this.action = [];
			this.enable(true);
			if (this.options.toolLinks) {
				$('body').delegate("a[href=\"#" + (this.canvas.attr('id')) + "\"]", 'click', function(e) {
					var $canvas, $this, key, sketch, _i, _len, _ref;
					$this = $(this);
					$canvas = $($this.attr('href'));
					sketch = $canvas.data('sketch');
					_ref = ['color', 'size', 'tool'];
					for (_i = 0, _len = _ref.length; _i < _len; _i++) {
						key = _ref[_i];
						if ($this.attr("data-" + key)) {
							sketch.set(key, $(this).attr("data-" + key));
						}
					}
					if ($(this).attr('data-download')) {
						sketch.download($(this).attr('data-download'));
					}
					return false;
				});
			}
		}
		
		Sketch.prototype.download = function(format) {
			var mime;
			format || (format = "png");
			if (format === "jpg") {
				format = "jpeg";
			}
			mime = "image/" + format;
			return window.open(this.el.toDataURL(mime));
		};

		Sketch.prototype.set = function(key, value) {
			this[key] = value;
			return this.canvas.trigger("sketch.change" + key, value);
		};

		Sketch.prototype.startPainting = function() {
			this.painting = true;
			return this.action = {
				tool: this.tool,
				color: this.color,
				size: parseFloat(this.size),
				events: []
			};
		};

		Sketch.prototype.stopPainting = function() {
			if (this.action) {
				this.actions.push(this.action);
			}
			this.painting = false;
			this.action = null;
			return this.redraw();
		};

		Sketch.prototype.onEvent = function(e) {
			// @by philipp
			// 모바일 터치 이벤트 오류 수정
			if (e.originalEvent && e.originalEvent.targetTouches) {
				e.pageX = e.originalEvent.targetTouches.length > 0 ? e.originalEvent.targetTouches[0].pageX : $.sketch.lastEvent.originalEvent.targetTouches[0].pageX;
				e.pageY = e.originalEvent.targetTouches.length > 0 ? e.originalEvent.targetTouches[0].pageY : $.sketch.lastEvent.originalEvent.targetTouches[0].pageY;
			}
			$.sketch.lastEvent = e;
			$.sketch.tools[$(this).data('sketch').tool].onEvent.call($(this).data('sketch'), e);
			e.preventDefault();
			return false;
		};

		Sketch.prototype.redraw = function() {
			var sketch;
			this.el.width = this.canvas.width();
			this.context = this.el.getContext('2d');
			sketch = this;
			$.each(this.actions, function() {
				if (this.tool) {
					return $.sketch.tools[this.tool].draw.call(sketch, this);
				}
			});
			if (this.painting && this.action) {
				return $.sketch.tools[this.action.tool].draw.call(sketch, this.action);
			}
		};

		// @by philipp
		// 직접 canvas에 그리지 않고 포인트를 source상에서 추가
		Sketch.prototype.addPoint = function(pointer) {
			if (this.action) {
				if (pointer.length) {
					for (var i = 0; i < pointer.length; i++) {
						this.action.events.push({
							x: (pointer[i].x || 0) - this.canvas.offset().left,
							y: (pointer[i].y || 0) - this.canvas.offset().top,
							event: 'mousedown'
						});
					};
				} else {
					this.action.events.push({
						x: (pointer.x || 0) - this.canvas.offset().left,
						y: (pointer.y || 0) - this.canvas.offset().top,
						event: 'mousedown'
					});
				}
			}
		};

		// @by philipp
		// 이벤트 바인딩 on/off
		Sketch.prototype.enable = function(isEnable) {
			if (isEnable) {
				this.canvas.bind('click mousedown mouseup mousemove mouseleave mouseout touchstart touchend touchcancel touchmove', this.onEvent);
			} else {
				this.canvas.unbind('click mousedown mouseup mousemove mouseleave mouseout touchstart touchend touchcancel touchmove', this.onEvent);
			}
		};

		// @by philipp
		// 캔버스 클리어
		Sketch.prototype.clear = function() {
			// clear
			this.set('tool', 'eraser');
			this.startPainting();
			this.addPoint([{'x':0, 'y':0}, {'x':0, 'y':0}]);
			this.redraw();

			// 초기화
			this.painting = false;
			this.color = this.options.defaultColor;
			this.size = this.options.defaultSize;
			this.tool = this.options.defaultTool;
			this.actions = [];
			this.action = [];
		};
		
		// @by philipp
		// base64 dataUrl 반환
		Sketch.prototype.getDataURL = function(format) {
			var mime;
			format || (format = "png");
			if (format === "jpg") {
				format = "jpeg";
			}
			mime = "image/" + format;
			
			return this.el.toDataURL(mime);
		};		

		return Sketch;
	})();

	$.sketch = {
		tools: {},
		lastEvent: null /* @by philipp */
	};

	$.sketch.tools.marker = {
		onEvent: function(e) {
			switch (e.type) {
				case 'mousedown':
				case 'touchstart':
					this.startPainting();
					break;
				case 'mouseup':
				case 'mouseout':
				case 'mouseleave':
				case 'touchend':
				case 'touchcancel':
					this.stopPainting();
					break;
			}
			if (this.painting) {
				this.action.events.push({
					x: e.pageX - this.canvas.offset().left,
					y: e.pageY - this.canvas.offset().top,
					event: e.type
				});
				return this.redraw();
			}
		},

		draw: function(action) {
			var event, previous, _i, _len, _ref;
			this.context.lineJoin = "round";
			this.context.lineCap = "round";
			this.context.beginPath();
			this.context.moveTo(action.events[0].x, action.events[0].y);
			_ref = action.events;
			for (_i = 0, _len = _ref.length; _i < _len; _i++) {
				event = _ref[_i];
				this.context.lineTo(event.x, event.y);
				previous = event;
			}
			this.context.strokeStyle = action.color;
			this.context.lineWidth = action.size;
			return this.context.stroke();
		}
	};

	return $.sketch.tools.eraser = {
		onEvent: function(e) {
			return $.sketch.tools.marker.onEvent.call(this, e);
		},

		draw: function(action) {
			var oldcomposite;
			oldcomposite = this.context.globalCompositeOperation;
			this.context.globalCompositeOperation = "copy";
			action.color = "rgba(0,0,0,0)";
			$.sketch.tools.marker.draw.call(this, action);
			return this.context.globalCompositeOperation = oldcomposite;
		}
	};

})(jQuery);