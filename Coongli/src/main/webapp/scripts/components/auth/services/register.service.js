'use strict';

angular.module('coongliApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


