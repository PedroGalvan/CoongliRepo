'use strict';

angular.module('coongliApp')
	.controller('TextableDeleteController', function($scope, $uibModalInstance, entity, Textable) {

        $scope.textable = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Textable.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
