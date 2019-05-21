<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<jsp:include page="/WEB-INF/view/header.jsp" flush="true" />

<body>
	<div class="title">SemanticAnalysisPlatform</div>
	<div class="wrap-m">
		<div class="btn-group">
			<button class='btn-random' onClick="addText()">随机添加文本</button>
		</div>
		<div class='areaContent'>
			<div class="input-wrap">
				<div class="operate">
					<div class="operate-left">
						<div class='mode-tab mode-tab-checked'>分词</div>
						<div class='mode-tab'>相似词语</div>
						<div class='mode-tab'>依存句法关系</div>
						<div class='mode-tab'>敏感识别</div>
					</div>
					<div class="operate-center">
						<div id="translateBtn" class="button-img"></div>
					</div>
					<div class="operate-right">
						<div id="attributeTab" class="attribute-tab" onClick="changeAttribute()">显示词性</div>
					</div>
				</div>
				<textarea id="inputArea" class="area" rows="10" cols="30"></textarea>
			</div>
			<div class="output-wrap">
				<span id="outputArea" class="area area-out-span"></span>
			</div>
		</div>
		<div class="dependenceContent">
			<textarea id="input" rows="10" cols="80" style="display:none;"></textarea>
			<textarea id="log" rows="10" cols="80" disabled="disabled" style="display:none;"></textarea>
			<textarea id="parsed" style="display:none;"></textarea>
			<div id="vis" style="margin-top:20px;"></div>
			<div class="conllu-parse" data-visid="vis" data-inputid="input" data-parsedid="parsed" data-logid="log">
			</div>
		</div>
	</div>
	
	<script type="text/javascript">
		var textIndex = 0;
		var wordIndex = 0;
		var showAttribute = false;
		var modeTypes = ["分词", "相似词语", "依存句法关系", "敏感识别"]
		var modeType = "分词";
		var segmentWords = "";
		
		$(function() {
			$(".dependenceContent").addClass("hidden");
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
				getResult();
			});
		});
		
		$("#translateBtn").click(function(){
			getResult();
		});
		
		function dependencyClear() {
			$("#input").text("");
			$(".dependenceContent").addClass("hidden");
			$("#outputArea").html("");
		}

		function addText() {
			$.get('getText', function(result) {
				if (result.hr == 0 && result.data) {
					$('#inputArea').val(result.data);
					getResult();
				}
			});
		}
		
		function getResult() {
			dependencyClear();
			if(modeType == modeTypes[0]) {
				getSegmentResult();
			} else if(modeType == modeTypes[1]) {
				getNearestResult();
			} else if(modeType == modeTypes[2]) {
				getTextDependenciesResult();
			} else if(modeType == modeTypes[3]) {
				getTextAnalysisResult();
			}
		}

		function getSegmentResult() {
			dependencyClear();
			var text = $('#inputArea').val();
			$.get('getTextSegmentResult', {
				text : text
			}, function(result) {
				if (result.hr == 0 && result.data) {
					segmentWords = result.data;
					showSegment();
				}
			});
		}
		
		function showSegment() {
			var segment = '';
			$.each(segmentWords, function(index, item) {
				if(showAttribute) {
					segment += item.wordWithNature + '&nbsp&nbsp&nbsp&nbsp';
				} else {
					segment += item.word + '&nbsp&nbsp&nbsp&nbsp';
				}
			});
			$("#outputArea").html(segment);
		}

		function getNearestResult() {
			dependencyClear();
			var text = $('#inputArea').val();
			$.get('getTextNearestResult', {
				text : text
			}, function(result) {
				if (result.hr == 0 && result.data) {
					var nearest = '';
					$.each(result.data, function(index, item) {
						if(Object.keys(item.nearestWords).length > 0) {
							nearest += item.word + ": <br>";
							var sortable = [];
							for (var name in item.nearestWords) {
							    sortable.push([name, item.nearestWords[name]]);
							}
							sortable.sort(function(a, b) {
							    return b[1] - a[1];
							});
							$.each(sortable, function(nearestWord, nearestCos) {
								nearest += "&nbsp&nbsp&nbsp&nbsp" + nearestWord + ", &nbsp" + nearestCos + "<br>";
							});
						} else {
							nearest += item.word + "<br>";
						}
					});
					$("#outputArea").html(nearest);
				}
			});
		}
		
		function getTextDependenciesResult() {
			dependencyClear();
			var text = $('#inputArea').val();
			$.get('getTextDependenciesResult', {
				text : text
			}, function(result) {
				if (result.hr == 0 && result.data) {
					$("#outputArea").html(result.data.coNLLSentenceUIList);
					$("#input").text(result.data.coNLLSentenceList);
					$(".dependenceContent").removeClass("hidden");
					$("#input").change();
				}
			});
		}
		
		function getTextAnalysisResult() {
			dependencyClear();
			var text = $('#inputArea').val();
			$.get('getTextAnalysisResult', {
				text : text
			}, function(result) {
				if (result.hr == 0) {
					if(result.data) {
						$("#outputArea").html("<span style='color:red'>敏感语句</span>");
					} else {
						$("#outputArea").html("非敏感语句");
					}
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
			showSegment();
		}
	</script>
</body>
