'use strict';

describe('Controller Tests', function() {

    describe('Session Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSession, MockReport, MockUser, MockInvitation;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSession = jasmine.createSpy('MockSession');
            MockReport = jasmine.createSpy('MockReport');
            MockUser = jasmine.createSpy('MockUser');
            MockInvitation = jasmine.createSpy('MockInvitation');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Session': MockSession,
                'Report': MockReport,
                'User': MockUser,
                'Invitation': MockInvitation
            };
            createController = function() {
                $injector.get('$controller')("SessionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'coongliApp:sessionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
