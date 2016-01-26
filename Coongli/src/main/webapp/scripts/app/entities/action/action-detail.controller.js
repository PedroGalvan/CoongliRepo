'use strict';

angular.module('coongliApp')
    .controller('ActionDetailController', function ($scope, $rootScope, $stateParams, entity, Action, User) {
        $scope.action = entity;
        $scope.load = function (id) {
            Action.get({id: id}, function(result) {
                $scope.action = result;
            });
        };
        var unsubscribe = $rootScope.$on('coongliApp:actionUpdate', function(event, result) {
            $scope.action = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
