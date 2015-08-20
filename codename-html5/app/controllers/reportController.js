(function () {
    var reportController = function ($scope, $rootScope, $users, $auth, appConstants) {
       $scope.pageClass = "report";
       $( window ).scrollTop( 0 );
    };

    reportController.$inject = ['$scope', '$rootScope', '$users', '$auth', 'appConstants'];
    angular.module("codename").controller("privacyController", reportController);

}());


