var filterProvider = (function () {
	/** https://docs.angularjs.org/api **/
    /*
    @ HTML에서 필터 사용
    {{ yourExpression | yourFilter: arg1:arg2:... }}

    @ JavaScript에서 필터 사용
    $filter('yourFilter')(yourExpression, arg1, ...)
    
    @ Default 앵글러 필터 리스트
    Lowercase: $filter('lowercase')('ABCDEFG'); // {{ 'ABCDEFG' | lowercase }}
    Uppercase: $filter('uppercase')('abcdefg'); // {{ 'abcdefg' | uppercase }}
    Number(3자리콤마): $filter('number')(100000); // {{ 100000 | number }}
    
    LimitTo(길이제한): $filter('limitTo')('ABCDEFG', 3); // {{ 'ABCDEFG' | limitTo:3 }}
    (*limitTo의 경우 배열인 경우 배열길이제한 만큼 리턴)
    
    orderBy(리스트정렬): https://docs.angularjs.org/api/ng/filter/orderBy 참조
    */
    return [
        /** 10보다 작은 수일때, 앞에 0을 붙혀 두자리로 표시해야하는 경우 사용 */
		['addZero', function () {
		    return function(input) {
		    	if (!input || isNaN(input)) return;
		    	return (input < 10) ? '0'+input : String(input);
		    };
		}],
		
        /** Array<Object> 형태의 목록에서 특정 키값으로 필터링
         * @inHTML {{ [{productCode: 'P0001', productName: '품목1'}] | search:'productCode':'P0001' }}
         * @inJS $filter('search')(arr, 'productCode', 'P0001');
         */
		['search', function () {
		    return function(input, key, value, likeSearch) {
		    	if (!input || input.length == 0) return;
		    	if (likeSearch === undefined) likeSearch = false;
		    		
		    	var out = input.filter(search);
		    	
		    	function search(element, index) {
		    		return (likeSearch ? String(element[key]).indexOf(value) != -1 : element[key] == value);
		    	}
		    	
		    	return out;
		    };
		}],
		
        /** Array<Object> 형태의 목록에서 여러개의 키값으로 필터링
         * @inJS $filter('search')(arr, ['productCode', 'deptCode'], ['P0001', 'D0001']);
         */
		['search2', function () {
		    return function(input, keys, values) {
		    	if (!input || input.length == 0) return;
		    	
		    	var out = input.filter(search);
		    	function search(element, index) {
		    		for(var i = 0; i < keys.length; i++) {
						if (!element[keys[i]] || element[keys[i]] != values[i]) {
							return false;
						}
					}
		    		return true;
		    	}
		    	
		    	return out;
		    };
		}],
		
        /** 문자열(string)에 대한 날짜,시각 포맷 정의
         * @inHTML {{ 20140709 | date:'yyyyMMdd' }}
         * @inJS $filter('date')('20140709', 'yyyyMMdd');
         */
		['date', function () {
		    return function(input, format, delimiter, timeDelimiter) {
		    	var dd = delimiter || '.';
		    	var td = timeDelimiter || ':';
		        var out = String(input);
		        if (out) {
		            switch (format) {
		                case 'yyyy':
		                    out = out.substr(0, 4);
		                    break;                    
		                case 'yyyyMM':
		                    out = out.substr(0, 4) + dd + out.substr(4, 2);
		                    break;
		                case 'MMdd':
		                	out = out.substr(4, 2) + dd + out.substr(6, 2);
		                    break;		                    
		                case 'yyyyMMdd':
		                    out = out.substr(0, 4) + dd + out.substr(4, 2) + dd + out.substr(6, 2);
		                    break;
		                case 'yyMMdd':
		                    out = out.substr(2, 2) + dd + out.substr(4, 2) + dd + out.substr(6, 2);
		                    break;
		                case 'yyyyMMddHHmm':
		                	out = out.substr(0, 4) + dd + out.substr(4, 2) + dd + out.substr(6, 2) + ' ' + out.substr(8, 2) + td + out.substr(10, 2);
		                    break;
		                case 'yyyyMMddHHmmss':
		                	out = out.substr(0, 4) + dd + out.substr(4, 2) + dd + out.substr(6, 2) + ' ' + out.substr(8, 2) + td + out.substr(10, 2) + td + out.substr(12, 2);
		                    break;
		                case 'HHmm':
		                	out = out.substr(0, 2) + td + out.substr(2, 2);
		                    break;
		                case 'HHmmss':
		                	out = out.substr(0, 2) + td + out.substr(2, 2) + td + out.substr(4, 2);
		                    break;
		            }
		        }
		        return out;
		    };
		}],
		
        /** Date형에 대한 날짜,시각 포맷을 정의해 문자열로 변환하여 리턴
         * @inHTML {{ Date형식값 | date2:'yyyyMMddHHmmss' }}
         * @inJS $filter('date2')(new Date(), 'yyyyMMddHHmmss');
         */
		['date2', function () {
		    return function(dt, format, delimiter, timeDelimiter) {
		    	var dd = delimiter || '.';
		    	var td = timeDelimiter || ':';
		    	
		    	if (!dt) return;
		    	var out = "";
	            switch (format) {
	                case 'yyyy':
	                    out = dt.getFullYear();
	                    break;
	                case 'yyyyMM':
	                    out = dt.getFullYear() + dd + addZero(dt.getMonth() + 1);
	                    break;
	                case 'MMdd':
	                    out = addZero(dt.getMonth() + 1) + dd + addZero(dt.getDate());
	                    break;               
	                case 'yyyyMMdd':
	                    out = dt.getFullYear() + dd + addZero(dt.getMonth() + 1) + dd + addZero(dt.getDate());
	                    break;
	                case 'yyyyMMddHHmm':
	                	out = dt.getFullYear() + dd + addZero(dt.getMonth() + 1) + dd + addZero(dt.getDate()) + ' ' + addZero(dt.getHours()) + td + addZero(dt.getMinutes());
	                    break;
	                case 'yyyyMMddHHmmss':
	                	out = dt.getFullYear() + dd + addZero(dt.getMonth() + 1) + dd + addZero(dt.getDate()) + ' ' + addZero(dt.getHours()) + td + addZero(dt.getMinutes()) + td + addZero(dt.getSeconds());
	                    break;
	                case 'HHmm':
	                	out = addZero(dt.getHours()) + td + addZero(dt.getMinutes());
	                    break;
	                case 'HHmmss':
	                	out = addZero(dt.getHours()) + td + addZero(dt.getMinutes()) + td + addZero(dt.getSeconds());
	                    break;
	            }
		        
		        function addZero(i) {
		            var rtn = i + 100;
		            return rtn.toString().substring(1, 3);
		        }
		        
		        return out;
		    };
		}],
		
		/** 숫자에 콤마를 삽입
         * @inHTML {{ product.price * product.quantity | numberWithCommas }}
         */
		['numberWithCommas', function () {
			return function(item) {
				var str = String(item); 
				var split = str.split(".");
				if(split.length > 1){
					// 소수점이 있을경우 
					return split[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",") + "." + split[1];
				}else{
					//소수점이 없는경우
					return split[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
				}
			};
		}],
		
		/** 숫자에 콤마를 삽입
         * @inHTML {{ product.price * product.quantity | numberWithCommas }}
         */
		['dateForChart', function () {
			return function(item) {
				var str = String(item); 
				return str.substring(2, 4) + "\'" + str.substring(4, str.length);
			};
		}]
    ];
}());

