<%@ page contentType="text/html; charset=UTF-8"%>
<!-- 다운로드용  iframe -->
<iframe width="0" height="0" name="download_hidden_frame" style="display:none;"></iframe>

<!-- 확인/취소 메시지창 -->
<div id="comm_msg_box_confirm" class="alert_popup" style="display:none;">
	<div class="alert-title"><span class="alert-title-message">확인 메시지</span></div>
	<div class="alert-content">
		<span><img src="${pageContext.request.contextPath}/img/alert_question.png"></span>
		<span id="comm_msg_box_confirm_content" class="comm_msg_box_confirm_content"></span>
	</div>
	<div class="alert-btn-wrap">
		<a id="comm_msg_box_confirm_yes_btn" class="btn_l basic_btn">확인</a>
		<a id="comm_msg_box_confirm_no_btn" class="btn_l gray_btn">취소</a>
	</div>
</div>