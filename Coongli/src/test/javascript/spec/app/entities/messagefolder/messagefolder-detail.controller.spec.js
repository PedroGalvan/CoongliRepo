'use strict';

describe('Controller Tests', function() {

    describe('Messagefolder Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockMessagefolder, MockActor, MockMesage;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockMessagefolder = jasmine.createSpy('MockMessagefolder');
            MockActor = jasmine.createSpy('MockActor');
            MockMesage = jasmine.createSpy('MockMesage');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Messagefolder': MockMessagefolder,
                'Actor': MockActor,
                'Mesage': MockMesage
            };
            createController = function() {
                $injector.get('$controller')("MessagefolderDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'coongliApp:messagefolderUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
