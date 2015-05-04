<%@ page isELIgnored="false" trimDirectiveWhitespaces="true"
         contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="onamap" tagdir="/WEB-INF/tags/onamap" %>
<html ng-app="myApp">
<onamap:head>


  <!-- Bootstrap core CSS -->
  <link href="/assets/libs/bootstrap-3.3.4/css/bootstrap.min.css" rel="stylesheet">
  <link href="/assets/css/profile.css" rel="stylesheet" type="text/css"/>
  <style>#content > div {
    width: 100%;
    height: 100%;
    background: white;
    position: absolute;
  }</style>

</onamap:head>

<body>

<!-- Fixed navbar -->
<nav class="navbar navbar-default navbar-fixed-top">
  <div class="container">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="#">${requestScope.USERNAME}</a>
    </div>
    <div id="navbar" class="collapse navbar-collapse">
      <ul class="nav navbar-nav">
        <li><a href="#" onclick="showTab(0);return false;">US States</a></li>
        <li><a href="#" onclick="showTab(1);return false;">Timeline</a></li>
        <li><a href="#" onclick="showTab(2);return false;">World Map</a></li>

      </ul>
    </div>
    <!--/.nav-collapse -->
  </div>
</nav>

<table id="wrapper">
  <tbody>
  <tr>
    <td>
      <div id="header">
        <ul id="tabs">
          <li>US Map</li>
          <li>Google Map</li>
        </ul>
        <strong><a href="${DOMAIN_BASE_URL}">HOME</a></strong><br/>
        <c:if test="${COOKIE_USER!=null}">
          <strong>Logged in as ${COOKIE_USER .email}</strong> - <a
            href="${DOMAIN_BASE_URL}/action/logout">Logout Here</a>
          <br/>
        </c:if>

      </div>
    </td>
  </tr>
  <tr class="middle">
    <td id="tdcontent">
      <div id="content">
        <div id="us-map-svg"></div>
        <div id="timeline" ng-controller="TimelineCtrl" >
          <div class="row">

            <div class="col-sm-4" ng-repeat="photo in photos">
              <div class="row" style="padding:10px;">
                <div class="col-xs-12 text-center">
                  <img ng-src="{{photo.url_s}}" class="img-thumbnail"/>
                  <div>
                    <strong>{{photo.title}}</strong><span> on </span>
                    <span>{{photo.datetaken | date:'MMMM dd, yyyy'}}</span></div>
                </div>
              </div>
            </div>
          </div>
        </div>
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


<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.15/angular.min.js"></script>

<script type="text/javascript"
        src="//maps.googleapis.com/maps/api/js?sensor=false"></script>
<script src="/assets/js/raphael/raphael-2.1.min.js"></script>
<script src="/assets/js/raphael/scale.raphael-0.8.js"></script>
<script src="/assets/js/maps-svg/us-map-svg.js"></script>
<script type="text/javascript">
  var json = <c:out value='${json}' escapeXml="false"/>;
</script>
<script src="/assets/libs/bootstrap-3.3.4/js/bootstrap.min.js"></script>
<script src="/assets/js/profile_bootstrap.js"></script>
</body>
</html>