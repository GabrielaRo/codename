(function () {
    var contactController = function ($scope, $rootScope, $users, $auth, appConstants) {
       $scope.pageClass = "contact";
       $( window ).scrollTop( 0 );
    };

    contactController.$inject = ['$scope', '$rootScope', '$users', '$auth', 'appConstants'];
    angular.module("codename").controller("contactController", contactController);

}());


