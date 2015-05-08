<%@ tag isELIgnored="false" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
  <meta name="viewport" content="initial-scale=1,user-scalable=no"/>
  <meta http-equiv="content-type" content="text/html;charset=utf-8"/>
  <meta name="author" content="Handstand Technologies, LLC"/>
  <meta name="copyright" content="&copy; 2010 Handstand Technologies, LLC"/>
  <meta name="robots" content="all"/>
  <meta name="title" content="On a Map"/>
  <meta name="Description" content="On a Map"/>
  <meta name="Keywords" content="On a Map"/>
  <meta name="google-site-verification"
        content="QJpl8tWzCU-5tAhtqa3i9IcDFFX-yaMnPnGe_Vz89JI"/>
  <c:choose>
    <c:when test="${title!=null}">
      <title>On a Map - ${title}</title>
    </c:when>
    <c:otherwise>
      <title>On a Map</title>
    </c:otherwise>
  </c:choose>
  <!--link type="text/css" rel="stylesheet" href="/assets/css/style.css"/-->
  <meta name="robots" content="all"/>
  <!-- Bootstrap core CSS -->
  <link href="/assets/libs/bootstrap-3.3.4/css/bootstrap.min.css" rel="stylesheet">
  <jsp:doBody/>
</head>
