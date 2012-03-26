<%@ page isELIgnored="false" trimDirectiveWhitespaces="true"
	contentType="text/html;charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="onamap" tagdir="/WEB-INF/tags/onamap"%>
<onamap:html>
<onamap:head />
<onamap:body>
	<c:if test="${EMAIL_TAKEN}">
		<h1>Email is already registered.</h1>
	</c:if>
	<c:if test="${USERNAME_TAKEN}">
		<h1>Username is already taken.</h1>
	</c:if>
	<c:if test="${MISSING_EMAIL}">
		<h1>Please provide an email address.</h1>
	</c:if>
	<c:if test="${MISSING_USERNAME}">
		<h1>Please provide a username.</h1>
	</c:if>
	<c:if test="${MISSING_PASSWORD}">
		<h1>Please provide a password.</h1>
	</c:if>
	<c:if test="${INVALID_EMAIL}">
		<h1>Please provide a valid email address.</h1>
	</c:if>
	<c:if test="${INVALID_USERNAME}">
		<h1>Please provide a valid username.</h1>
	</c:if>
	<c:if test="${INVALID_PASSWORD}">
		<h1>Please provide a valid password.</h1>
	</c:if>


	<form method="post" action="/action/signup">
		<label>Email</label> <input type="text" name="email" value="${email}" />
		<br /> <label>Username</label> <input type="text" name="username"
			value="${username}" /> <br /> <label>Password</label> <input
			type="password" name="pass" /> <br /> <input type="submit"
			value="Sign Up" />
	</form>
	</body>
</onamap:body>
</onamap:html>