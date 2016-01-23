<%@ page isELIgnored="false" trimDirectiveWhitespaces="true"
         contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="onamap" tagdir="/WEB-INF/tags/onamap" %>
<html ng-app="myApp">
<onamap:head>

  <link href="/assets/css/profile.css" rel="stylesheet" type="text/css"/>
  <style>#content > div {
    position: absolute;
    left: 0px;
    top: 0px;
    width: 100%;
    height: 100%;
    overflow: auto;
    background: white;
  }

  #timeline img.img-thumbnail {
    max-height: 250px;
  }
  </style>

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
      <a class="navbar-brand" href="#" onclick="showTab(0);return false;">${USERNAME}</a>
    </div>
    <div id="navbar" class="collapse navbar-collapse">
      <ul class="nav navbar-nav">
        <li><a href="#" onclick="showTab(1);return false;">US States</a></li>
        <li><a href="#" onclick="showTab(2);return false;">Timeline</a></li>
        <li><a href="#" onclick="showTab(3);return false;">World Map</a></li>

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
        <div id="stats" ng-controller="StatsCtrl">
          <br/>

          <div class="container">
            <div class="row" style="font-size: 80%;">
              <div class="col-xs-12 text-center">
                <span>Latest Photo</span> on <span style="font-size: 80%">{{datetaken}}</span><br/>
                <strong>{{photo.title}}</strong>
                <br/>
                <br/>
                <a ng-href="{{photo.link}}" target="_blank">
                  <img ng-src="{{photo.url_m}}" class="img-thumbnail"/>
                </a>
                <br/><br/>
              </div>
              <div class="col-xs-4">
                <strong>Countries</strong>
                <ul>
                  <li ng-repeat="(countryName, country) in data.world.places">
                    <span>{{countryName}} - {{country.photos.length}}</span>
                  </li>
                </ul>
              </div>
              <div class="col-xs-4">
                <strong>US States</strong>
                <ul>
                  <li ng-repeat="(stateName, state) in data.world.places['United States'].places">
                    <span>{{stateName}} - {{state.photos.length}}</span>
                  </li>
                </ul>
              </div>
              <div class="col-xs-4">
                <strong>By Date</strong>
                <ul>
                  <li ng-repeat="(fullYear, year) in photosByDate">
                    <span>{{fullYear}} - {{year.photos.length}}</span>
                    <span ng-repeat="month in year.months track by $index" ng-show="year.months[$index].photos.length>0">
                    <ul>
                      <li>{{month.name}} - {{month.photos.length}}</li>
                    </ul>
                    </span>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
        <div id="us-map-svg"></div>
        <div id="timeline" ng-controller="TimelineCtrl">
          <h3 class="text-center">Total Photo Count: {{photos.length}}</h3>

          <div class="row" ng-repeat="row in rows">
            <div class="col-sm-4" ng-repeat="photo in row track by $index">
              <div class="text-center" style="padding:10px;">
                <a ng-href="{{photo.link}}" target="_blank">
                  <img ng-src="{{photo.url_m}}" class="img-thumbnail"/>
                </a>

                <div>
                  <strong>{{photo.title}}</strong><span> on </span>
                  <span>{{photo.datetaken | date:'MMMM dd, yyyy'}}</span></div>
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
      <div id="bottom"></div>
    </td>
  </tr>
  </tbody>
</table>

<onamap:scripts>
  <script type="text/javascript"
          src="//maps.googleapis.com/maps/api/js"></script>
  <script src="/assets/js/raphael/raphael-2.1.min.js"></script>
  <script src="/assets/js/raphael/scale.raphael-0.8.js"></script>
  <script src="/assets/js/maps-svg/us-map-svg.js"></script>
  <script type="text/javascript">
    var json = <c:out value='${json}' escapeXml="false"/>;
    <c:if test="${not empty param.mock}">
    <onamap:mock></onamap:mock>
    </c:if>
  </script>
  <script src="/assets/js/profile_bootstrap.js"></script>
</onamap:scripts>


</body>
</html>