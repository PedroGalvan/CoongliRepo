'use strict';

angular.module('coongliApp').controller('ReportDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Report', 'Session',
        function($scope, $stateParams, $uibModalInstance, entity, Report, Session) {

        $scope.report = entity;
        $scope.sessions = Session.query();
        $scope.load = function(id) {
            Report.get({id : id}, function(result) {
                $scope.report = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('coongliApp:reportUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.report.id != null) {
                Report.update($scope.report, onSaveSuccess, onSaveError);
            } else {
                Report.save($scope.report, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
