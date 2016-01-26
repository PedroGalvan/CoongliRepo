'use strict';

angular.module('coongliApp')
    .controller('MesageDetailController', function ($scope, $rootScope, $stateParams, entity, Mesage, Actor, Messagefolder) {
        $scope.mesage = entity;
        $scope.load = function (id) {
            Mesage.get({id: id}, function(result) {
                $scope.mesage = result;
            });
        };
        var unsubscribe = $rootScope.$on('coongliApp:mesageUpdate', function(event, result) {
            $scope.mesage = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
