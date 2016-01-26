'use strict';

angular.module('coongliApp').controller('MessagefolderDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Messagefolder', 'Actor', 'Mesage',
        function($scope, $stateParams, $uibModalInstance, entity, Messagefolder, Actor, Mesage) {

        $scope.messagefolder = entity;
        $scope.actors = Actor.query();
        $scope.mesages = Mesage.query();
        $scope.load = function(id) {
            Messagefolder.get({id : id}, function(result) {
                $scope.messagefolder = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('coongliApp:messagefolderUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.messagefolder.id != null) {
                Messagefolder.update($scope.messagefolder, onSaveSuccess, onSaveError);
            } else {
                Messagefolder.save($scope.messagefolder, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
