'use strict';

angular.module('coongliApp')
	.controller('MessagefolderDeleteController', function($scope, $uibModalInstance, entity, Messagefolder) {

        $scope.messagefolder = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Messagefolder.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
