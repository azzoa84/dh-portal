<%@ page contentType="text/html; charset=UTF-8"%>

<div>
<button type="button" class="btn btn-success" ng-click="file.testFileUpload();">파일추가</button>
<input type="file" id="testFileUpload" name="files" style="display:none" multiple>

<ul class="list-group mt10">
	<span class="list-group-item active">파일목록</span>
	<li class="list-group-item" ng-repeat="m in params.fileList">
		<span class="underline" ng-click="file.doDownload(m.fileId);">{{m.fileName}}</span>
		<span class="glyphicon glyphicon-trash cursor" aria-hidden="true" ng-click="file.doDelete(m.fileId, $index);"></span>
	</li>
</ul>
	<div id="imageBox"></div>
</div>

