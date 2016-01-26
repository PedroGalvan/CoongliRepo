'use strict';

angular.module('coongliApp').controller('ResourceDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Resource',
        function($scope, $stateParams, $uibModalInstance, entity, Resource) {

        $scope.resource = entity;
        $scope.load = function(id) {
            Resource.get({id : id}, function(result) {
                $scope.resource = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('coongliApp:resourceUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.resource.id != null) {
                Resource.update($scope.resource, onSaveSuccess, onSaveError);
            } else {
                Resource.save($scope.resource, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
