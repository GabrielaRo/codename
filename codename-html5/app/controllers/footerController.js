(function () {

    var footerController = function ($scope, $location, $rootScope) {


        $scope.isActive = function (route) {
            return route === $location.path();
        };


    };

    footerController.$inject = ['$scope', '$location', '$rootScope', ];
    angular.module("codename").controller("footerController", footerController);

}());
