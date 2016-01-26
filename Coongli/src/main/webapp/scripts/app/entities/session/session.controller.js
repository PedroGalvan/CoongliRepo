'use strict';

angular.module('coongliApp')
    .controller('SessionController', function ($scope, $state, Session, ParseLinks) {

        $scope.sessions = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Session.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.sessions = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.session = {
                startmoment: null,
                endmoment: null,
                periodica: null,
                hidden: null,
                accepted: null,
                cancelled: null,
                id: null
            };
        };
    });
