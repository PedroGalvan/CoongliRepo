'use strict';

angular.module('coongliApp').controller('SessionDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Session', 'Report', 'User', 'Invitation',
        function($scope, $stateParams, $uibModalInstance, $q, entity, Session, Report, User, Invitation) {

        $scope.session = entity;
        $scope.reports = Report.query({filter: 'session-is-null'});
        $q.all([$scope.session.$promise, $scope.reports.$promise]).then(function() {
            if (!$scope.session.report || !$scope.session.report.id) {
                return $q.reject();
            }
            return Report.get({id : $scope.session.report.id}).$promise;
        }).then(function(report) {
            $scope.reports.push(report);
        });
        $scope.users = User.query();
        $scope.invitations = Invitation.query();
        $scope.load = function(id) {
            Session.get({id : id}, function(result) {
                $scope.session = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('coongliApp:sessionUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.session.id != null) {
                Session.update($scope.session, onSaveSuccess, onSaveError);
            } else {
                Session.save($scope.session, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForStartmoment = {};

        $scope.datePickerForStartmoment.status = {
            opened: false
        };

        $scope.datePickerForStartmomentOpen = function($event) {
            $scope.datePickerForStartmoment.status.opened = true;
        };
        $scope.datePickerForEndmoment = {};

        $scope.datePickerForEndmoment.status = {
            opened: false
        };

        $scope.datePickerForEndmomentOpen = function($event) {
            $scope.datePickerForEndmoment.status.opened = true;
        };
}]);
