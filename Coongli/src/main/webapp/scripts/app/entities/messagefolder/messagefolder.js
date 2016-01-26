'use strict';

angular.module('coongliApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('messagefolder', {
                parent: 'entity',
                url: '/messagefolders',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Messagefolders'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/messagefolder/messagefolders.html',
                        controller: 'MessagefolderController'
                    }
                },
                resolve: {
                }
            })
            .state('messagefolder.detail', {
                parent: 'entity',
                url: '/messagefolder/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Messagefolder'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/messagefolder/messagefolder-detail.html',
                        controller: 'MessagefolderDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Messagefolder', function($stateParams, Messagefolder) {
                        return Messagefolder.get({id : $stateParams.id});
                    }]
                }
            })
            .state('messagefolder.new', {
                parent: 'messagefolder',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/messagefolder/messagefolder-dialog.html',
                        controller: 'MessagefolderDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('messagefolder', null, { reload: true });
                    }, function() {
                        $state.go('messagefolder');
                    })
                }]
            })
            .state('messagefolder.edit', {
                parent: 'messagefolder',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/messagefolder/messagefolder-dialog.html',
                        controller: 'MessagefolderDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Messagefolder', function(Messagefolder) {
                                return Messagefolder.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('messagefolder', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('messagefolder.delete', {
                parent: 'messagefolder',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/messagefolder/messagefolder-delete-dialog.html',
                        controller: 'MessagefolderDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Messagefolder', function(Messagefolder) {
                                return Messagefolder.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('messagefolder', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
