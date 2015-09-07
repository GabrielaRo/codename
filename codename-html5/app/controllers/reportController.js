(function () {
    var reportController = function ($scope) {
       $scope.pageClass = "report";
       $( window ).scrollTop( 0 );
    };

    reportController.$inject = ['$scope'];
    angular.module("codename").controller("privacyController", reportController);

}());


