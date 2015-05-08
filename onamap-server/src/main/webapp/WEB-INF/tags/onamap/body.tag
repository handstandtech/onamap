<%@ tag isELIgnored="false" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="onamap" tagdir="/WEB-INF/tags/onamap"%>
<body ng-cloak>
	<div class="container">
		<onamap:header />
		<jsp:doBody />
		<onamap:footer />
	</div>
</body>