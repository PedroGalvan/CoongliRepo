'use strict';

angular.module('coongliApp').controller('AdminisDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Adminis',
        function($scope, $stateParams, $uibModalInstance, entity, Adminis) {

        $scope.adminis = entity;
        $scope.load = function(id) {
            Adminis.get({id : id}, function(result) {
                $scope.adminis = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('coongliApp:adminisUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.adminis.id != null) {
                Adminis.update($scope.adminis, onSaveSuccess, onSaveError);
            } else {
                Adminis.save($scope.adminis, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
