'use strict';

angular.module('coongliApp')
	.controller('AdminisDeleteController', function($scope, $uibModalInstance, entity, Adminis) {

        $scope.adminis = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Adminis.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
