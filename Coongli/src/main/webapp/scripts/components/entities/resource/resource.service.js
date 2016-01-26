'use strict';

angular.module('coongliApp')
    .factory('Resource', function ($resource, DateUtils) {
        return $resource('api/resources/:id', {}, {
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
