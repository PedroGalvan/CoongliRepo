'use strict';

angular.module('coongliApp')
    .factory('Session', function ($resource, DateUtils) {
        return $resource('api/sessions/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.startmoment = DateUtils.convertLocaleDateFromServer(data.startmoment);
                    data.endmoment = DateUtils.convertLocaleDateFromServer(data.endmoment);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.startmoment = DateUtils.convertLocaleDateToServer(data.startmoment);
                    data.endmoment = DateUtils.convertLocaleDateToServer(data.endmoment);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.startmoment = DateUtils.convertLocaleDateToServer(data.startmoment);
                    data.endmoment = DateUtils.convertLocaleDateToServer(data.endmoment);
                    return angular.toJson(data);
                }
            }
        });
    });
