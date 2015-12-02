<%@ page isELIgnored="false" trimDirectiveWhitespaces="true"
         contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ taglib prefix="onamap" tagdir="/WEB-INF/tags/onamap" %>
<onamap:html>
  <onamap:head>
  </onamap:head>
  <onamap:body>

    <div id="content">
      <div class="row">
        <div class="col-xs-12">
          <form method="post" action="/admin/delete">
            <input type="submit" value="DELETE" class="btn btn-primary"/>
          </form>
        </div>
      </div>

      <ul>
        <c:forEach var="type" items="${types}">
          <li>
            <a href="/admin/models/${type}">${type}</a>
          </li>
        </c:forEach>
      </ul>
    </div>
  </onamap:body>
</onamap:html>