'use strict';

angular.module('coongliApp').controller('MesageDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Mesage', 'Actor', 'Messagefolder',
        function($scope, $stateParams, $uibModalInstance, entity, Mesage, Actor, Messagefolder) {

        $scope.mesage = entity;
        $scope.actors = Actor.query();
        $scope.messagefolders = Messagefolder.query();
        $scope.load = function(id) {
            Mesage.get({id : id}, function(result) {
                $scope.mesage = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('coongliApp:mesageUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.mesage.id != null) {
                Mesage.update($scope.mesage, onSaveSuccess, onSaveError);
            } else {
                Mesage.save($scope.mesage, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForSentmoment = {};

        $scope.datePickerForSentmoment.status = {
            opened: false
        };

        $scope.datePickerForSentmomentOpen = function($event) {
            $scope.datePickerForSentmoment.status.opened = true;
        };
}]);
