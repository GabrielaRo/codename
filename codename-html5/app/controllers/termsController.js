(function () {
    var termsController = function ($scope, $rootScope, $users, $auth, appConstants) {
       $scope.pageClass = "terms";
       $( window ).scrollTop( 0 );
    };

    termsController.$inject = ['$scope', '$rootScope', '$users', '$auth', 'appConstants'];
    angular.module("codename").controller("termsController", termsController);

}());


