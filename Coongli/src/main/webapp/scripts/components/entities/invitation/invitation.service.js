'use strict';

angular.module('coongliApp')
    .factory('Invitation', function ($resource, DateUtils) {
        return $resource('api/invitations/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.creationmoment = DateUtils.convertLocaleDateFromServer(data.creationmoment);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.creationmoment = DateUtils.convertLocaleDateToServer(data.creationmoment);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.creationmoment = DateUtils.convertLocaleDateToServer(data.creationmoment);
                    return angular.toJson(data);
                }
            }
        });
    });
