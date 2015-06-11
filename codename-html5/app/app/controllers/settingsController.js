(function(){
    var settingsController = function ($rootScope, $scope, $upload, $timeout, $users, appConstants) {
         
        $scope.settings = {
            firstname: "",
            lastname: "",
            location: "",
            bio: "",
            profession: "",
            interests: {},
            avatarUrl: appConstants.server + appConstants.context +"rest/public/users/" + $scope.user_id + "/avatar",
            coverUrl:  appConstants.server + appConstants.context +"rest/public/users/" + $scope.user_id + "/cover"
        };
        
        $scope.uploadingAvatar = false;
        $scope.uploadAvatarPercentage = 0;
        $scope.uploadingCover = false;
        $scope.uploadCoverPercentage = 0;

        $scope.fileReaderSupported = window.FileReader != null && (window.FileAPI == null || FileAPI.html5 != false);

        console.log("AVATAR URL " + appConstants.server + appConstants.context + $scope.settings.avatarUrl);

        $scope.uploadAvatarFile = function (files, event) {
            console.log("Files : " + files + "-- event: " + event);
            var file = files[0];
            $scope.upload = $users.uploadAvatar(file)
            .progress(function (evt) {
                $scope.uploadAvatarPercentage = parseInt(100.0 * evt.loaded / evt.total);
                $scope.uploadingAvatar = true;
                console.log('progress: ' + parseInt(100.0 * evt.loaded / evt.total) + '% file :' + evt.config.file.name);
            }).success(function (data) {
                // file is uploaded successfully
                console.log('file ' + file.name + 'is uploaded successfully. Response: ' + data);
                $scope.uploadAvatarPercentage = false;
                $scope.settings.avatarUrl = "";
                $scope.settings.avatarUrl = appConstants.server + appConstants.context + "rest/public/users/" + $scope.user_id + "/avatar" + '?' + new Date().getTime();
                $rootScope.$broadcast("updateProfileImage");

            }).error(function (data) {
                console.log('file ' + file.name + ' upload error. Response: ' + data);
            });
        };
        
        $scope.uploadCoverFile = function (files, event) {
            console.log("Files : " + files + "-- event: " + event);
            var file = files[0];
            $scope.upload = $users.uploadCover(file)
            .progress(function (evt) {
                $scope.uploadCoverPercentage = parseInt(100.0 * evt.loaded / evt.total);
                $scope.uploadingCover = true;
                console.log('progress: ' + parseInt(100.0 * evt.loaded / evt.total) + '% file :' + evt.config.file.name);
            }).success(function (data) {
                // file is uploaded successfully
                console.log('file ' + file.name + 'is uploaded successfully. Response: ' + data);
                $scope.uploadingCover = false;
                $scope.settings.coverUrl = "";
                $scope.settings.coverUrl = appConstants.server + appConstants.context + "rest/public/users/" + $scope.user_id + "/cover" + '?' + new Date().getTime();
                $rootScope.$broadcast("updateProfileImage");

            }).error(function (data) {
                console.log('file ' + file.name + ' upload error. Response: ' + data);
            });
        };

        $scope.generateThumb = function (file) {
            console.log(file);
            if (file != null) {
                if ($scope.fileReaderSupported && file.type.indexOf('image') > -1) {
                    console.log("file oh yeah");
                    $timeout(function () {
                        var fileReader = new FileReader();
                        fileReader.readAsDataURL(file);
                        fileReader.onload = function (e) {
                            $timeout(function () {
                                console.log("file oh yeah 2: " + e.target.result);
                                file.dataUrl = e.target.result;
                            });
                        }
                    });
                }
            }
        };


        var initialData = "";

        $scope.loadProfile = function () {
            //console.log("Loading profile for user " + user_id + " with email: " + email + " and auth_token: " + auth_token);
            $users.getProfile()
            .success(function (data) {
               // $rootScope.$broadcast("quickNotification", "Loading your settings...!");
                console.log("firstname = " + data.firstname);
                console.log("lastname = " + data.lastname);
                console.log("userId = " + data.userId);
                console.log("location = " + data.location);
                console.log("bio = " + data.bio);
                console.log("profession = " + data.profession);
                $scope.settings.userId = data.userId;
                $scope.settings.firstname = data.firstname;
                $scope.settings.lastname = data.lasttname;
                $scope.settings.location = data.location;
                $scope.settings.bio = data.bio;
                $scope.settings.profession = data.profession;
                $scope.settings.interests = data.interests;
                initialData = angular.copy($scope.settings)

            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong!" + data);
            });

        };

        $scope.save = function (isValid, files, coverFiles, event) {

            console.log("save-changes");
            if (isValid) {
                $users.updateProfile($scope.settings.firstname, $scope.settings.lastname, $scope.settings.location, $scope.settings.bio, $scope.settings.profession, $scope.settings.interests  )
                .success(function (data) {
                    //$rootScope.$broadcast("quickNotification", "Your settings are now updated!");

                    initialData = angular.copy($scope.settings)
                    if(files != undefined){
                        $scope.uploadAvatarFile(files, event);
                    }
                    if(coverFiles != undefined){
                        $scope.uploadCoverFile(coverFiles, event);
                    }
                    $scope.settingsForm.$setPristine();
                }).error(function (data) {
                    console.log("Error: " + data.error);
                    $rootScope.$broadcast("quickNotification", "Settings not saved because:" + data);
                });
            } else {
                alert("form not valid");
            }

        };

        $scope.cancel = function () {
            console.log("cancel-changes");
            console.log("initial data firstname: " + initialData.firstname);
            console.log("initial data lastname: " + initialData.lastname);
            console.log("initial data bio: " + initialData.bio);
            console.log("initial data location: " + initialData.location);
            console.log("initial data profession: " + initialData.profession);
            console.log("initial data interests: " + initialData.interests);
            $scope.settings = angular.copy(initialData);
            $scope.settingsForm.$setPristine();
        };

        $scope.loadProfile($scope.user_id, $scope.email, $scope.auth_token);
        
    };
    
    settingsController.$inject = ['$rootScope', '$scope','$upload','$timeout', '$users', 'appConstants'];
    angular.module( "codename" ).controller("settingsController", settingsController);

}());
