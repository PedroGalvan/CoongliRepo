'use strict';

angular.module('coongliApp')
    .controller('ReportDetailController', function ($scope, $rootScope, $stateParams, entity, Report, Session) {
        $scope.report = entity;
        $scope.load = function (id) {
            Report.get({id: id}, function(result) {
                $scope.report = result;
            });
        };
        var unsubscribe = $rootScope.$on('coongliApp:reportUpdate', function(event, result) {
            $scope.report = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
