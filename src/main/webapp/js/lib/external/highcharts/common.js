// highcharts 공통 속성
function getHighChartCommonProperties() {
	return {
		credits: { enabled: false },
		navigation: {
			buttonOptions: {
				enabled: false
			}
		}
	};
};

function createHighChartProperties(prop) {
	var commProp = getHighChartCommonProperties();
	jQuery.extend(true, prop, commProp);
	
	return prop;
};