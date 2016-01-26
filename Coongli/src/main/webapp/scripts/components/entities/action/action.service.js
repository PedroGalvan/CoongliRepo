'use strict';

angular.module('coongliApp')
    .factory('Action', function ($resource, DateUtils) {
        return $resource('api/actions/:id', {}, {
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
