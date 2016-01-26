'use strict';

angular.module('coongliApp')
	.controller('SessionDeleteController', function($scope, $uibModalInstance, entity, Session) {

        $scope.session = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Session.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
