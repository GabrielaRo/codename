(function () {
    var privacyController = function ($scope) {
       $scope.pageClass = "privacy";
 
    };

    privacyController.$inject = ['$scope'];
    angular.module("codename").controller("privacyController", privacyController);

}());


