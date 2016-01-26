'use strict';

angular.module('coongliApp')
    .controller('SessionDetailController', function ($scope, $rootScope, $stateParams, entity, Session, Report, User, Invitation) {
        $scope.session = entity;
        $scope.load = function (id) {
            Session.get({id: id}, function(result) {
                $scope.session = result;
            });
        };
        var unsubscribe = $rootScope.$on('coongliApp:sessionUpdate', function(event, result) {
            $scope.session = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
