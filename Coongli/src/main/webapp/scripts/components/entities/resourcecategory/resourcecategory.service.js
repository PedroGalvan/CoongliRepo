'use strict';

angular.module('coongliApp')
    .factory('Resourcecategory', function ($resource, DateUtils) {
        return $resource('api/resourcecategorys/:id', {}, {
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
