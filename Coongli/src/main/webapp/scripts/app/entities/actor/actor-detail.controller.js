'use strict';

angular.module('coongliApp')
    .controller('ActorDetailController', function ($scope, $rootScope, $stateParams, entity, Actor, Messagefolder, Mesage) {
        $scope.actor = entity;
        $scope.load = function (id) {
            Actor.get({id: id}, function(result) {
                $scope.actor = result;
            });
        };
        var unsubscribe = $rootScope.$on('coongliApp:actorUpdate', function(event, result) {
            $scope.actor = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
