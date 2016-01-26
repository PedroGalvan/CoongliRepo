'use strict';

angular.module('coongliApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('textable', {
                parent: 'entity',
                url: '/textables',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Textables'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/textable/textables.html',
                        controller: 'TextableController'
                    }
                },
                resolve: {
                }
            })
            .state('textable.detail', {
                parent: 'entity',
                url: '/textable/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Textable'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/textable/textable-detail.html',
                        controller: 'TextableDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Textable', function($stateParams, Textable) {
                        return Textable.get({id : $stateParams.id});
                    }]
                }
            })
            .state('textable.new', {
                parent: 'textable',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/textable/textable-dialog.html',
                        controller: 'TextableDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    text: null,
                                    creationMoment: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('textable', null, { reload: true });
                    }, function() {
                        $state.go('textable');
                    })
                }]
            })
            .state('textable.edit', {
                parent: 'textable',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/textable/textable-dialog.html',
                        controller: 'TextableDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Textable', function(Textable) {
                                return Textable.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('textable', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('textable.delete', {
                parent: 'textable',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/textable/textable-delete-dialog.html',
                        controller: 'TextableDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Textable', function(Textable) {
                                return Textable.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('textable', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
