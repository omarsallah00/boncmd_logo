(function() {
    'use strict';

    angular
        .module('boncmd8App')
        .controller('CategorieClientDialogController', CategorieClientDialogController);

    CategorieClientDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Categorie'];

    function CategorieClientDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Categorie) {
        var vm = this;

        vm.categorie = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.categorie.id !== null) {
                Categorie.update(vm.categorie, onSaveSuccess, onSaveError);
            } else {
                Categorie.save(vm.categorie, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('boncmd8App:categorieUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
