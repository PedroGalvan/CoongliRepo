'use strict';

angular.module('coongliApp').controller('TextableDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Textable',
        function($scope, $stateParams, $uibModalInstance, entity, Textable) {

        $scope.textable = entity;
        $scope.load = function(id) {
            Textable.get({id : id}, function(result) {
                $scope.textable = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('coongliApp:textableUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.textable.id != null) {
                Textable.update($scope.textable, onSaveSuccess, onSaveError);
            } else {
                Textable.save($scope.textable, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForCreationMoment = {};

        $scope.datePickerForCreationMoment.status = {
            opened: false
        };

        $scope.datePickerForCreationMomentOpen = function($event) {
            $scope.datePickerForCreationMoment.status.opened = true;
        };
}]);
