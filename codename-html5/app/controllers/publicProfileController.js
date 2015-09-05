(function () {
    var publicProfileController = function ($rootScope, $scope, $timeout, $users, $sockets, $cookieStore, appConstants, $routeParams, $auth, location, $chat) {

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
            avatarUrl: appConstants.server + appConstants.context + "rest/public/users/" + $scope.user_nick + "/avatar?size=600",
            coverUrl: appConstants.server + appConstants.context + "rest/public/users/" + $scope.user_nick + "/cover"
        };
        
        $scope.newConversation = function (selectedUser) {
            
            $chat.newConversation(selectedUser).success(function (data) {
                $rootScope.$broadcast('goTo', "/messages/"+data);

            }).error(function (data) {
                console.log("Error: ");
                $rootScope.$broadcast("quickNotification", "Something went wrong creating a new conversations!" + data);
            });

        }

        $scope.editProfile = function(){
            $rootScope.$broadcast('goTo', "/profile");
        }

        $scope.loadPublicUserData = function (nickname) {
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



        if ($routeParams && $routeParams.nickname) {
            console.log($routeParams.nickname);

            $scope.loadPublicUserData($routeParams.nickname);

        }




    };

    publicProfileController.$inject = ["$rootScope", "$scope", "$timeout", "$users", "$sockets", "$cookieStore", "appConstants", "$routeParams", "$auth", "location", '$chat'];
    angular.module("codename").controller("publicProfileController", publicProfileController);
}());