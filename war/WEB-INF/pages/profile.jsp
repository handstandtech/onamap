<%@ page isELIgnored="false" trimDirectiveWhitespaces="true"
	contentType="text/html;charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="onamap" tagdir="/WEB-INF/tags/onamap"%>
<onamap:html>
<onamap:head>
	<script type="text/javascript"
		src="//maps.googleapis.com/maps/api/js?sensor=false"></script>
	<script src="/assets/js/raphael/raphael-2.1.min.js"></script>
	<script src="/assets/js/raphael/scale.raphael-0.8.js"></script>
	<script src="/assets/js/maps-svg/us-map-svg.js"></script>
	<script type="text/javascript">
		var json = <c:out value='${json}' escapeXml="false"/>;
	</script>
	<script src="/assets/js/profile.js"></script>
	<link href="/assets/css/profile.css" rel="stylesheet" type="text/css"/>
</onamap:head>
<body>
	<table id="wrapper">
		<tbody>
			<tr>
				<td><div id="header">
						<ul id="tabs">
							<li>US Map</li>
							<li>Google Map</li>
						</ul>
						<strong><a href="${DOMAIN_BASE_URL}">HOME</a></strong><br />
						<c:if test="${COOKIE_USER!=null}">
							<strong>Logged in as ${COOKIE_USER .email}</strong> - <a
								href="${DOMAIN_BASE_URL}/action/logout">Logout Here</a>
							<br />
						</c:if>

					</div></td>
			</tr>
			<tr class="middle">
				<td id="tdcontent">
					<div id="content">
						<div id="us-map-svg"></div>
						<div id="google-map"></div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div id="bottom">Bottom</div>
				</td>
			</tr>
		</tbody>
	</table>
</body>
</onamap:html>