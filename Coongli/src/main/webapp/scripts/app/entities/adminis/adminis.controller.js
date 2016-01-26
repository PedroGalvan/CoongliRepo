'use strict';

angular.module('coongliApp')
    .controller('AdminisController', function ($scope, $state, Adminis) {

        $scope.adminiss = [];
        $scope.loadAll = function() {
            Adminis.query(function(result) {
               $scope.adminiss = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.adminis = {
                id: null
            };
        };
    });
