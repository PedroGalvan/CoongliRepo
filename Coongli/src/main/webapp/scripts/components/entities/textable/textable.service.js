'use strict';

angular.module('coongliApp')
    .factory('Textable', function ($resource, DateUtils) {
        return $resource('api/textables/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.creationMoment = DateUtils.convertLocaleDateFromServer(data.creationMoment);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.creationMoment = DateUtils.convertLocaleDateToServer(data.creationMoment);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.creationMoment = DateUtils.convertLocaleDateToServer(data.creationMoment);
                    return angular.toJson(data);
                }
            }
        });
    });
