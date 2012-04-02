<%@ page isELIgnored="false" trimDirectiveWhitespaces="true"
	contentType="text/html;charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ taglib prefix="onamap" tagdir="/WEB-INF/tags/onamap"%>
<onamap:html>
<onamap:head>
	<meta name="google-site-verification"
		content="QJpl8tWzCU-5tAhtqa3i9IcDFFX-yaMnPnGe_Vz89JI" />
	</onamap:head>
<onamap:body>
	<strong>View <a href="${CURRENT_USERS_PAGE_URL}">${CURRENT_USER.username}'s
			page</a></strong>
	<br />
	<strong>Logged in as ${CURRENT_USER.email}</strong> -
	<a href="${DOMAIN_BASE_URL}/action/logout">Logout Here</a>
	<br />
	<c:choose>
		<c:when test="${CURRENT_USER.flickrInfo!=null}">
			<strong> Linked to Flickr as <a
				href="http://flickr.com/photos/${CURRENT_USER.flickrInfo.id}">${CURRENT_USER.flickrInfo.username}</a>
			</strong> - 
								<a href="${DOMAIN_BASE_URL}/action/flickr/disconnect">Disconnect
				Flickr Account</a>
		</c:when>
		<c:otherwise>
			<a href="${DOMAIN_BASE_URL}/action/flickr/connect">Connect Flickr
				Account</a>
		</c:otherwise>
	</c:choose>

	<div id="content">
		<div>
			<c:choose>
				<c:when test="${photosets!=null}">
					<ul>
						<c:forEach var="photoset" items="${photosets.photoset}">
							<li><a
								href="${DOMAIN_BASE_URL}/action/flickr/photoset?id=${photoset.id}">${photoset.title._content}</a></li>
						</c:forEach>
					</ul>
				</c:when>
				<c:otherwise>
					<a href="${DOMAIN_BASE_URL}/action/flickr/load_photosets">Load
						Photosets</a>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</onamap:body>
</onamap:html>