var directiveProvider = (function () {
	/** https://docs.angularjs.org/api **/
    /*
    @ HTML에서 다이렉티브 사용
    <h1 app-version></h1>
    */
    return [
		/* application version */
		['appVersion', function () {
			return function (scope, elm, attrs) {
		        elm.text("1.0");
			};
		}],
		
		/* application version name */
		['appVersionName', function () {
		    return function (scope, elm, attrs) {
		        elm.text("Jelly Bean");
		    };
		}],
		
		/* <div search-btn /> */
		['searchBtn', function () {
		    return function (scope, elm, attrs) {
		        elm.text("조회");
		        elm.addClass("btn red_btn");
		    };
		}],
		
		/* <input datepicker /> */
		['datepicker', function () {
		    return {
		    	restrict: "A",
		        require: "ngModel",
		    	link: function (scope, elm, attrs, ngModelCtrl) {
			    	var dateFormat;
			    	if(attrs.datepicker != ""){
			    		dateFormat = attrs.datepicker;
			    	}
			    	
			    	$.fn.datepicker.dates['kr'] = {
			    			days: ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일"],
			    			daysShort: ["일", "월", "화", "수", "목", "금", "토", "일"],
			    			daysMin: ["일", "월", "화", "수", "목", "금", "토", "일"],
			    			months: ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"],
			    			monthsShort: ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"]
			    	};
			    	
					var updateModel = function (dateText) {
						scope.$apply(function () {
							ngModelCtrl.$setViewValue(dateText);
						});
					};
					
					elm.datepicker({
						format: dateFormat || "yyyy.mm.dd",
						startView: 0,
						minViewMode: 0,
						todayBtn:"linked",
						language: "kr",
						orientation: "top auto",
						keyboardNavigation: false,
						forceParse: false,
						autoclose: true,
						todayHighlight: true
					}).on("changeDate", function (e) {
						updateModel(e.format());
					});
			    }
		    }
		}],
		
		['datepickermini', function () {
		    return {
		    	restrict: "A",
		        require: "ngModel",
		    	link: function (scope, elm, attrs, ngModelCtrl) {
			    	var dateFormat;
			    	if(attrs.datepickermini != ""){
			    		dateFormat = attrs.datepickermini;
			    	}
			    	
			    	$.fn.datepicker.dates['kr'] = {
			    			days: ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일"],
			    			daysShort: ["일", "월", "화", "수", "목", "금", "토", "일"],
			    			daysMin: ["일", "월", "화", "수", "목", "금", "토", "일"],
			    			months: ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"],
			    			monthsShort: ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"]
			    	};
			    	
					var updateModel = function (dateText) {
						scope.$apply(function () {
							ngModelCtrl.$setViewValue(dateText);
						});
					};
					
					elm.datepicker({
						format: dateFormat || "yyyy.mm",
						startView: 0,
						minViewMode: 1,
						todayBtn:"linked",
						language: "kr",
						orientation: "top auto",
						keyboardNavigation: false,
						forceParse: false,
						autoclose: true,
						todayHighlight: true
					}).on("changeDate", function (e) {
						updateModel(e.format());
					});
			    }
		    }
		}],
		
		['currencyinput', ['$filter',"$browser", function ($filter, $browser) {
		    return {
		        require: "ngModel",
		        link: function(scope, element, attrs, ngModelCtrl) {
		            var listener = function() {
		                var value = element.val().replace(/[^\d]+/g, '');
		                element.val(value.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,'));
		            }
		            
		            // This runs when we update the text field
		            ngModelCtrl.$parsers.push(function(viewValue) {
		                return viewValue.replace(/[^\d\.]+/g, '')
		            })
		            
		            // This runs when the model gets updated on the scope directly and keeps our view in sync
		            ngModelCtrl.$render = function() {
		                element.val(String(ngModelCtrl.$viewValue).replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,'))
		            }
		            
		            element.bind('change', listener)
		            element.bind('keydown', function(event) {
		                var key = event.keyCode
		                // If the keys include the CTRL, SHIFT, ALT, or META keys, or the arrow keys, do nothing.
		                // This lets us support copy and paste too
		                if (key == 91 || (15 < key && key < 19) || (37 <= key && key <= 40)) 
		                    return 
		                $browser.defer(listener) // Have to do this or changes don't get picked up properly
		            })
		            
		            element.bind('paste cut', function() {
		                $browser.defer(listener)  
		            })
		        }
		    }
		}]]
    ];
}());