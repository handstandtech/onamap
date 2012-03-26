<%@ tag isELIgnored="false" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<meta http-equiv="content-type" content="text/html;charset=utf-8" />
<meta name="author" content="Handstand Technologies, LLC" />
<meta name="copyright" content="&copy; 2010 Handstand Technologies, LLC" />
<meta name="robots" content="all" />
<meta name="title" content="On a Map" />
<meta name="Description" content="On a Map" />
<meta name="Keywords" content="On a Map" />
<c:choose>
	<c:when test="${title!=null}">
		<title>On a Map - ${title}</title>
	</c:when>
	<c:otherwise>
		<title>On a Map</title>
	</c:otherwise>
</c:choose>
<script type="text/javascript">
	/* Make sure we can use these elements, even in Internet Explorer */
	document.createElement("article");
	document.createElement("footer");
	document.createElement("header");
	document.createElement("hgroup");
	document.createElement("nav");
</script>
<link type="text/css" rel="stylesheet" href="/assets/css/style.css" />
<meta name="robots" content="all" />
<script type="text/javascript"
	src="/assets/js/jquery/jquery-1.7.1.min.js"></script>
<jsp:doBody />
</head>
