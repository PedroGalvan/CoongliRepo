'use strict';

angular.module('coongliApp').controller('ActionplanDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Actionplan', 'User',
        function($scope, $stateParams, $uibModalInstance, $q, entity, Actionplan, User) {

        $scope.actionplan = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            Actionplan.get({id : id}, function(result) {
                $scope.actionplan = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('coongliApp:actionplanUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.actionplan.id != null) {
                Actionplan.update($scope.actionplan, onSaveSuccess, onSaveError);
            } else {
                Actionplan.save($scope.actionplan, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
