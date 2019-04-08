<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<meta name="viewport" content="width=device-width">
<title>SemanticAnalysisPlatform</title>

<script type="text/javascript" src="<c:url value="/resources/extra-lib/js/jquery.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/extra-lib/js/jquery.svg.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/extra-lib/js/jquery.svgdom.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/extra-lib/js/jquery-ui.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/extra-lib/js/waypoints.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/extra-lib/js/webfont.js" />"></script>

<script type="text/javascript" src="<c:url value="/resources/extra-lib/conllu/lib/brat/annotation_log.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/extra-lib/conllu/lib/brat/configuration.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/extra-lib/conllu/lib/brat/dispatcher.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/extra-lib/conllu/lib/brat/url_monitor.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/extra-lib/conllu/lib/brat/util.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/extra-lib/conllu/lib/brat/visualizer.js" />"></script>

<script type="text/javascript" src="<c:url value="/resources/extra-lib/conllu/lib/local/annodoc.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/extra-lib/conllu/lib/local/config.js" />"></script>

<script type="text/javascript" src="<c:url value="/resources/extra-lib/conllu/conllu.js" />"></script>

<link rel="shortcut icon" href="<c:url value="/resources/images/sensitive.ico" />">
<link rel="stylesheet" href="<c:url value="/resources/lib/css/main.css" />">
<link rel="stylesheet" href="<c:url value="/resources/extra-lib/css/jquery-ui-redmond.css" />">
<link rel="stylesheet" href="<c:url value="/resources/extra-lib/conllu/css/conllu-main.css" />">
<link rel="stylesheet" href="<c:url value="/resources/extra-lib/conllu/css/style-vis.css" />">

<script type="text/javascript">
	var webFontURLs = [];
	$(function() {
		Annodoc.activate(Config.bratCollData, {});
	});
</script>
</head>