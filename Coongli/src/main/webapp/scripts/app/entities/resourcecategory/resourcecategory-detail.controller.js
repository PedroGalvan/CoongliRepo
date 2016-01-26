'use strict';

angular.module('coongliApp')
    .controller('ResourcecategoryDetailController', function ($scope, $rootScope, $stateParams, entity, Resourcecategory, User, Resource) {
        $scope.resourcecategory = entity;
        $scope.load = function (id) {
            Resourcecategory.get({id: id}, function(result) {
                $scope.resourcecategory = result;
            });
        };
        var unsubscribe = $rootScope.$on('coongliApp:resourcecategoryUpdate', function(event, result) {
            $scope.resourcecategory = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
