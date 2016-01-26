'use strict';

angular.module('coongliApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('adminis', {
                parent: 'entity',
                url: '/adminiss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Adminiss'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/adminis/adminiss.html',
                        controller: 'AdminisController'
                    }
                },
                resolve: {
                }
            })
            .state('adminis.detail', {
                parent: 'entity',
                url: '/adminis/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Adminis'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/adminis/adminis-detail.html',
                        controller: 'AdminisDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Adminis', function($stateParams, Adminis) {
                        return Adminis.get({id : $stateParams.id});
                    }]
                }
            })
            .state('adminis.new', {
                parent: 'adminis',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/adminis/adminis-dialog.html',
                        controller: 'AdminisDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('adminis', null, { reload: true });
                    }, function() {
                        $state.go('adminis');
                    })
                }]
            })
            .state('adminis.edit', {
                parent: 'adminis',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/adminis/adminis-dialog.html',
                        controller: 'AdminisDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Adminis', function(Adminis) {
                                return Adminis.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('adminis', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('adminis.delete', {
                parent: 'adminis',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/adminis/adminis-delete-dialog.html',
                        controller: 'AdminisDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Adminis', function(Adminis) {
                                return Adminis.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('adminis', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
