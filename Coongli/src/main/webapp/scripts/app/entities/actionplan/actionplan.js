'use strict';

angular.module('coongliApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('actionplan', {
                parent: 'entity',
                url: '/actionplans',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Actionplans'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/actionplan/actionplans.html',
                        controller: 'ActionplanController'
                    }
                },
                resolve: {
                }
            })
            .state('actionplan.detail', {
                parent: 'entity',
                url: '/actionplan/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Actionplan'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/actionplan/actionplan-detail.html',
                        controller: 'ActionplanDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Actionplan', function($stateParams, Actionplan) {
                        return Actionplan.get({id : $stateParams.id});
                    }]
                }
            })
            .state('actionplan.new', {
                parent: 'actionplan',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/actionplan/actionplan-dialog.html',
                        controller: 'ActionplanDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('actionplan', null, { reload: true });
                    }, function() {
                        $state.go('actionplan');
                    })
                }]
            })
            .state('actionplan.edit', {
                parent: 'actionplan',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/actionplan/actionplan-dialog.html',
                        controller: 'ActionplanDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Actionplan', function(Actionplan) {
                                return Actionplan.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('actionplan', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('actionplan.delete', {
                parent: 'actionplan',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/actionplan/actionplan-delete-dialog.html',
                        controller: 'ActionplanDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Actionplan', function(Actionplan) {
                                return Actionplan.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('actionplan', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
