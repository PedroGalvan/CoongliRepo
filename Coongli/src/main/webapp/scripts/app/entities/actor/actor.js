'use strict';

angular.module('coongliApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('actor', {
                parent: 'entity',
                url: '/actors',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Actors'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/actor/actors.html',
                        controller: 'ActorController'
                    }
                },
                resolve: {
                }
            })
            .state('actor.detail', {
                parent: 'entity',
                url: '/actor/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Actor'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/actor/actor-detail.html',
                        controller: 'ActorDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Actor', function($stateParams, Actor) {
                        return Actor.get({id : $stateParams.id});
                    }]
                }
            })
            .state('actor.new', {
                parent: 'actor',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/actor/actor-dialog.html',
                        controller: 'ActorDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    surname: null,
                                    email: null,
                                    phone: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('actor', null, { reload: true });
                    }, function() {
                        $state.go('actor');
                    })
                }]
            })
            .state('actor.edit', {
                parent: 'actor',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/actor/actor-dialog.html',
                        controller: 'ActorDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Actor', function(Actor) {
                                return Actor.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('actor', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('actor.delete', {
                parent: 'actor',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/actor/actor-delete-dialog.html',
                        controller: 'ActorDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Actor', function(Actor) {
                                return Actor.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('actor', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
