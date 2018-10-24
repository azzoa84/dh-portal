<%@ page contentType="text/html; charset=UTF-8"%>
<div>
<div class="form-group">
  <label class="sr-only" for="txtAddress">Email address</label>
  <input type="text" class="form-control" id="txtAddress" placeholder="Enter address" ng-model="params.address" ng-keydown="onClickSearch();">
</div>
<button type="button" class="btn btn-default" ng-click="search.address();">도로명주소</button>
<div class="row mt10">
	<div class="col-md-12">
		<table class="table">
			<thead>
				<tr>
					<th>우편번호</th>
					<th>도로명 주소</th>
					<th>지번</th>
				</tr>
			</thead>
			<tbody>
				<tr ng-repeat="m in data.addressList">
					<td>{{m.zipNo}}</td>
					<td>{{m.roadAddr}}</td>
					<td>{{m.jibunAddr}}</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
</div>

