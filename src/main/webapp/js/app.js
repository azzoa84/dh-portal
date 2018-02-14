var angular_comm = (function ()
{
	var module = angular.module('comm', [], function ($provide, $compileProvider, $controllerProvider, $filterProvider)
	{
		// 필터 추가
		if(filterProvider)
		{
			for(var i=0; i < filterProvider.length; i++)
			{
				$filterProvider.register(filterProvider[i][0], filterProvider[i][1]);
			}			
		}
		
		// 다이렉티브 추가
		if(directiveProvider)
		{
			for(var i=0; i < directiveProvider.length; i++)
			{
				$compileProvider.directive.apply(null, directiveProvider[i]);
			}
		}
		
		// 서비스 추가
		if(serviceProvider)
		{
			for(var i=0; i < serviceProvider.length; i++)
			{
				$provide.value(serviceProvider[i][0], serviceProvider[i][1]);
			}
		}
	});
	
	// 외부 컨트롤러
	module.controller('CommCtrl', function($scope) {
		angular.element(document).ready(function () {
	    	
			$scope.data = {};
		});
	});
	
	// 화면 개별 컨트롤러 추가
	if(mainController)
	{
		module.controller('MainCtrl', mainController);
		module.directive('currencyInput', function($filter, $browser) {
		    return {
		        require: 'ngModel',
		        link: function($scope, $element, $attrs, ngModelCtrl)
		        {
		            var listener = function()
		            {
		                var value = $element.val().replace(/,/g, '');
		                $element.val($filter('number')(value, false));
		            };
		            
		            // This runs when we update the text field
		            ngModelCtrl.$parsers.push(function(viewValue) { return viewValue.replace(/,/g, ''); });
		            
		            // This runs when the model gets updated on the scope directly and keeps our view in sync
		            ngModelCtrl.$render = function() { $element.val($filter('number')(ngModelCtrl.$viewValue, false)); };
		            
		            // If the keys include the CTRL, SHIFT, ALT, or META keys, or the arrow keys, do nothing.
	                // This lets us support copy and paste too
		            $element.bind('change', listener);
		            $element.bind('keydown', function(event) {
		                var key = event.keyCode;
		                if(key == 91 || (15 < key && key < 19) || (37 <= key && key <= 40)) return;
		                
		                $browser.defer(listener); // Have to do this or changes don't get picked up properly
		            });		            
		            $element.bind('paste cut', function() { $browser.defer(listener); });
		        }
		    };
		});		
		mainController.$inject = ['$scope', '$timeout', '$injector', '$filter'];
	}
	return module;
}());