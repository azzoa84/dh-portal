function mainController($scope, $timeout, $injector, $filter)
{	
	angular.element(document).ready(function () {
		$scope.init.data();
		$scope.init.view();
	});
	
	$scope.init = 
	{
		data : function()
		{
			$scope.params = {};
			$scope.params.fileList = [];
			
			$scope.stackFileList = [];
		},
		view : function()
		{
			$scope.imageBox = new potalImage('imageBox', 'testImage', 300, 400);
			$scope.imageBox.setMode('N');
			$scope.imageBox.setZooming(true);
			$scope.imageBox.setBorder(true);
			
			// File Change Event
			$('#testFileUpload').change(function(event)
			{
				$scope.file.doFile(event);
			});
		}
	};
	
	$scope.file = {
		testFileUpload : function()
    	{
			$('#testFileUpload').val('');
			$('#testFileUpload').click();
    	},
    	doFile : function(event)
    	{
    		
    	}
	};
}