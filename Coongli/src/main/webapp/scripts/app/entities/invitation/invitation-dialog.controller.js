'use strict';

angular.module('coongliApp').controller('InvitationDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Invitation', 'User', 'Session',
        function($scope, $stateParams, $uibModalInstance, entity, Invitation, User, Session) {

        $scope.invitation = entity;
        $scope.users = User.query();
        $scope.sessions = Session.query();
        $scope.load = function(id) {
            Invitation.get({id : id}, function(result) {
                $scope.invitation = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('coongliApp:invitationUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.invitation.id != null) {
                Invitation.update($scope.invitation, onSaveSuccess, onSaveError);
            } else {
                Invitation.save($scope.invitation, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForCreationmoment = {};

        $scope.datePickerForCreationmoment.status = {
            opened: false
        };

        $scope.datePickerForCreationmomentOpen = function($event) {
            $scope.datePickerForCreationmoment.status.opened = true;
        };
}]);
