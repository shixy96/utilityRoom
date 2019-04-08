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
						<div class='mode-tab mode-tab-checked' onClick="getSegmentResult()">分词</div>
						<div class='mode-tab' onClick="getNearestResult()">相似词语</div>
						<div class='mode-tab' onClick="getTextDependenciesResult()">语义依存关系</div>
						<div class='mode-tab' onClick="getTextAnalysisResult()">敏感分析</div>
					</div>
					<div class="operate-center">
						<div class="button-img" onClick="getResult()"></div>
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
		var placeholders = [
			'我新造一个词叫幻想乡你能识别并正确标注词性吗？', 
			"徐先生还具体帮助他确定了把画雄鹰、松鼠和麻雀作为主攻目标。",
			'我的希望是希望张晚霞的背影被晚霞映红',
			'今天，刘志军案的关键人物,山西女商人丁书苗在市二中院出庭受审。', 
			'刘喜杰石国祥会见吴亚琴先进事迹报告团成员' 
		];
		var showAttribute = false;
		var modeTypes = ["分词", "相似词语", "语义依存关系", "敏感分析"]
		var modeType = "分词";
		var segmentWords = "";
		
		$(function() {
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
		
		function getResult() {
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
			$("#outputArea").val("");
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
			$("#outputArea").val("");
			var text = $('#inputArea').val();
			$.get('getTextDependenciesResult', {
				text : text
			}, function(result) {
				if (result.hr == 0 && result.data) {
					$("#outputArea").html(result.data.coNLLSentenceUIList);
					$("#input").text(result.data.coNLLSentenceList);
					$("#input").change();
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
			showSegment();
		}
	</script>
</body>
