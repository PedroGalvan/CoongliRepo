'use strict';

angular.module('coongliApp')
    .controller('TextableDetailController', function ($scope, $rootScope, $stateParams, entity, Textable) {
        $scope.textable = entity;
        $scope.load = function (id) {
            Textable.get({id: id}, function(result) {
                $scope.textable = result;
            });
        };
        var unsubscribe = $rootScope.$on('coongliApp:textableUpdate', function(event, result) {
            $scope.textable = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
