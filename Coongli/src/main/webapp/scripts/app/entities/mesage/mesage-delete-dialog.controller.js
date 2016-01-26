'use strict';

angular.module('coongliApp')
	.controller('MesageDeleteController', function($scope, $uibModalInstance, entity, Mesage) {

        $scope.mesage = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Mesage.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
