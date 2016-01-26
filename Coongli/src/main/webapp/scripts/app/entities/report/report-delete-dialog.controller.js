'use strict';

angular.module('coongliApp')
	.controller('ReportDeleteController', function($scope, $uibModalInstance, entity, Report) {

        $scope.report = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Report.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
