(function () {
    var homeController = function ($scope, $rootScope, $users, $auth, appConstants) {
        $scope.imagePath = "static/img/public-images/";
        $scope.pageClass = "home full";
        $scope.formTabActive = "sign-in";

        $scope.joinStatus = "home";
        $scope.tabClicked = function (tab, tabsForm) {
            var form = $("#" + tabsForm);
            var formsTabs = form.find(".tab");
            var actualTab = $("#" + tab);
            if (tab == "sign-in") {
                $scope.formTabActive = tab;
            } else if (tab == "join") {
                $scope.formTabActive = tab;
            }

            formsTabs.each(function (index) {
                $(this).removeClass("active");
            });
            actualTab.addClass("active");
        }


        $scope.joinStatusChange = function (status) {
            if (status == 'home') {
                $scope.joinStatus = status;
            } else {
                $scope.joinStatus = status;
            }

        }

        $scope.initMockUsers = function () {
            $users.initMockUsers().success(function (data) {
                $rootScope.$broadcast("quickNotification", "Mock Users Created!");

            }).error(function (data) {
                $rootScope.$broadcast("quickNotification", "<i class='fa fa-exclamation-triangle'></i> Something failed: " + data, 'error');
                console.log("Error : " + data + "!");

            });

        }

        $scope.registerUser = function (isValid) {

            $scope.joinSubmitted = true;

            if (isValid) {
                $users.signup($scope.newUser.email, $scope.newUser.pass).success(function (data) {
                    $rootScope.$broadcast("quickNotification", "You are  now registered, please login!");

                    $rootScope.$broadcast("goTo", "/");
                    $scope.registerForm.$setPristine();

                    $scope.tabClicked("sign-in", 'home-tabs');
                    $scope.joinStatus = "home";


                }).error(function (data) {
                    $rootScope.$broadcast("quickNotification", " <i class='fa fa-exclamation-triangle'></i>Something failed: " + data.error, 'error');
                    console.log("Error : " + data.error + "!");

                });
            } else {
                console.log("Error : Invalid Form");
            }
        };
        




    };

    homeController.$inject = ['$scope', '$rootScope', '$users', '$auth', 'appConstants'];
    angular.module("codename").controller("homeController", homeController);

}());


