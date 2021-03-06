'use strict';

angular.module('coongliApp').controller('GoalDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Goal', 'User',
        function($scope, $stateParams, $uibModalInstance, entity, Goal, User) {

        $scope.goal = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            Goal.get({id : id}, function(result) {
                $scope.goal = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('coongliApp:goalUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.goal.id != null) {
                Goal.update($scope.goal, onSaveSuccess, onSaveError);
            } else {
                Goal.save($scope.goal, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
