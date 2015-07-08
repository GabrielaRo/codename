(function () {
    var profileController = function ($rootScope, $scope, $timeout, $users, $sockets, $cookieStore, appConstants, $routeParams, $auth) {


       
        

        /*
         * For Loading we try to fetch everything at once instead of each different piece
         */
        $scope.params = $routeParams;
        $scope.edit = true;
        $scope.profile = {
            firstname: "",
            lastname: "",
            nickname: "",
            location: "",
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
            avatarUrl: appConstants.server + appConstants.context + "rest/public/users/" + $scope.user_id + "/avatar",
            coverUrl: appConstants.server + appConstants.context + "rest/public/users/" + $scope.user_id + "/cover"
        };


        /*
         * This code loads all the profile user data from the server.
         *  We use initialData to store the information that we retrieved from the server when the method
         *  is called. So we can check if the user changed it at some point.
         */

        $scope.loadUserData = function () {
            $users.getUserData()
                    .success(function (data) {
                        $scope.profile.userId = data.userId;
                        $scope.profile.firstname = data.firstname;
                        $scope.profile.lastname = data.lastname;
                        $scope.profile.nickname = data.nickname;
                        $scope.profile.location = data.location;
                        $scope.profile.originallyFrom = data.originallyFrom;
                        $scope.profile.bio = data.bio;
                        $scope.profile.longbio = data.longbio;
                        $scope.profile.title = data.title;
                        $scope.profile.lookingFor = data.lookingFor;
                        $scope.profile.interests = data.interests;
                        $scope.profile.iam = data.iams;
                        $scope.profile.website = data.website;
                        $scope.profile.linkedin = data.linkedin;
                        $scope.profile.twitter = data.twitter;
                        $scope.profile.live = data.live;
                        $scope.profile.hasavatar = data.hasavatar;
                        $scope.profile.hascover = data.hascover;
                        $scope.profile.avatarUrl = appConstants.server + appConstants.context + "rest/public/users/" + data.userId + "/avatar",
                                $scope.profile.coverUrl = appConstants.server + appConstants.context + "rest/public/users/" + data.userId + "/cover"
                        initialData = angular.copy($scope.profile)
                        $scope.calculatePercentage();
                    }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with getting the user data" + data);
            });

        };


        

        var initialData = "";

        $scope.resetData = function () {
            $scope.profile = angular.copy(initialData);
            $scope.calculatePercentage();
        }

        //ABOUT EDITABLE BLOCK
        $scope.aboutStatus = false;
        $scope.editAboutBlock = function () {

            $scope.editContentBlock($("#user-about-form"));
            $scope.aboutStatus = true;
        }
        $scope.saveAboutBlock = function (iam, bio, longbio) {

            $scope.disableContentBlock($("#user-about-form"));
            $scope.updateBioLongBioIams(bio, longbio, iam);

            $scope.aboutStatus = false;


        }
        $scope.cancelAboutBlock = function () {
            $scope.disableContentBlock($("#user-about-form"));
            $scope.aboutStatus = false;

            $scope.resetData();

        }

        //INTEREST EDITABLE BLOCK
        $scope.interestStatus = false;
        $scope.editInterestBlock = function () {

            // $scope.editContentBlock($("#user-about-form"));
            $scope.interestStatus = true;
        }
        $scope.saveInterestBlock = function (interests) {

            // $scope.disableContentBlock($("#user-interests-form"));
            $scope.updateInterests(interests);

            $scope.interestStatus = false;


        }
        $scope.cancelInterestBlock = function () {
            //$scope.disableContentBlock($("#user-about-form"));
            $scope.interestStatus = false;
            $scope.resetData();

        }

        $scope.updateInterests = function (interests) {
            console.log("interests = " + interests);
            if (typeof (interests) != "undefined"
                    && interests != initialData.interests) {
                $users.updateInterests(interests).success(function (data) {
                    $scope.profile.interests = interests;
                    initialData.interests = interests;
                    $rootScope.$broadcast("quickNotification", "Interests Updated Successfully");

                    $scope.calculatePercentage();
                }).error(function (data) {
                    console.log("Error: " + data);
                    $rootScope.$broadcast("quickNotification", "Something went wrong with updating the Interests values!" + data);
                });
            } else {
                $scope.resetData();
            }

        };
        //

        $scope.lookingFors = [['Socialise', 'Socialise with other Fhellows'], ['Collaborate', 'Collaborate with other Fhellows'], ['Mentor', 'Mentor Fhellows']];
        $scope.iAms = [['Freelancer', 'Freelancer'], ['Entrepreneur', 'Entrepreneur'], ['Digital Nomad', 'Digital Nomad']];
        $scope.interestsList = ['Tech', 'Design', 'Journalism', 'Photography', 'Fashion', 'Gaming', 'Virtual Reality', 'Software', 'Education', 'Startups', 'Business', 'Blogging', 'Music', 'Sports'];

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

            $scope.calculatePercentage();
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
            $scope.calculatePercentage();

        };

        $scope.toggleInterstsSelection = function (interest) {
            var idx = $scope.profile.interests.indexOf(interest);
            // is currently selected
            if (idx > -1) {
                $scope.profile.interests.splice(idx, 1);
            }

            // is newly selected
            else {
                $scope.profile.interests.push(interest);
            }

            $scope.calculatePercentage();
        };




        $scope.loadPublicUserData = function (userId) {
            $users.getPublicUserData(userId)
                    .success(function (data) {


                        $scope.profile.userId = data.userId;
                        $scope.profile.firstname = data.firstname;
                        $scope.profile.lastname = data.lastname;
                        $scope.profile.nickname = data.lastname;
                        $scope.profile.location = data.location;
                        $scope.profile.originallyFrom = data.originallyFrom;
                        $scope.profile.bio = data.bio;
                        $scope.profile.longbio = data.longbio;
                        $scope.profile.title = data.title;
                        $scope.profile.lookingFor = data.lookingFor;
                        $scope.profile.interests = data.interests;
                        $scope.profile.iam = data.iams;
                        $scope.profile.website = data.website;

                        $scope.profile.hasavatar = data.hasavatar;
                        $scope.profile.hascover = data.hascover;
                        $scope.profile.avatarUrl = appConstants.server + appConstants.context + "rest/public/users/" + data.userId + "/avatar",
                                $scope.profile.coverUrl = appConstants.server + appConstants.context + "rest/public/users/" + data.userId + "/cover"
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
                $rootScope.$broadcast("updateUser", {token: $scope.auth_token, userId: $scope.user_id});

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

//            if (typeof (firstname) != "undefined" && firstname != "" && firstname != initialData.firstname) {
//            } else {
//                $scope.resetData();
//            }
//            if (typeof (lastname) != "undefined" && lastname != "" && lastname != initialData.lastname) {
//            } else {
//                $scope.resetData();
//
//            }
            $users.updateBothNames(firstname, lastname).success(function (data) {
                $scope.profile.firstname = firstname;

                initialData.firstname = firstname;
                $scope.profile.lastname = lastname;

                initialData.lastname = lastname;

                $rootScope.$broadcast("quickNotification", "First Name Updated Successfully");

                $scope.calculatePercentage();
            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with updating the first name!" + data);
                $scope.resetData();
            });

        };



        $scope.updateLookingFors = function (lookingFors) {
            $scope.clearEditablesActive();
            if (typeof (lookingFors) != "undefined"
                    && lookingFors != initialData.lookingFor) {
                $users.updateLookingFor(lookingFors).success(function (data) {
                    $scope.profile.lookingFor = lookingFors;
                    initialData.lookingFor = lookingFors;
                    $rootScope.$broadcast("quickNotification", "LookingFor Updated Successfully");
                }).error(function (data) {
                    console.log("Error: " + data);
                    $rootScope.$broadcast("quickNotification", "Something went wrong with updating the lookingFor values!" + data);
                });
            } else {
                $scope.resetData();
            }

        };

        $scope.updateIams = function (iams) {
            console.log("iams = " + iams);
            if (typeof (iams) != "undefined"
                    && iams != initialData.iam) {
                $users.updateIam(iams).success(function (data) {
                    $scope.profile.iam = iams;
                    initialData.iam = iams;
                    $rootScope.$broadcast("quickNotification", "I am Updated Successfully");

                    $scope.calculatePercentage();
                }).error(function (data) {
                    console.log("Error: " + data);
                    $rootScope.$broadcast("quickNotification", "Something went wrong with updating the I am values!" + data);
                });
            } else {
                $scope.resetData();
            }

        };



        $scope.updateOriginallyFrom = function (originallyFrom) {

            $scope.clearEditablesActive();
            // $scope.clearField($("#user-originally-from-form"));
            if (typeof (originallyFrom) != "undefined" && originallyFrom != ""
                    && originallyFrom != initialData.originallyFrom) {
                $users.updateOriginallyFrom(originallyFrom).success(function (data) {
                    $scope.profile.originallyFrom = originallyFrom;
                    initialData.originallyFrom = originallyFrom;
                    $scope.calculatePercentage();
                    $rootScope.$broadcast("quickNotification", "Originally From Updated Successfully");
                }).error(function (data) {
                    console.log("Error: " + data);
                    $rootScope.$broadcast("quickNotification", "Something went wrong with updating the Originally From location!" + data);
                });
            } else {
                $scope.resetData();
            }

        };

        $scope.updateLocation = function (location) {

            $scope.clearEditablesActive();
            //$scope.clearField($("#user-location-form"));
            if (typeof (location) != "undefined" && location != "" && location != initialData.location) {
                $users.updateLocation(location).success(function (data) {
                    $scope.profile.location = location;
                    initialData.location = location;
                    $scope.calculatePercentage();
                    $rootScope.$broadcast("quickNotification", "Location Updated Successfully");
                }).error(function (data) {
                    console.log("Error: " + data);
                    $rootScope.$broadcast("quickNotification", "Something went wrong with updating the location!" + data);
                });
            } else {
                $scope.resetData();
            }

        };

        $scope.updateBioLongBioIams = function (bio, longbio, iams) {
            // if (typeof (bio) != "undefined" && bio != "" && bio != initialData.bio) {
            $users.updateBioLongBioIams(bio, longbio, iams).success(function (data) {

                $scope.profile.bio = bio;
                initialData.bio = bio;
                $scope.profile.longbio = longbio;
                initialData.longbio = longbio;
                $scope.profile.iam = iams;
                initialData.iam = iams;
                $rootScope.$broadcast("quickNotification", "Bio Updated Successfully");
            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with updating the bio!" + data);
            });
//            } else {
//                $scope.resetData();
//            }

        };

        $scope.updateBio = function (bio) {
            if (typeof (bio) != "undefined" && bio != "" && bio != initialData.bio) {
                $users.updateBio(bio).success(function (data) {
                    $rootScope.$broadcast("quickNotification", "Bio Updated Successfully");
                    $scope.profile.bio = bio;
                    initialData.bio = bio;
                }).error(function (data) {
                    console.log("Error: " + data);
                    $rootScope.$broadcast("quickNotification", "Something went wrong with updating the bio!" + data);
                });
            } else {
                $scope.resetData();
            }

        };

        $scope.updateLongBio = function (longbio) {
            if (typeof (longbio) != "undefined" && longbio != "" && longbio != initialData.longbio) {
                $users.updateLongBio(longbio).success(function (data) {
                    $rootScope.$broadcast("quickNotification", "Long Bio Updated Successfully");
                    $scope.profile.longbio = longbio;
                    initialData.longbio = longbio;
                    $scope.calculatePercentage();
                }).error(function (data) {
                    console.log("Error: " + data);
                    $rootScope.$broadcast("quickNotification", "Something went wrong with updating the long bio!" + data);
                });
            } else {
                $scope.resetData();
            }

        };


        $scope.updateTitle = function (title) {
            $scope.clearEditablesActive();
            //$scope.clearField($("#user-job-form"));
            if (typeof (title) != "undefined" && title != "" && title != initialData.title) {
                $users.updateTitle(title).success(function (data) {
                    $scope.profile.title = title;
                    initialData.title = title;
                    $scope.calculatePercentage();
                    $rootScope.$broadcast("quickNotification", "Title Updated Successfully");
                }).error(function (data) {
                    console.log("Error: " + data);
                    $rootScope.$broadcast("quickNotification", "Something went wrong with updating the bio!" + data);
                });
            } else {
                $scope.resetData();

            }

        };

        $scope.updateTwitter = function (twitter) {
            $scope.clearEditablesActive();
            //$scope.clearField($("#user-job-form"));
            if (typeof (twitter) != "undefined" && twitter != "" && twitter != initialData.twitter) {
                $users.updateTwitter(twitter).success(function (data) {
                    $scope.profile.twitter = twitter;
                    initialData.twitter = twitter;
                    $scope.calculatePercentage();
                    $rootScope.$broadcast("quickNotification", "twitter Updated Successfully");
                }).error(function (data) {
                    console.log("Error: " + data);
                    $rootScope.$broadcast("quickNotification", "Something went wrong with updating twitter!" + data);
                });
            } else {
                $scope.resetData();

            }

        };

        $scope.updateWebsite = function (website) {
            $scope.clearEditablesActive();
            //$scope.clearField($("#user-job-form"));
            if (typeof (website) != "undefined" && website != "" && website != initialData.website) {
                $users.updateWebsite(website).success(function (data) {
                    $scope.profile.website = website;
                    initialData.website = website;
                    $scope.calculatePercentage();
                    $rootScope.$broadcast("quickNotification", "website Updated Successfully");
                }).error(function (data) {
                    console.log("Error: " + data);
                    $rootScope.$broadcast("quickNotification", "Something went wrong with updating website!" + data);
                });
            } else {
                $scope.resetData();

            }

        };

        $scope.updateLinkedin = function (linkedin) {
            $scope.clearEditablesActive();
            //$scope.clearField($("#user-job-form"));
            if (typeof (linkedin) != "undefined" && linkedin != "" && linkedin != initialData.linkedin) {
                $users.updateLinkedin(linkedin).success(function (data) {
                    $scope.profile.linkedin = linkedin;
                    initialData.linkedin = linkedin;
                    $scope.calculatePercentage();
                    $rootScope.$broadcast("quickNotification", "linkedin Updated Successfully");
                }).error(function (data) {
                    console.log("Error: " + data);
                    $rootScope.$broadcast("quickNotification", "Something went wrong with updating linkedin!" + data);
                });
            } else {
                $scope.resetData();

            }

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
            if ($scope.profile.firstname != "undefined" && $scope.profile.firstname != "") {
                $scope.profile.percentage += 5;
                console.log("> + 5 firstname");
            }
            if ($scope.profile.lastname != "undefined" && $scope.profile.lastname != "") {
                $scope.profile.percentage += 5;
                console.log("> + 5 lastname");
            }
            if ($scope.profile.title != "undefined" && $scope.profile.title != "") {
                $scope.profile.percentage += 10;
                console.log("> + 10 title = " + $scope.profile.title);
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
                console.log("> + 10 lookingfor = " + $scope.profile.lookingFor);
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
            editables.each(function (index) {
                var element = $(this);
                element.bind("click", $scope.clickOnEditable);
            });
        }

        $scope.clickOnEditable = function (event) {

            if (event.target.className.indexOf("editable") > -1) {
                $scope.clearEditablesActive();
                var target = $(event.currentTarget);
                target.addClass("editable-active");
            }
        }

        $scope.clearEditablesActive = function () {
            var editablesActive = $(".editable-active")
            editablesActive.each(function (index) {
                var element = $(this);
                element.removeClass("editable-active");
            });
        };

        $scope.closeEditables = function (event) {
            event.stopPropagation();
            $scope.clearEditablesActive();
            $scope.resetData();
            $scope.calculatePercentage();
            var target = $(event.currentTarget);
//            var currentform = target.closest(".floatform");

//            var formFields =  currentform.find("input");
//            formFields.each(function(index){
//                 $(this).val('');
//            }); 
        };

//        $scope.clearField = function(form){
//             var formFields = form.find("input");
//            formFields.each(function(index){
//                 $(this).val('');
//            }); 
//        };

        $scope.initiateContentBlocks = function () {
            var blockContents = $(".content-block-content");
            blockContents.each(function (index) {

                var textareas = $(this).find("textarea");
                var checkboxes = $(this).find(".checkboxes").children().attr('disabled', 'disabled');

                textareas.each(function (index) {
                    $(this).attr('disabled', 'disabled');
                });
            });
        };

        $scope.editContentBlock = function (block) {
            block.find("textarea").removeAttr('disabled');
            block.find(".checkbox-label").removeClass('disabled');
            block.find(".checkbox-label input").removeAttr('disabled');
        }

        $scope.disableContentBlock = function (block) {
            block.find("textarea").attr('disabled', 'disabled');
            block.find(".checkbox-label").addClass('disabled');
            block.find(".checkbox-label input").attr('disabled', 'disabled');
        }


        var firstLogin = $cookieStore.get('firstLogin');

         /*
         * This code is executed everytime that we access to the profile page
         */

        angular.element(document).ready(function () {
            console.log("THe USER ID HERE IS: "+ $scope.user_id);
            console.log("THe USER EMAIL HERE IS: "+ $scope.email);
            if (firstLogin && $scope.auth_token && $scope.auth_token !== "") {
                // If it is the first time that the user is accessing the site using this account
                //  we need to update the information in the server and then load the basic data. 
                $scope.updateUserFirstLogin();

            } else {

                if ($scope.params && $scope.params.id) {
                    console.log("ROUTE PARAMS ID ");
                    console.log($scope.params.id);

                    $scope.loadPublicUserData($scope.params.id);
                    $("#profile").addClass("read-only");
                    $scope.edit = false;
                    $scope.initiateContentBlocks();
                } else if ($scope.auth_token && $scope.auth_token !== "") {
                    $scope.loadUserData($scope.user_id, $scope.email, $scope.auth_token);
                    $scope.edit = true;

                    setTimeout(function () {
                        $scope.bindEditableEvents();
                        $scope.initiateContentBlocks();
                    }, 1000);

                }
            }

        });

    };

    profileController.$inject = ["$rootScope", "$scope", "$timeout", "$users", "$sockets", "$cookieStore", "appConstants", "$routeParams", "$auth"];
    angular.module("codename").controller("profileController", profileController);
}());