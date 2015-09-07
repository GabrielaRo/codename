(function () {
    var termsController = function ($scope) {
        $scope.pageClass = "terms";
        $(window).scrollTop(0);
    };

    termsController.$inject = ['$scope'];
    angular.module("codename").controller("termsController", termsController);

}());


