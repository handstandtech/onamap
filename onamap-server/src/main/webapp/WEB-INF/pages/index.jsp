<%@ page isELIgnored="false" trimDirectiveWhitespaces="true"
         contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="onamap" tagdir="/WEB-INF/tags/onamap" %>
<html ng-app="myApp">
<onamap:head/>
<onamap:body>
  <div class="row" ng-controller="IndexCtrl">
    <div class="col-xs-12 text-center">
      <h4>
        <a href="${DOMAIN_BASE_URL}/login">Login</a>, <a class="" href="${DOMAIN_BASE_URL}/signup">Sign Up</a> or check out a cool map:
      </h4>
    </div>
    <div class="col-xs-4"></div>
    <div class="col-xs-4">
      <ul>
        <li ng-repeat="user in users"><a href="http://{{user.username}}.onamap.net">{{user.username}}</a></li>
      </ul>
    </div>
    <div class="col-xs-4"></div>
  </div>
  <br/>
  <br/>
  <br/>
  <onamap:scripts>
    <script type="text/javascript">
      window.pageData = <c:out escapeXml="false" value="${pageData}"/>;

      var app = angular.module('myApp', ['appControllers']);
      var appControllers = angular.module('appControllers', []);

      appControllers.controller('IndexCtrl', ['$scope', '$rootScope', '$http',
        function ($scope, $rootScope, $http) {
          $scope.users = window.pageData.users;
        }
      ]);
    </script>
  </onamap:scripts>
</onamap:body>
</html>