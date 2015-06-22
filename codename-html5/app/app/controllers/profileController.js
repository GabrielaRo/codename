(function () {
    var profileController = function ($rootScope, $scope, $upload, $timeout, $users, $cookieStore, appConstants) {
        /*
         * For Loading we try to fetch everything at once instead of each different piece
         */
        $scope.profile = {
            firstname: "",
            lastname: "",
            location: "",
            bio: "",
            longbio: "",
            title: "",
            lookingFors: "",
            interests: "",
            iam: "",
            percentage: 0,
            live: "false",
            avatarUrl: appConstants.server + appConstants.context + "rest/public/users/" + $scope.user_id + "/avatar",
            coverUrl: appConstants.server + appConstants.context + "rest/public/users/" + $scope.user_id + "/cover"
        };
        
        //ABOUT EDITABLE BLOCK
        $scope.aboutStatus = false;
        $scope.editAboutBlock = function () {
            
            $('#user-about-form input, #user-about-form textarea').removeAttr('disabled'); 
            $(".checkbox-label").removeClass("disabled");
            $scope.aboutStatus = true;
        }
        $scope.saveAboutBlock = function () {
            
            $('#user-about-form input, #user-about-form textarea').attr('disabled', 'disabled'); 
            $(".checkbox-label").addClass("disabled");
            $scope.updateBio($scope.profile.bio);
            $scope.updateLongBio($scope.profile.longbio);
            $scope.updateIams($scope.profile.iam);
            $scope.aboutStatus = false;
            
            
        }
        $scope.cancelAboutBlock = function () {
            
            $('#user-about-form input, #user-about-form textarea').attr('disabled', 'disabled'); 
            $(".checkbox-label").addClass("disabled");
            $scope.aboutStatus = false;
        }
        
        //
        
        $scope.lookingFors = [['Socialise', 'Socialise with other Fhellows'], ['Collaborate', 'Collaborate with other Fhellows'], ['Mentor', 'Mentor Fhellows']];
        $scope.iAms = [['Freelancer', 'Freelancer'], ['Entrepreneur', 'Entrepreneur'], ['Digital Nomad', 'Digital Nomad']];

        $scope.toggleIamSelection = function (iam) {
            
            var idx = $scope.profile.iam.indexOf(iam[0]);
            // is currently selected
            if (idx > -1) {
                $scope.profile.iam.splice(idx, 1);
            }

            // is newly selected
            else {
                $scope.profile.iam.push(iam[0]);
            }
            
           
        };

        $scope.toggleLookingForSelection = function (lookingFor) {
            
            var idx = $scope.profile.lookingFor.indexOf(lookingFor[0]);
            // is currently selected
            if (idx > -1) {
                $scope.profile.lookingFor.splice(idx, 1);
            }

            // is newly selected
            else {
                $scope.profile.lookingFor.push(lookingFor[0]);
            }
            
           
        };

        /*
         * This code loads all the profile user data from the server.
         *  We use initialData to store the information that we retrieved from the server when the method
         *  is called. So we can check if the user changed it at some point.
         */
        var initialData = "";
        $scope.loadUserData = function () {
            $users.getUserData()
                    .success(function (data) {

                        console.log("profile.userId: " + data.userId);
                        console.log("profile.firstname: " + data.firstname);
                        console.log("profile.lastname: " + data.lastname);
                        console.log("profile.location: " + data.location);
                        console.log("profile.originallyFrom: " + data.originallyFrom);
                        console.log("profile.bio: " + data.bio);
                        console.log("profile.longbio: " + data.longbio);
                        console.log("profile.title: " + data.title);
                        console.log("profile.lookingFor: " + data.lookingFor);
                        console.log("profile.interests: " + data.interests);
                        console.log("profile.imas: " + data.iams);
                        console.log("profile.percentage: " + data.percentage);
                        console.log("profile.live: " + data.live);
                        $scope.profile.userId = data.userId;
                        $scope.profile.firstname = (data.firstname != "undefined" ) ? data.firstname : "";
                        $scope.profile.lastname = (data.lastname != "undefined" ) ? data.lastname : "";
                        $scope.profile.location = (data.location != "undefined" ) ? data.location : "";
                        $scope.profile.originallyFrom = (data.originallyFrom != "undefined" ) ? data.originallyFrom : "";
                        $scope.profile.bio = data.bio;
                        $scope.profile.longbio = data.longbio;
                        $scope.profile.title = (data.title != "undefined") ? data.title : "";
                        $scope.profile.lookingFor = data.lookingFor;
                        $scope.profile.interests = data.interests;
                        $scope.profile.iam = data.iams;
                        $scope.profile.percentage = data.percentage;
                        initialData = angular.copy($scope.profile)

                    }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with getting the user data" + data);
            });

        };


        // Does this browser support the FILEAPI ?
        $scope.fileReaderSupported = window.FileReader != null && (window.FileAPI == null || FileAPI.html5 != false);

        /*
         * Code for Uploading the User Profile Avatar
         */
        $scope.uploadingAvatar = false;
        $scope.uploadAvatarPercentage = 0;
        $scope.uploadAvatarFile = function (files, event) {
            
            var file = files;
            
            $scope.upload = $users.uploadAvatar(file)
                    .progress(function (evt) {
                        $scope.uploadAvatarPercentage = parseInt(100.0 * evt.loaded / evt.total);
                        $scope.uploadingAvatar = true;
                        console.log('progress: ' + parseInt(100.0 * evt.loaded / evt.total) + '% file :' + evt.config.file.name);
                    }).success(function (data) {
                // file is uploaded successfully
                //console.log('file ' + file.name + 'is uploaded successfully. Response: ' + data);
                $scope.uploadAvatarPercentage = false;
                $scope.profile.avatarUrl = "";
                $scope.profile.avatarUrl = appConstants.server + appConstants.context + "rest/public/users/" + $scope.user_id + "/avatar" + '?' + new Date().getTime();
                $rootScope.$broadcast("updateUserImage");

            }).error(function (data) {
                console.log('file ' + file.name + ' upload error. Response: ' + data);
            });
        };

        /*
         * Code for Uploading the User Profile Cover
         */
        $scope.uploadingCover = false;
        $scope.uploadCoverPercentage = 0;
        $scope.uploadCoverFile = function (files, event) {
            
            var file = files;
            $scope.upload = $users.uploadCover(file)
                    .progress(function (evt) {
                        $scope.uploadCoverPercentage = parseInt(100.0 * evt.loaded / evt.total);
                        $scope.uploadingCover = true;
                        console.log('progress: ' + parseInt(100.0 * evt.loaded / evt.total) + '% file :' + evt.config.file.name);
                    }).success(function (data) {
                // file is uploaded successfully
                console.log('file ' + file.name + 'is uploaded successfully. Response: ' + data);
                $scope.uploadingCover = false;
                $scope.profile.coverUrl = "";
                $scope.profile.coverUrl = appConstants.server + appConstants.context + "rest/public/users/" + $scope.user_id + "/cover" + '?' + new Date().getTime();
                $rootScope.$broadcast("updateUserCover");

            }).error(function (data) {
                console.log('file ' + file.name + ' upload error. Response: ' + data);
            });
        };

        /*
         * This code generates a Thumbnail from a file uploaded to the browser only.
         * It does not send the data to the server
         */
        $scope.generateThumb = function (file) {
            if (file != null) {
                if ($scope.fileReaderSupported && file.type.indexOf('image') > -1) {
                    $timeout(function () {
                        var fileReader = new FileReader();
                        fileReader.readAsDataURL(file);
                        fileReader.onload = function (e) {
                            $timeout(function () {
                                
                                file.dataUrl = e.target.result;
                                $scope.profile.avatarUrl = e.target.result;
                                $scope.uploadAvatarFile(file);
                            });
                        }
                    });
                }
            }
        };
         $scope.generateCoverThumb = function (file) {
            if (file != null) {
                if ($scope.fileReaderSupported && file.type.indexOf('image') > -1) {
                    $timeout(function () {
                        var fileReader = new FileReader();
                        fileReader.readAsDataURL(file);
                        fileReader.onload = function (e) {
                            $timeout(function () {

                                file.dataUrl = e.target.result;
                                $scope.profile.coverUrl = e.target.result;
                                $scope.uploadCoverFile(file);
                            });
                        }
                    });
                }
            }
        };

        $scope.updateBothNames = function (firstname, lastname) {
            $users.updateBothNames(firstname, lastname).success(function (data) {
                $rootScope.$broadcast("quickNotification", "First & Last Name Updated Successfully");
            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with updating the first & last name!" + data);
            });

        };
        
        $scope.updateLookingFors = function (lookingFors) {
            console.log("lookingFors = "+lookingFors);
            $users.updateLookingFor(lookingFors).success(function (data) {
                $rootScope.$broadcast("quickNotification", "LookingFor Updated Successfully");
            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with updating the lookingFor values!" + data);
            });

        };
        
        $scope.updateIams = function (iams) {
            console.log("iams = "+iams);
            $users.updateIam(iams).success(function (data) {
                $rootScope.$broadcast("quickNotification", "I am Updated Successfully");
            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with updating the I am values!" + data);
            });

        };

        $scope.updateFirstName = function (firstname) {
            $users.updateFirstName(firstname).success(function (data) {
                $rootScope.$broadcast("quickNotification", "First Name Updated Successfully");
            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with updating the first name!" + data);
            });

        };

        $scope.updateLastName = function (lastname) {
            $users.updateLastName(lastname).success(function (data) {
                $rootScope.$broadcast("quickNotification", "Last Name Updated Successfully");
            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with updating the last name!" + data);
            });

        };
        
         $scope.updateOriginallyFrom = function (locationFrom) {
            $users.updateOriginallyFrom(locationFrom).success(function (data) {
                $rootScope.$broadcast("quickNotification", "Originally From Updated Successfully");
            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with updating the Originally From location!" + data);
            });

        };

        $scope.updateLocation = function (location) {
            $users.updateLocation(location).success(function (data) {
                $rootScope.$broadcast("quickNotification", "Location Updated Successfully");
            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with updating the location!" + data);
            });

        };

        $scope.updateBio = function (bio) {
            $users.updateBio(bio).success(function (data) {
                $rootScope.$broadcast("quickNotification", "Bio Updated Successfully");
            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with updating the bio!" + data);
            });

        };
        
         $scope.updateLongBio = function (longbio) {
            $users.updateLongBio(longbio).success(function (data) {
                $rootScope.$broadcast("quickNotification", "Long Bio Updated Successfully");
            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with updating the long bio!" + data);
            });

        };


        $scope.updateTitle = function (title) {
            $users.updateTitle(title).success(function (data) {
                $rootScope.$broadcast("quickNotification", "Title Updated Successfully");
            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with updating the bio!" + data);
            });
        };

        /*
         * This code updates on the server side if the user is accessing the site
         *  with his/her account for the first time. After this method gets called
         *  the user will be redirected to the home page, instead of the profile page.
         */
        $scope.updateUserFirstLogin = function () {
            $users.updateUserFirstLogin().success(function (data) {
                $cookieStore.put('firstLogin', false);
                $scope.loadUserData($scope.user_id, $scope.email, $scope.auth_token);
            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with updating the first login data!" + data);
            });

        };

        /*
         * This code is executed everytime that we access to the profile page
         */
        var firstLogin = $cookieStore.get('firstLogin');
        if (firstLogin) {
            // If it is the first time that the user is accessing the site using this account
            //  we need to update the information in the server and then load the basic data. 
            $scope.updateUserFirstLogin();
        } else {
            $scope.loadUserData($scope.user_id, $scope.email, $scope.auth_token);
        }
    };

    profileController.$inject = ["$rootScope", "$scope", "$upload", "$timeout", "$users", "$cookieStore", "appConstants"];
    angular.module("codename").controller("profileController", profileController);
}());