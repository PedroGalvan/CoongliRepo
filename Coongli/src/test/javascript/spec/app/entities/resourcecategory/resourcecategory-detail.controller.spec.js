'use strict';

describe('Controller Tests', function() {

    describe('Resourcecategory Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockResourcecategory, MockUser, MockResource;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockResourcecategory = jasmine.createSpy('MockResourcecategory');
            MockUser = jasmine.createSpy('MockUser');
            MockResource = jasmine.createSpy('MockResource');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Resourcecategory': MockResourcecategory,
                'User': MockUser,
                'Resource': MockResource
            };
            createController = function() {
                $injector.get('$controller')("ResourcecategoryDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'coongliApp:resourcecategoryUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
