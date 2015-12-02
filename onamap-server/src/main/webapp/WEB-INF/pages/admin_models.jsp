<%@ page isELIgnored="false" trimDirectiveWhitespaces="true"
         contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ taglib prefix="onamap" tagdir="/WEB-INF/tags/onamap" %>
<onamap:html>

  <script type="text/javascript">
    window.models = ${models};
  </script>
  <onamap:head>
  </onamap:head>
  <onamap:body>
    <div ng-app="App">
      <div id="content" ng-controller="ListCtrl">
        <div class="row" ng-repeat="model in models">
          <div class="col-xs-12">
            <pre>{{model | json}}</pre>
          </div>
          <br/>
        </div>
      </div>
    </div>
  </onamap:body>
  <onamap:scripts>
    <script type="text/javascript">
      var angularApp = angular.module('App', []);

      angularApp.controller('ListCtrl', function ($scope) {
        $scope.models = window.models;
      });
    </script>
  </onamap:scripts>
</onamap:html>