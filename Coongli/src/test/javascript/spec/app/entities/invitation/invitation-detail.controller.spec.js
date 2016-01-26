'use strict';

describe('Controller Tests', function() {

    describe('Invitation Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockInvitation, MockUser, MockSession;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockInvitation = jasmine.createSpy('MockInvitation');
            MockUser = jasmine.createSpy('MockUser');
            MockSession = jasmine.createSpy('MockSession');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Invitation': MockInvitation,
                'User': MockUser,
                'Session': MockSession
            };
            createController = function() {
                $injector.get('$controller')("InvitationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'coongliApp:invitationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
