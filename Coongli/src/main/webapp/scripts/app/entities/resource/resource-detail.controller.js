'use strict';

angular.module('coongliApp')
    .controller('ResourceDetailController', function ($scope, $rootScope, $stateParams, entity, Resource) {
        $scope.resource = entity;
        $scope.load = function (id) {
            Resource.get({id: id}, function(result) {
                $scope.resource = result;
            });
        };
        var unsubscribe = $rootScope.$on('coongliApp:resourceUpdate', function(event, result) {
            $scope.resource = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
