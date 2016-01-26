'use strict';

angular.module('coongliApp')
    .controller('InvoiceDetailController', function ($scope, $rootScope, $stateParams, entity, Invoice, User) {
        $scope.invoice = entity;
        $scope.load = function (id) {
            Invoice.get({id: id}, function(result) {
                $scope.invoice = result;
            });
        };
        var unsubscribe = $rootScope.$on('coongliApp:invoiceUpdate', function(event, result) {
            $scope.invoice = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
