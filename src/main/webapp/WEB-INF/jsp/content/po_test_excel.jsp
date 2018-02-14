<%@ page contentType="text/html; charset=UTF-8"%>
<div>
<button type="button" class="btn btn-success" ng-click="file.testExcelUpload();">Excel</button>
<input type="file" id="testExcelUpload" name="files" style="display:none" />

<ul class="list-group mt10">
	<span class="list-group-item active">Excel List</span>
	<li class="list-group-item" ng-repeat="m in params.fileList">
		<span class="underline" ng-click="file.doDownload(m.fileId);">{{m.fileName}}</span>
		<span class="glyphicon glyphicon-trash cursor" aria-hidden="true" ng-click="file.doDelete(m.fileId, $index);"></span>
	</li>
</ul>
</div>