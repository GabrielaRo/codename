(function () {
    var feedbackController = function ($scope, $rootScope, $users, $auth, appConstants) {
       $scope.pageClass = "feedback";
 
    };

    feedbackController.$inject = ['$scope', '$rootScope', '$users', '$auth', 'appConstants'];
    angular.module("codename").controller("feedbackController", feedbackController);

}());


