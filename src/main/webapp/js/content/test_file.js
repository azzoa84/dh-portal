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
			// File Change Event
			$('#testFileUpload').change(function(e)
			{
				$scope.file.doFile();
			});
		}
	};
	
	$scope.file = {
		testFileUpload : function()
    	{
			$('#testFileUpload').val('');
			$('#testFileUpload').click();
    	},
    	doFile : function()
    	{
    		var param = $scope.createFormData('testFileUpload');
    		
    		if (!param)
    		{
    			console.log("파일 욜양 초과");
    			
    		}
    		
    		var fileInfo = util.callFileUpload('./fileUpload.do', param);
    		
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
    			
    			$scope.stackFileList.push(fileObj);
    		}
    		$scope.params.fileList = $scope.stackFileList;
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
    			var filtered = $scope.stackFileList.filter(function(m) {
    			    return m.fileId !== fileId;
    			});
    			$scope.stackFileList = filtered;
    			$scope.params.fileList = filtered;
    		}
    	}
	};
	
	$scope.createFormData = function(id)
	{
		var formData = new FormData();
		var bResult = true;
		$.each($('#' + id)[0].files, function(i, file) {
			if (file.size > 100000000)	bResult = false;
			
			formData.append('files', file);
		});
		
		if (bResult)		return formData;
		else				return formData;
	}
}