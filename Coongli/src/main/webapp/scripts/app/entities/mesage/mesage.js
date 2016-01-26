'use strict';

angular.module('coongliApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('mesage', {
                parent: 'entity',
                url: '/mesages',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Mesages'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/mesage/mesages.html',
                        controller: 'MesageController'
                    }
                },
                resolve: {
                }
            })
            .state('mesage.detail', {
                parent: 'entity',
                url: '/mesage/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Mesage'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/mesage/mesage-detail.html',
                        controller: 'MesageDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Mesage', function($stateParams, Mesage) {
                        return Mesage.get({id : $stateParams.id});
                    }]
                }
            })
            .state('mesage.new', {
                parent: 'mesage',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/mesage/mesage-dialog.html',
                        controller: 'MesageDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    subject: null,
                                    body: null,
                                    sentmoment: null,
                                    saw: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('mesage', null, { reload: true });
                    }, function() {
                        $state.go('mesage');
                    })
                }]
            })
            .state('mesage.edit', {
                parent: 'mesage',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/mesage/mesage-dialog.html',
                        controller: 'MesageDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Mesage', function(Mesage) {
                                return Mesage.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('mesage', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('mesage.delete', {
                parent: 'mesage',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/mesage/mesage-delete-dialog.html',
                        controller: 'MesageDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Mesage', function(Mesage) {
                                return Mesage.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('mesage', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
