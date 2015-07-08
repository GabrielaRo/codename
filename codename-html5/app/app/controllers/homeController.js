(function () {
    var homeController = function ($scope, $rootScope, $users, $auth, appConstants) {
        $scope.imagePath = "static/img/public-images/";
        $scope.pageClass = "home full";
        $scope.formTabActive = "sign-in";

        $scope.joinStatus = "home";
        $scope.tabClicked = function (tab, tabsForm) {
            var form = $("#" + tabsForm);
            console.log(form);
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
                $rootScope.$broadcast("quickNotification", "Mock Users");




            }).error(function (data) {
                $rootScope.$broadcast("quickNotification", "Something failed: " + data.error, 'error');
                console.log("Error : " + data.error + "!");

            });

        }

        $scope.registerUser = function (isValid) {
            // console.log("asd " + $scope.newUser.email + " / " + $scope.newUser.pass);

            $scope.joinSubmitted = true;

            if (isValid) {
                $users.signup($scope.newUser.email, $scope.newUser.pass).success(function (data) {
                    $rootScope.$broadcast("quickNotification", "You are  now registered, please login!");

                    $rootScope.$broadcast("goTo", "/");
                    $scope.registerForm.$setPristine();
                    console.log("Welcome to " + $scope.newUser.email + "!");

                    $scope.tabClicked("sign-in", 'home-tabs');
                    $scope.joinStatus = "home";


                }).error(function (data) {
                    $rootScope.$broadcast("quickNotification", "Something failed: " + data.error, 'error');
                    console.log("Error : " + data.error + "!");

                });
            } else {
                console.log("Invalid Form");
            }
        };
        
//        if ($scope.auth_token && $scope.auth_token !== "") {
//
//            console.log("loading private clubs because: " + $scope.auth_token);
//
//            //
//        } else {
//            console.log("loading public clubs because: " + $scope.auth_token);
//
//
//        }




    };

    homeController.$inject = ['$scope', '$rootScope', '$users', '$auth', 'appConstants'];
    angular.module("codename").controller("homeController", homeController);

}());


