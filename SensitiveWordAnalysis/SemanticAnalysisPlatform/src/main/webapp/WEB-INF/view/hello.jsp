<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>segment</title>
<link rel="shortcut icon" href="<c:url value="/resources/images/sensitive.ico" />">
<link rel="stylesheet" href="<c:url value="/resources/lib/translate.css" />">
<script type="text/javascript" src="<c:url value="/resources/lib/jquery-3.3.1.js" />"></script>
</head>
<body>
	<h3 class="title">It's a simple Word segmenter</h3>
	<div class="wrap">
		<div class='areaContent'>
			<div class="input-wrap">
				<div class="ls-wrap">
					<div class="operate-left">
						<div class='button mode-button button-checked' onClick="clickBtn()">分词</div>
						<div class='button mode-button' onClick="getTextAnalysisResult()">相似词语</div>
						<div class='button mode-button' onClick="getTextAnalysisResult()">敏感分析</div>
					</div>
					<div class="operate-center">
						<div class="button-img"></div>
					</div>
					<div class="operate-right">
						<button class='u-random' style="height:100%" onClick="addText()">添加文本</button>
					</div>
				</div>
				<textarea id="inputArea" class="area" rows="10" cols="30">
				</textarea>
			</div>
			<div class="output-wrap">
				<span id="outputArea" class="area area-out-span"></span>
			</div>
		</div>
	</div>

	<script type="text/javascript">
		var textIndex = 0;
		var wordIndex = 0;
		var placeholders = [ '我新造一个词叫幻想乡你能识别并正确标注词性吗？', '我的希望是希望张晚霞的背影被晚霞映红',
				'今天，刘志军案的关键人物,山西女商人丁书苗在市二中院出庭受审。', '刘喜杰石国祥会见吴亚琴先进事迹报告团成员' ];
		$(function() {
			$("#inputArea").val("");
			$("#outputArea").val("");
			$(".mode-button").click(function() {
				$(".mode-button").removeClass("button-checked");
				$(this).addClass("button-checked");
			});
		});

		function addText() {
			textIndex = ++textIndex % placeholders.length;
			$('#inputArea').val(placeholders[textIndex]);
		}

		function clickBtn() {
			var text = $('#inputArea').val();
			$.get('segment', {
				text : text
			}, function(result) {
				if (result.hr == 0 && result.data) {
					var segment = '';
					$.each(result.data, function(index, item) {
						segment += item.word + '  ';
					});
					$("#outputArea").html(segment);
				}
			});
		}

		function getTextAnalysisResult() {
			$("#outputArea").val("");
			var text = $('#inputArea').val();
			$.get('getTextAnalysisResult', {
				text : text
			}, function(result) {
				if (result.hr == 0 && result.data) {
					$("#outputArea").val(result.data);
				}
			});
		}
	</script>
</body>
</html>