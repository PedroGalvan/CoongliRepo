'use strict';

angular.module('coongliApp').controller('ResourcecategoryDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Resourcecategory', 'User', 'Resource',
        function($scope, $stateParams, $uibModalInstance, entity, Resourcecategory, User, Resource) {

        $scope.resourcecategory = entity;
        $scope.users = User.query();
        $scope.resources = Resource.query();
        $scope.load = function(id) {
            Resourcecategory.get({id : id}, function(result) {
                $scope.resourcecategory = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('coongliApp:resourcecategoryUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.resourcecategory.id != null) {
                Resourcecategory.update($scope.resourcecategory, onSaveSuccess, onSaveError);
            } else {
                Resourcecategory.save($scope.resourcecategory, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
