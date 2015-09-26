(function () {
    var publicProfileController = function ($rootScope, $scope, $users, appConstants, $routeParams, $error) {

        /*
         * For Loading we try to fetch everything at once instead of each different piece
         */

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
            avatarUrl: appConstants.server + appConstants.context + "rest/public/users/" + $rootScope.user_nick + "/avatar?size=600",
            coverUrl: appConstants.server + appConstants.context + "rest/public/users/" + $rootScope.user_nick + "/cover"
        };

        $scope.newConversation = function (selectedUser, firstname, lastname) {
            $rootScope.$broadcast('goTo', "/messages/" + selectedUser + "/" + firstname + "/" + lastname);

        };

        $scope.editProfile = function () {
            $rootScope.$broadcast('goTo', "/profile");
        }

        $scope.loadPublicUserData = function (nickname) {
            $users.getPublicUserData(nickname)
                    .success(function (data) {

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
                        $scope.profile.twitter = data.twitter;
                        $scope.profile.linkedin = data.linkedin;
                        $scope.profile.messageme = data.messageme;
                        $scope.profile.share = data.share;
                        $scope.profile.hasavatar = data.hasavatar;
                        $scope.profile.hascover = data.hascover;
                        $scope.profile.avatarUrl = appConstants.server + appConstants.context + "rest/public/users/" + data.nickname + "/avatar?size=600",
                                $scope.profile.coverUrl = appConstants.server + appConstants.context + "rest/public/users/" + data.nickname + "/cover"
                        initialData = angular.copy($scope.profile)
                        // $scope.calculatePercentage();
                    })
                    .error(function (data, status) {
                        $error.handleError(data, status);
                    });

        };

        // Does this browser support the FILEAPI ?
        $scope.fileReaderSupported = window.FileReader != null && (window.FileAPI == null || FileAPI.html5 != false);



        /*
         * This code is executed everytime that we access to the profile page
         */



        if ($routeParams && $routeParams.nickname) {

            $scope.loadPublicUserData($routeParams.nickname);

        }




    };

    publicProfileController.$inject = ["$rootScope", "$scope", "$users", "appConstants", '$routeParams', "$error"];
    angular.module("codename").controller("publicProfileController", publicProfileController);
}());