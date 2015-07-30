(function () {
    var publicProfileController = function ($rootScope, $scope, $timeout, $users, $sockets, $cookieStore, appConstants, $routeParams, $auth, location) {

        /*
         * For Loading we try to fetch everything at once instead of each different piece
         */
        $scope.params = $routeParams;
        $scope.edit = true;
        $scope.profile = {
            firstname: "",
            lastname: "",
            nickname: "",
            location: {description: ""},
            originallyFrom: "",
            bio: "",
            longbio: "",
            title: "",
            lookingFors: "",
            interests: "",
            iam: "",
            twitter: "",
            linkedin: "",
            website: "",
            percentage: 0,
            live: "false",
            hasavatar: "false",
            hascover: "false",
            avatarUrl: appConstants.server + appConstants.context + "rest/public/users/" + $scope.user_nick + "/avatar",
            coverUrl: appConstants.server + appConstants.context + "rest/public/users/" + $scope.user_nick + "/cover"
        };



        $scope.loadPublicUserData = function (nickname) {
            console.log("LOAD PUBLIC DATA USER: " + nickname);
            $users.getPublicUserData(nickname)
                    .success(function (data) {
                        console.log(data);

                        $scope.profile.userId = data.userId;
                        $scope.profile.firstname = data.firstname;
                        $scope.profile.lastname = data.lastname;
                        $scope.profile.nickname = data.nickname;
                        $scope.profile.location.description = data.location;
                        $scope.profile.originallyFrom = data.originallyFrom;
                        $scope.profile.bio = data.bio;
                        $scope.profile.longbio = data.longbio;
                        $scope.profile.title = data.title;
                        $scope.profile.lookingFor = data.lookingFor;
                        $scope.profile.interests = data.interests;
                        $scope.profile.iam = data.iams;
                        $scope.profile.website = data.website;
                        $scope.profile.advice = data.advice;
                        $scope.profile.messageme = data.messageme;
                        $scope.profile.share = data.share;
                        $scope.profile.hobbies = data.hobbies;
                        $scope.profile.resources = data.resources;
                        $scope.profile.hasavatar = data.hasavatar;
                        $scope.profile.hascover = data.hascover;
                        $scope.profile.avatarUrl = appConstants.server + appConstants.context + "rest/public/users/" + data.nickname + "/avatar",
                                $scope.profile.coverUrl = appConstants.server + appConstants.context + "rest/public/users/" + data.nickname + "/cover"
                        initialData = angular.copy($scope.profile)
                        // $scope.calculatePercentage();
                    }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with getting the user data" + data);
            });

        };

        // Does this browser support the FILEAPI ?
        $scope.fileReaderSupported = window.FileReader != null && (window.FileAPI == null || FileAPI.html5 != false);



        /*
         * This code is executed everytime that we access to the profile page
         */


        console.log("THe USER ID HERE IS: " + $scope.user_id);
        console.log("THe USER EMAIL HERE IS: " + $scope.email);


        if ($scope.params && $scope.params.nickname) {
            console.log("ROUTE PARAMS NIckname ");
            console.log($scope.params.nickname);

            $scope.loadPublicUserData($scope.params.nickname);

        }




    };

    publicProfileController.$inject = ["$rootScope", "$scope", "$timeout", "$users", "$sockets", "$cookieStore", "appConstants", "$routeParams", "$auth", "location"];
    angular.module("codename").controller("publicProfileController", publicProfileController);
}());