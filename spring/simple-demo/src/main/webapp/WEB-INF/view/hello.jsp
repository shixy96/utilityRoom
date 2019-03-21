<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>simple segmenter</title>
<style type="text/css">
.title {
	background-color: #f8f8f8;
	margin-bottom: 30px;
	text-align: center;
	line-height: 30px;
}

.term {
	padding-right: 10px;
}
</style>
<script type="text/javascript"
	src="<c:url value="/resources/lib/jquery-3.3.1.js" />"></script>
</head>
<body>
	<h3 class="title">It's a simple Word segmenter</h3>
	<div>
		<div class='input'>
			<textarea id="inputArea" rows="10" cols="30">
			</textarea>
		</div>
		<button class='button' onClick="clickBtn()">分词</button>
		<div class='output'></div>
		<c:forEach items="${model.termList}" var="term">
			<span class="term">${term}</span>
		</c:forEach>
		
	</div>
	<script type="text/javascript">
		var placeholders = [ '我新造一个词叫幻想乡你能识别并正确标注词性吗？', '我的希望是希望张晚霞的背影被晚霞映红',
				'今天，刘志军案的关键人物,山西女商人丁书苗在市二中院出庭受审。', '刘喜杰石国祥会见吴亚琴先进事迹报告团成员' ];
		$(function() {
			$('#inputArea').val(placeholders[2]);
		});
		function clickBtn() {
			var text = $('#inputArea').val();
			console.log(text)
			console.log(typeof text)
			$.get('segment', {
				text : text
			}, function(result) {
				console.log(result)
				if(result.hr==0 && result.data) {
					$.each(result.data, function(index,item) {
						console.log(item)
					})
				}
			});
		}
	</script>
</body>
</html>