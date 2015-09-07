(function () {

    var footerController = function ($scope, $location) {


        $scope.isActive = function (route) {
            return route === $location.path();
        };


    };

    footerController.$inject = ['$scope', '$location' ];
    angular.module("codename").controller("footerController", footerController);

}());
