'use strict';

angular.module('coongliApp')
    .factory('Goal', function ($resource, DateUtils) {
        return $resource('api/goals/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });