<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="now" class="java.util.Date"/>
<fmt:formatDate var="year" value="${now}" pattern="yyyy"/>
<hr/>
<footer class="wrapper">
    <div class="row">
      <div class="col-xs-5 text-left">
        <a href="/about">About</a>
        <span>&nbsp;|&nbsp;</span>
        <a href="/terms">Terms</a>
        <span>&nbsp;|&nbsp;</span>
        <a href="/privacy" class="last">Privacy</a>
      </div>
      <div class="col-xs-7 text-right">
        <a href="http://handstandtech.com" title="Handstand Technologies">&copy; 2011 - ${year} Handstand Technologies, LLC</a>
      </div>
    </div>
</footer>