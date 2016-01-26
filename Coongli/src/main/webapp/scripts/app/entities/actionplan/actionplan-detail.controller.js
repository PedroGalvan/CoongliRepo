'use strict';

angular.module('coongliApp')
    .controller('ActionplanDetailController', function ($scope, $rootScope, $stateParams, entity, Actionplan, User) {
        $scope.actionplan = entity;
        $scope.load = function (id) {
            Actionplan.get({id: id}, function(result) {
                $scope.actionplan = result;
            });
        };
        var unsubscribe = $rootScope.$on('coongliApp:actionplanUpdate', function(event, result) {
            $scope.actionplan = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
