'use strict';

angular.module('coongliApp')
    .factory('Adminis', function ($resource, DateUtils) {
        return $resource('api/adminiss/:id', {}, {
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
