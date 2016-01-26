'use strict';

angular.module('coongliApp').controller('ActionDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Action', 'User',
        function($scope, $stateParams, $uibModalInstance, entity, Action, User) {

        $scope.action = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            Action.get({id : id}, function(result) {
                $scope.action = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('coongliApp:actionUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.action.id != null) {
                Action.update($scope.action, onSaveSuccess, onSaveError);
            } else {
                Action.save($scope.action, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
