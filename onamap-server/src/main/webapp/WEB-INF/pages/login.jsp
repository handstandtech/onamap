<%@ page isELIgnored="false" trimDirectiveWhitespaces="true"
         contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="onamap" tagdir="/WEB-INF/tags/onamap" %>
<onamap:html>
  <onamap:head/>
  <onamap:body>
    <div class="row">
      <div class="col-xs-12">
        <h1>Login Page</h1>
        <br/>
        <c:if test="${INVALID_LOGIN}">
          <h1>Invalid Login Credentials</h1>
        </c:if>
        <form method="post" action="/action/login">
          <label>Email or Username</label> <input type="text" name="user" class="form-control"
                                                  value="${user}"/> <br/> <label>Password</label> <input
            type="password"  class="form-control" name="pass"/> <br/> <input type="submit" class="btn btn-primary"
                                                       value="Login"/>
        </form>
      </div>
    </div>
  </onamap:body>
</onamap:html>