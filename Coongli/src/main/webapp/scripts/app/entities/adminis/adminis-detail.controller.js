'use strict';

angular.module('coongliApp')
    .controller('AdminisDetailController', function ($scope, $rootScope, $stateParams, entity, Adminis) {
        $scope.adminis = entity;
        $scope.load = function (id) {
            Adminis.get({id: id}, function(result) {
                $scope.adminis = result;
            });
        };
        var unsubscribe = $rootScope.$on('coongliApp:adminisUpdate', function(event, result) {
            $scope.adminis = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
