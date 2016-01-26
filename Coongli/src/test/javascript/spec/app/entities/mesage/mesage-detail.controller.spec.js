'use strict';

describe('Controller Tests', function() {

    describe('Mesage Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockMesage, MockActor, MockMessagefolder;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockMesage = jasmine.createSpy('MockMesage');
            MockActor = jasmine.createSpy('MockActor');
            MockMessagefolder = jasmine.createSpy('MockMessagefolder');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Mesage': MockMesage,
                'Actor': MockActor,
                'Messagefolder': MockMessagefolder
            };
            createController = function() {
                $injector.get('$controller')("MesageDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'coongliApp:mesageUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
