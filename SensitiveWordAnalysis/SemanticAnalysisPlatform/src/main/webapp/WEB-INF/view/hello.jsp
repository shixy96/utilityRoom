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
		<link rel="stylesheet" href="<c:url value="/resources/lib/css/main.css" />">
		<script type="text/javascript" src="<c:url value="/resources/lib/jquery-3.3.1.js" />"></script>
	</head>
	<body>
		<div class="title">SemanticAnalysisPlatform</div>
		<div class="wrap">
			<div class="btn-group">
				<button class='btn-random' onClick="addText()">随机添加文本</button>
			</div>
			<div class='areaContent'>
				<div class="input-wrap">
					<div class="operate">
						<div class="operate-left">
							<div class='mode-tab mode-tab-checked' onClick="clickBtn()">分词</div>
							<div class='mode-tab' onClick="getTextAnalysisResult()">相似词语</div>
							<div class='mode-tab' onClick="getTextAnalysisResult()">敏感分析</div>
						</div>
						<div class="operate-center">
							<div class="button-img"></div>
						</div>
						<div class="operate-right">
							<div id="attributeTab" class="attribute-tab" onClick="changeAttribute()">显示词性</div>
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
			var showAttribute = false;
			var modeTypes = ["分词", "相似词语", "敏感分析"]
			var modeType = "分词";
			
			$(function() {
				$("#inputArea").val("");
				$("#outputArea").val("");
				$(".mode-tab").click(function() {
					modeType = $(this).html();
					$(".mode-tab").removeClass("mode-tab-checked");
					$(this).addClass("mode-tab-checked");
					if(modeType != modeTypes[0]) {
						changeAttribute();
						$("#attributeTab").addClass("disable");
					} else {
						$("#attributeTab").removeClass("disable");
					}
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
			
			function changeAttribute() {
				if(modeType == modeTypes[0]) {
					$("#attributeTab").removeClass("disable");
					showAttribute = !showAttribute;
					if(showAttribute) {
						$("#attributeTab").addClass("mode-tab-checked");
						$("#attributeTab").html("隐藏词性");
					} else {
						$("#attributeTab").removeClass("mode-tab-checked");
						$("#attributeTab").html("显示词性");
					}
				} else {
					showAttribute = false;
					$("#attributeTab").removeClass("mode-tab-checked");
					$("#attributeTab").html("显示词性");
					$("#attributeTab").addClass("disable");
				}
			}
		</script>
	</body>
</html>