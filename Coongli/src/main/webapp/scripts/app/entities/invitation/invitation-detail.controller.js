'use strict';

angular.module('coongliApp')
    .controller('InvitationDetailController', function ($scope, $rootScope, $stateParams, entity, Invitation, User, Session) {
        $scope.invitation = entity;
        $scope.load = function (id) {
            Invitation.get({id: id}, function(result) {
                $scope.invitation = result;
            });
        };
        var unsubscribe = $rootScope.$on('coongliApp:invitationUpdate', function(event, result) {
            $scope.invitation = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
