'use strict';

describe('Controller Tests', function() {

    describe('Actor Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockActor, MockMessagefolder, MockMesage;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockActor = jasmine.createSpy('MockActor');
            MockMessagefolder = jasmine.createSpy('MockMessagefolder');
            MockMesage = jasmine.createSpy('MockMesage');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Actor': MockActor,
                'Messagefolder': MockMessagefolder,
                'Mesage': MockMesage
            };
            createController = function() {
                $injector.get('$controller')("ActorDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'coongliApp:actorUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
