'use strict';

angular.module('coongliApp')
	.controller('ResourcecategoryDeleteController', function($scope, $uibModalInstance, entity, Resourcecategory) {

        $scope.resourcecategory = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Resourcecategory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
