'use strict';

angular.module('coongliApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('session', {
                parent: 'entity',
                url: '/sessions',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Sessions'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/session/sessions.html',
                        controller: 'SessionController'
                    }
                },
                resolve: {
                }
            })
            .state('session.detail', {
                parent: 'entity',
                url: '/session/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Session'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/session/session-detail.html',
                        controller: 'SessionDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Session', function($stateParams, Session) {
                        return Session.get({id : $stateParams.id});
                    }]
                }
            })
            .state('session.new', {
                parent: 'session',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/session/session-dialog.html',
                        controller: 'SessionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    startmoment: null,
                                    endmoment: null,
                                    periodica: null,
                                    hidden: null,
                                    accepted: null,
                                    cancelled: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('session', null, { reload: true });
                    }, function() {
                        $state.go('session');
                    })
                }]
            })
            .state('session.edit', {
                parent: 'session',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/session/session-dialog.html',
                        controller: 'SessionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Session', function(Session) {
                                return Session.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('session', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('session.delete', {
                parent: 'session',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/session/session-delete-dialog.html',
                        controller: 'SessionDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Session', function(Session) {
                                return Session.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('session', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
