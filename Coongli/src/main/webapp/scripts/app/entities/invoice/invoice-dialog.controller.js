'use strict';

angular.module('coongliApp').controller('InvoiceDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Invoice', 'User',
        function($scope, $stateParams, $uibModalInstance, entity, Invoice, User) {

        $scope.invoice = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            Invoice.get({id : id}, function(result) {
                $scope.invoice = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('coongliApp:invoiceUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.invoice.id != null) {
                Invoice.update($scope.invoice, onSaveSuccess, onSaveError);
            } else {
                Invoice.save($scope.invoice, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
