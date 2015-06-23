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
            hasavatar: "false",
            hascover: "false",
            avatarUrl: appConstants.server + appConstants.context + "rest/public/users/" + $scope.user_id + "/avatar",
            coverUrl: appConstants.server + appConstants.context + "rest/public/users/" + $scope.user_id + "/cover"
        };

        //ABOUT EDITABLE BLOCK
        $scope.aboutStatus = false;
        $scope.editAboutBlock = function () {

            $scope.editContentBlock($("#user-about-form"));
            $scope.aboutStatus = true;
        }
        $scope.saveAboutBlock = function () {

            $scope.disableContentBlock($("#user-about-form"));
            $scope.updateBio($scope.profile.bio);
            $scope.updateLongBio($scope.profile.longbio);
            $scope.updateIams($scope.profile.iam);
            $scope.aboutStatus = false;


        }
        $scope.cancelAboutBlock = function () {
            $scope.disableContentBlock($("#user-about-form"));
            $scope.aboutStatus = false;
            
            $scope.profile.bio = angular.copy(initialData.bio);
            $scope.profile.longbio = angular.copy(initialData.longbio);
             $scope.profile.iam = angular.copy(initialData.iam);
            
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

                        console.log("profile.live: " + data.live);
                        console.log("profile.hascover: " + data.hascover);
                        console.log("profile.hasavatar: " + data.hasavatar);
                        $scope.profile.userId = data.userId;
                        $scope.profile.firstname =  data.firstname ;
                        $scope.profile.lastname = data.lastname ;
                        $scope.profile.location = data.location ;
                        $scope.profile.originallyFrom =  data.originallyFrom ;
                        $scope.profile.bio = data.bio;
                        $scope.profile.longbio = data.longbio;
                        $scope.profile.title =  data.title ;
                        $scope.profile.lookingFor = data.lookingFor;
                        $scope.profile.interests = data.interests;
                        $scope.profile.iam = data.iams;

                        $scope.profile.live = data.live;
                        $scope.profile.hasavatar = data.hasavatar;
                        $scope.profile.hascover = data.hascover;
                        initialData = angular.copy($scope.profile)
                        $scope.calculatePercentage();
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
                $scope.profile.hasavatar = true;
                $scope.calculatePercentage();
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
                $scope.profile.hascover = true;
                $scope.calculatePercentage();
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
            $scope.clearEditablesActive();
            $scope.clearField($("#user-name-form"));
            $users.updateBothNames(firstname, lastname).success(function (data) {
                $scope.profile.firstname = firstname;
                $scope.profile.lastname = lastname;
                $rootScope.$broadcast("quickNotification", "First & Last Name Updated Successfully");
                $scope.calculatePercentage();
            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with updating the first & last name!" + data);
            });

        };

        $scope.updateLookingFors = function (lookingFors) {
            $scope.clearEditablesActive();
            console.log("lookingFors = " + lookingFors);

            $users.updateLookingFor(lookingFors).success(function (data) {
              
                $rootScope.$broadcast("quickNotification", "LookingFor Updated Successfully");
            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with updating the lookingFor values!" + data);
            });

        };

        $scope.updateIams = function (iams) {
            console.log("iams = " + iams);
            $users.updateIam(iams).success(function (data) {
                $rootScope.$broadcast("quickNotification", "I am Updated Successfully");
                $scope.calculatePercentage();
            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with updating the I am values!" + data);
            });

        };

        $scope.updateFirstName = function (firstname) {
            $users.updateFirstName(firstname).success(function (data) {
                $rootScope.$broadcast("quickNotification", "First Name Updated Successfully");
                $scope.calculatePercentage();
            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with updating the first name!" + data);
            });

        };

        $scope.updateLastName = function (lastname) {
            $users.updateLastName(lastname).success(function (data) {
                $rootScope.$broadcast("quickNotification", "Last Name Updated Successfully");
                $scope.calculatePercentage();
            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with updating the last name!" + data);
            });

        };

        $scope.updateOriginallyFrom = function (originallyFrom) {
            
            $scope.clearEditablesActive();
            $scope.clearField($("#user-originally-from-form"));
            
            $users.updateOriginallyFrom(originallyFrom).success(function (data) {
                $scope.profile.originallyFrom = originallyFrom;
                $scope.calculatePercentage();
                $rootScope.$broadcast("quickNotification", "Originally From Updated Successfully");
            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with updating the Originally From location!" + data);
            });

        };

        $scope.updateLocation = function (location) {
            
            $scope.clearEditablesActive();
            $scope.clearField($("#user-location-form"));
            
            $users.updateLocation(location).success(function (data) {
                $scope.profile.location = location;
                $scope.calculatePercentage();
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
                $scope.calculatePercentage();
            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with updating the long bio!" + data);
            });

        };


        $scope.updateTitle = function (title) {
            $scope.clearEditablesActive();
            $scope.clearField($("#user-job-form"));
            
            $users.updateTitle(title).success(function (data) {
                $scope.profile.title = title;
                $scope.calculatePercentage();
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

        $scope.calculatePercentage = function () {
            $scope.profile.percentage = 0;
            if ($scope.profile.hascover == true) {
                console.log("> + 5 hascover");
                $scope.profile.percentage += 5;
            }
            if ($scope.profile.hasavatar == true) {
                console.log("> + 5 hasavatar");
                $scope.profile.percentage += 5;
            }
            if ($scope.profile.firstname != "undefined" && $scope.profile.firstname != "" &&
                    $scope.profile.lastname != "undefined" && $scope.profile.lastname != "") {
                $scope.profile.percentage += 10;
                console.log("> + 10 firstname/lastname");
            }
            if ($scope.profile.title != "undefined" && $scope.profile.title != "") {
                $scope.profile.percentage += 10;
                console.log("> + 10 title = "+ $scope.profile.title);
            }
            if ($scope.profile.location != "undefined" && $scope.profile.location != "") {
                $scope.profile.percentage += 10;
                console.log("> + 10 location");
            }

            if ($scope.profile.originallyFrom != "undefined" && $scope.profile.originallyFrom != "") {
                $scope.profile.percentage += 10;
                console.log("> + 10 originallyFrom");
            }
            if ($scope.profile.bio != "undefined" && $scope.profile.bio != "") {
                $scope.profile.percentage += 5;
                console.log("> + 5 bio");
            }
            if ($scope.profile.longbio != "undefined" && $scope.profile.longbio != "") {
                $scope.profile.percentage += 5;
                console.log("> + 5 longbio");
            }
            if ($scope.profile.interests != "undefined" && $scope.profile.interests != "") {
                $scope.profile.percentage += 10;
                console.log("> + 10 interests");
            }
            if ($scope.profile.lookingFor != "undefined" && $scope.profile.lookingFor != "") {
                $scope.profile.percentage += 10;
                console.log("> + 10 lookingfor = "+$scope.profile.lookingFor);
            }
            if ($scope.profile.iam != "undefined" && $scope.profile.iam != "") {
                $scope.profile.percentage += 10;
                console.log("> + 10 iam");
            }
            console.log("The percentage is : " + $scope.profile.percentage);

        };

        $scope.updateUserLiveProfile = function (live) {
            console.log("Profile Live? " + !live)
            $users.updateUserLiveProfile(!live).success(function (data) {
                $scope.profile.live = !live;
            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with updating the first login data!" + data);
            });

        };
        
        /* 
         * Manage float forms and editable content blocks
         */
        
        
        $scope.bindEditableEvents = function () {
            var editables = $(".editable")
            editables.each(function(index){
                var element = $(this);
                element.bind("click", $scope.clickOnEditable);
            });
        }
        
        $scope.clickOnEditable = function (event) {
            
            if(event.target.className.indexOf("editable") > -1){
                $scope.clearEditablesActive();
                var target = $(event.currentTarget);
                target.addClass("editable-active");
            }
        }
        
         $scope.clearEditablesActive = function(){
            var editablesActive = $(".editable-active")
            editablesActive.each(function(index){
                var element = $(this);
                element.removeClass("editable-active");
            });
         };
        
         $scope.closeEditables = function(event){
            event.stopPropagation();
            $scope.clearEditablesActive();
            
            var target = $(event.currentTarget);
            var currentform = target.closest(".floatform");
             
            var formFields =  currentform.find("input");
            formFields.each(function(index){
                 $(this).val('');
            }); 
         };
        
        $scope.clearField = function(form){
             var formFields = form.find("input");
            formFields.each(function(index){
                 $(this).val('');
            }); 
        };
        
        $scope.initiateContentBlocks = function () {
            var blockContents = $(".content-block-content");
            blockContents.each(function(index){
            
                var textareas = $(this).find("textarea");
                var checkboxes = $(this).find(".checkboxes").children().attr('disabled','disabled');

                textareas.each(function(index){
                    $(this).attr('disabled','disabled');
                });
            });
        };
        
        $scope.editContentBlock = function(block){
            block.find("textarea").removeAttr('disabled');
            block.find(".checkbox-label").removeClass('disabled');
            block.find(".checkbox-label input").removeAttr('disabled');
        }
        
        $scope.disableContentBlock = function(block){
            block.find("textarea").attr('disabled','disabled');
            block.find(".checkbox-label").addClass('disabled');
            block.find(".checkbox-label input").attr('disabled','disabled');
        }

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
        $scope.bindEditableEvents();
        $scope.initiateContentBlocks();
    };

    profileController.$inject = ["$rootScope", "$scope", "$upload", "$timeout", "$users", "$cookieStore", "appConstants"];
    angular.module("codename").controller("profileController", profileController);
}());