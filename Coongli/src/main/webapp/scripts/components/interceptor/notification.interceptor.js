 'use strict';

angular.module('coongliApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-coongliApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-coongliApp-params')});
                }
                return response;
            }
        };
    });
