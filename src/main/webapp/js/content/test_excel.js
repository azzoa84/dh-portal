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
		},
		view : function()
		{
			// File Change Event
			$('#testExcelUpload').change(function(e)
			{
				$scope.file.doFile();
			});
		}
	};
	
	$scope.file = {
		testExcelUpload : function()
    	{
			$('#testExcelUpload').val('');
			$('#testExcelUpload').click();
    	},
    	doFile : function()
    	{
    		var param = $scope.createFormData('testExcelUpload');
    		var fileInfo = util.callFileUpload('./excelUpload.do', param);
    		console.log(fileInfo);
    		var fileIDList = fileInfo.fileIDList;
    		var fileSizeList = fileInfo.fileSizeList;
    		var fileNameList = fileInfo.oriFileNameList;
    		
    		var fileList = [];
    		for (var i = 0; i < fileIDList.length; i++)
    		{
    			var fileObj = {};
    			fileObj.fileId = fileIDList[i];
    			fileObj.fileSize = fileSizeList[i];
    			fileObj.fileName = fileNameList[i];
    			
    			$scope.params.fileList.push(fileObj);
    		}
    		
    		$scope.$apply();
    	},
    	doDownload : function(fileId)
    	{
    		var param = {file_id: fileId};
    		util.downloadLink('./fileDownload.do', param);
    	},
    	doDelete : function(fileId, index)
    	{
    		var param = {file_id: fileId};
    		var data = util.callSyncAjax('./fileDelete.do', param);
    		
    		if (data)
    		{
    			var filtered = $scope.params.fileList.filter(function(m) {
    			    return m.fileId !== fileId;
    			});
    			
    			$scope.params.fileList = filtered;
    		}
    	}
	};
	
	$scope.createFormData = function(id)
	{
		var formData = new FormData();
		
		$.each($('#' + id)[0].files, function(i, file) {
			formData.append('files', file);
		});
		formData.append('params', 'TEST');
		return formData;
	}
}