'use strict';

angular.module('coongliApp')
    .factory('Mesage', function ($resource, DateUtils) {
        return $resource('api/mesages/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.sentmoment = DateUtils.convertLocaleDateFromServer(data.sentmoment);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.sentmoment = DateUtils.convertLocaleDateToServer(data.sentmoment);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.sentmoment = DateUtils.convertLocaleDateToServer(data.sentmoment);
                    return angular.toJson(data);
                }
            }
        });
    });
