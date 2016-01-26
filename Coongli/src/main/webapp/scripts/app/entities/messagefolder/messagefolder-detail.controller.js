'use strict';

angular.module('coongliApp')
    .controller('MessagefolderDetailController', function ($scope, $rootScope, $stateParams, entity, Messagefolder, Actor, Mesage) {
        $scope.messagefolder = entity;
        $scope.load = function (id) {
            Messagefolder.get({id: id}, function(result) {
                $scope.messagefolder = result;
            });
        };
        var unsubscribe = $rootScope.$on('coongliApp:messagefolderUpdate', function(event, result) {
            $scope.messagefolder = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
