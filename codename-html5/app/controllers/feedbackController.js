(function () {
    var feedbackController = function ($scope, $rootScope, $users, $auth, appConstants) {
       $scope.pageClass = "feedback";
       $( window ).scrollTop( 0 );
    };

    feedbackController.$inject = ['$scope', '$rootScope', '$users', '$auth', 'appConstants'];
    angular.module("codename").controller("feedbackController", feedbackController);

}());


