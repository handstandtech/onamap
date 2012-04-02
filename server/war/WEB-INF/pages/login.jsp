<%@ page isELIgnored="false" trimDirectiveWhitespaces="true"
	contentType="text/html;charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="onamap" tagdir="/WEB-INF/tags/onamap"%>
<onamap:html>
<onamap:head />
<onamap:body>
	<h1>Login Page</h1>
	<br />
	<c:if test="${INVALID_LOGIN}">
		<h1>Invalid Login Credentials</h1>
	</c:if>
	<form method="post" action="/action/login">
		<label>Email or Username</label> <input type="text" name="user"
			value="${user}" /> <br /> <label>Password</label> <input
			type="password" name="pass" /> <br /> <input type="submit"
			value="Login" />
	</form>
</onamap:body>
</onamap:html>