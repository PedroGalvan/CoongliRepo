'use strict';

angular.module('coongliApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('resourcecategory', {
                parent: 'entity',
                url: '/resourcecategorys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Resourcecategorys'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/resourcecategory/resourcecategorys.html',
                        controller: 'ResourcecategoryController'
                    }
                },
                resolve: {
                }
            })
            .state('resourcecategory.detail', {
                parent: 'entity',
                url: '/resourcecategory/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Resourcecategory'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/resourcecategory/resourcecategory-detail.html',
                        controller: 'ResourcecategoryDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Resourcecategory', function($stateParams, Resourcecategory) {
                        return Resourcecategory.get({id : $stateParams.id});
                    }]
                }
            })
            .state('resourcecategory.new', {
                parent: 'resourcecategory',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/resourcecategory/resourcecategory-dialog.html',
                        controller: 'ResourcecategoryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    description: null,
                                    hidden: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('resourcecategory', null, { reload: true });
                    }, function() {
                        $state.go('resourcecategory');
                    })
                }]
            })
            .state('resourcecategory.edit', {
                parent: 'resourcecategory',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/resourcecategory/resourcecategory-dialog.html',
                        controller: 'ResourcecategoryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Resourcecategory', function(Resourcecategory) {
                                return Resourcecategory.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('resourcecategory', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('resourcecategory.delete', {
                parent: 'resourcecategory',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/resourcecategory/resourcecategory-delete-dialog.html',
                        controller: 'ResourcecategoryDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Resourcecategory', function(Resourcecategory) {
                                return Resourcecategory.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('resourcecategory', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
