'use strict';

angular.module('coongliApp')
	.controller('ActionplanDeleteController', function($scope, $uibModalInstance, entity, Actionplan) {

        $scope.actionplan = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Actionplan.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
