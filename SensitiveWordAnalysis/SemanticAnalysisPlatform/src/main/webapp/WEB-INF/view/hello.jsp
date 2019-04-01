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
	<div class="operate operate-left">
		<button class='button' onClick="addText()">添加文本</button>
	</div>
	<div class="operate">
		<div class='input areaContent'>
			<textarea id="inputArea" class="area" rows="10" cols="30">
			</textarea>
		</div>
		<button class='button' onClick="clickBtn()">分词</button>
		<div class='output areaContent'>
			<textarea id="outputArea" class="area" rows="10" cols="30"
				disabled="disabled">
			</textarea>
		</div>
	</div>

	<h3 class="title">It's a simple Word2Vec nearest</h3>
	<div class="operate operate-left">
		<button class='button' onClick="addWord()">添加词语</button>
	</div>
	<div class="operate">
		<div class='input areaContent'>
			<textarea id="inputWordArea" class="area" rows="10" cols="30">
			</textarea>
		</div>
		<button class='button' onClick="clickBtnWord2Vec()">nearest</button>
		<div class='output areaContent'>
			<textarea id="outputWordArea" class="area" rows="10" cols="30"
				disabled="disabled">
			</textarea>
		</div>
	</div>
	<script type="text/javascript">
		var textIndex = 0;
		var wordIndex = 0;
		var placeholders = [ '我新造一个词叫幻想乡你能识别并正确标注词性吗？', '我的希望是希望张晚霞的背影被晚霞映红',
				'今天，刘志军案的关键人物,山西女商人丁书苗在市二中院出庭受审。', '刘喜杰石国祥会见吴亚琴先进事迹报告团成员' ];
		var words = [ '中国', '美丽', '购买' ];
		$(function() {
		});

		function addText() {
			textIndex = ++textIndex % placeholders.length;
			$('#inputArea').val(placeholders[textIndex]);
		}

		function addWord() {
			wordIndex = ++wordIndex % words.length;
			$('#inputWordArea').val(words[wordIndex]);
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
					$("#outputArea").val(segment);
				}
			});
		}

		function clickBtnWord2Vec() {
			var word = $('#inputWordArea').val();
			$.get('nearest', {
				word : word
			}, function(result) {
				if (result.hr == 0 && result.data) {
					var segment = '';
					$.each(result.data, function(index, item) {
						segment += index + '  ' + item + '\n';
					});
					$("#outputWordArea").val(segment);
				}
			});
		}
	</script>
</body>
</html>