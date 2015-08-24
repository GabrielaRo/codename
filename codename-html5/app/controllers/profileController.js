(function () {
    var profileController = function ($rootScope, $scope, $timeout, $users, $cookieStore, appConstants, $routeParams, $auth, location, $interests, reverseGeocoder) {


        /*
         * For Loading we try to fetch everything at once instead of each different piece
         */
        $scope.params = $routeParams;
        $scope.edit = true;
        $scope.interests = [];
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

        $scope.shareLocationForProfile = function () {
            location.get(angular.noop, angular.noop);
            location.ready(function () {
                reverseGeocoder.geocode(location.current)
                        .then(function (results) {
                            console.log("Result [0]");
                            console.log(results[0]);
                            
                            var locData = {
                                latitude: location.current.latitude,
                                longitude: location.current.longitude,
                                name: results[0].address_components[6].short_name,
                                description: results[0].address_components[6].short_name + ", " +results[0].address_components[3].short_name
                            };
                            $scope.selectedLocation = locData;
                            var el = angular.element(document.querySelectorAll("#myLocationText"));
                            el[0].value = locData.description;
                            $scope.userCurrentLocation = locData;
                            console.log("Current location is: ");
                            console.log(locData);

                        });
            });

        };

        $scope.selectLocation = function () {
            if ($scope.userCurrentLocation) {
                $scope.profile.location.description = $scope.userCurrentLocation.description;
            }
        };


        $scope.$watch('userCurrentLocation', $scope.selectLocation);

        $scope.typeButtonPressed = function (buttonName) {

            if ($scope.profile.iam.indexOf(buttonName) == -1) {
                $scope.profile.iam.push(buttonName);
            } else {
                $scope.profile.iam.splice($scope.profile.iam.indexOf(buttonName), 1);
            }

        }
        $scope.lookingForButtonPressed = function (buttonName) {

            if ($scope.profile.lookingFor.indexOf(buttonName) == -1) {
                $scope.profile.lookingFor.push(buttonName);
            } else {
                $scope.profile.lookingFor.splice($scope.profile.lookingFor.indexOf(buttonName), 1);
            }
        }


        /*
         * This code loads all the profile user data from the server.
         *  We use initialData to store the information that we retrieved from the server when the method
         *  is called. So we can check if the user changed it at some point.
         */

        $scope.loadUserData = function () {
            $users.getUserData()
                    .success(function (data) {
                        //console.log(data);
                        $scope.profile.userId = data.userId;
                        $scope.profile.firstname = data.firstname;
                        $scope.profile.lastname = data.lastname;
                        $scope.profile.nickname = data.nickname;
                        $scope.profile.location.description = data.location;
                        $scope.profile.originallyFrom = data.originallyFrom;
                        $scope.profile.bio = data.bio;
                        $scope.profile.longbio = data.longbio;
                        $scope.profile.title = data.title;
                        $scope.profile.advice = data.advice;
                        $scope.profile.messageme = data.messageme;
                        $scope.profile.share = data.share;
                        $scope.profile.resources = data.resources;
                        $scope.profile.hobbies = data.hobbies;
                        $scope.profile.lookingFor = data.lookingFor;
                        $scope.profile.interests = data.interests;
                        $scope.profile.iam = data.iams;
                        $scope.profile.website = data.website;
                        $scope.profile.linkedin = data.linkedin;
                        $scope.profile.twitter = data.twitter;
                        $scope.profile.live = data.live;
                        $scope.profile.hasavatar = data.hasavatar;
                        $scope.profile.hascover = data.hascover;
                        $scope.profile.avatarUrl = appConstants.server + appConstants.context + "rest/public/users/" + data.nickname + "/avatar",
                                $scope.profile.coverUrl = appConstants.server + appConstants.context + "rest/public/users/" + data.nickname + "/cover"
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
        //INTERSTS TAGS FUNCTIONS

        $scope.interestsTagAdded = function (tagClicked) {
            if ($scope.profile.interests.indexOf(tagClicked.text) == -1) {
                $scope.profile.interests.push(tagClicked.text);
            }
        }
        $scope.interestsTagDeleted = function (tagClicked) {
            $scope.profile.interests.splice($scope.profile.interests.indexOf(tagClicked), 1);
        }

        //LOAD INTERESTS

        $scope.loadInterests = function ($query) {
            return $scope.interests.filter(function (interest) {
                console.log(interest);
                return interest.text.toLowerCase().indexOf($query.toLowerCase()) != -1;
            });
        }

        $scope.loadInterestOnInit = function () {
            $interests.getAll().success(function (data) {

                $scope.interests = data;

            }).error(function (data) {
                console.log("Error: ");
                console.log(data);
                $rootScope.$broadcast("quickNotification", "Something went wrong loading the interests!" + data);
            });

        }
        $scope.loadInterestOnInit();

        //INTEREST EDITABLE BLOCK
        $scope.interestStatus = false;
        $scope.editInterestBlock = function () {

            $scope.interestStatus = true;
        }
        $scope.saveInterestBlock = function (interests) {

            $scope.updateInterests(interests);
            $scope.interestStatus = false;

        }
        $scope.cancelInterestBlock = function () {
            //$scope.disableContentBlock($("#user-about-form"));
            $scope.interestStatus = false;
            $scope.resetData();

        }

        $scope.updateInterests = function (interests) {

            if (typeof (interests) != "undefined"
                    && interests != initialData.interests) {
                $users.updateInterests(interests).success(function (data) {
                    $scope.profile.interests = interests;
                    initialData.interests = interests;

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
                        $scope.profile.resources = data.resources;
                        $scope.profile.hasavatar = data.hasavatar;
                        $scope.profile.hascover = data.hascover;
                        $scope.profile.avatarUrl = appConstants.server + appConstants.context + "rest/public/users/" + data.nickname + "/avatar",
                                $scope.profile.coverUrl = appConstants.server + appConstants.context + "rest/public/users/" + data.nickname + "/cover"
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

                    }).success(function (data) {

                $scope.uploadAvatarPercentage = false;
                $scope.profile.avatarUrl = "";
                $scope.profile.avatarUrl = appConstants.server + appConstants.context + "rest/public/users/" + $scope.user_nick + "/avatar" + '?' + new Date().getTime();
                $scope.profile.hasavatar = true;
                $scope.calculatePercentage();
                $rootScope.$broadcast("updateUser", {token: $scope.auth_token, userId: $scope.user_id, userNick: $scope.user_nick});

            }).error(function (data) {
                console.log('Error: file ' + file.name + ' upload error. Response: ' + data);
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

                    }).success(function (data) {
                // file is uploaded successfully

                $scope.uploadingCover = false;
                $scope.profile.coverUrl = "";
                $scope.profile.coverUrl = appConstants.server + appConstants.context + "rest/public/users/" + $scope.user_nick + "/cover" + '?' + new Date().getTime();
                $scope.profile.hascover = true;
                $scope.calculatePercentage();
                $rootScope.$broadcast("updateUserCover");

            }).error(function (data) {
                console.log('Error: file ' + file.name + ' upload error. Response: ' + data);
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

            $users.updateBothNames(firstname, lastname).success(function (data) {
                $scope.profile.firstname = firstname;

                initialData.firstname = firstname;
                $scope.profile.lastname = lastname;

                initialData.lastname = lastname;


                $scope.calculatePercentage();
            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with updating the first name!" + data);
                $scope.resetData();
            });

        };



        $scope.updateLookingForsAndIams = function (lookingFors, iams) {
            $scope.clearEditablesActive();
            if (typeof (lookingFors) != "undefined"
                    && lookingFors != initialData.lookingFor && typeof (iams) != "undefined"
                    && iams != initialData.iam) {
                $users.updateLookingForAndIams(lookingFors, iams).success(function (data) {
                    $scope.profile.lookingFor = lookingFors;
                    initialData.lookingFor = lookingFors;
                    $scope.profile.iam = iams;
                    initialData.iam = iams;
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
            //$scope.clearField($("#user-originally-from-float-form"));
            if (typeof (originallyFrom) != "undefined" && originallyFrom != ""
                    && originallyFrom != initialData.originallyFrom) {
                $users.updateOriginallyFrom(originallyFrom).success(function (data) {
                    $scope.profile.originallyFrom = originallyFrom;
                    initialData.originallyFrom = originallyFrom;
                    $scope.calculatePercentage();

                }).error(function (data) {
                    console.log("Error: " + data);
                    $rootScope.$broadcast("quickNotification", "Something went wrong with updating the Originally From location!" + data);
                });
            } else {
                $scope.resetData();
            }

        };

        $scope.updateLocation = function (location, lon, lat) {

            $scope.clearEditablesActive();

            if (typeof (location) != "undefined" && location != ""
                    && location != initialData.location) {
                $users.updateLocation(location, lon, lat).success(function (data) {

                    $scope.profile.location.description = location;

                    initialData.location.description = location;
                    $scope.calculatePercentage();

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
            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with updating the bio!" + data);
            });
//            } else {
//                $scope.resetData();
//            }

        };

        $scope.updateBio = function (bio) {
            $scope.clearEditablesActive();
            if (typeof (bio) != "undefined" && bio != "" && bio != initialData.bio) {
                $users.updateBio(bio).success(function (data) {

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


        $scope.updateJobTitle = function (title) {
            $scope.clearEditablesActive();
            //$scope.clearField($("#user-job-form"));
            if (typeof (title) != "undefined" && title != "" && title != initialData.title) {
                $users.updateJobTitle(title).success(function (data) {
                    $scope.profile.title = title;
                    initialData.title = title;
                    $scope.calculatePercentage();
                }).error(function (data) {
                    console.log("Error: " + data);
                    $rootScope.$broadcast("quickNotification", "Something went wrong with updating the bio!" + data);
                });
            } else {
                $scope.resetData();

            }

        };
        
        $scope.updateNickname = function (nickname) {
            $scope.clearEditablesActive();
            //$scope.clearField($("#user-job-form"));
            if (typeof (nickname) != "undefined" && nickname != "" && nickname != initialData.nickname) {
                $users.updateNickname(nickname).success(function (data) {
                    $scope.profile.nickname = nickname;
                    initialData.nickname = nickname;
                    $rootScope.$broadcast("updateUser", {token: $scope.auth_token, userId: $scope.user_id, userNick: nickname});
                    $scope.calculatePercentage();
                }).error(function (data) {
                    console.log("Error: " + data);
                    $rootScope.$broadcast("quickNotification", "Something went wrong with updating your nickname!" + data);
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
                }).error(function (data) {
                    console.log("Error: " + data);
                    $rootScope.$broadcast("quickNotification", "Something went wrong with updating twitter!" + data);
                });
            } else {
                $scope.resetData();

            }

        };

        $scope.updateAdvice = function (advice) {
            $scope.clearEditablesActive();
            //$scope.clearField($("#user-job-form"));
            if (typeof (advice) != "undefined" && advice != "" && advice != initialData.advice) {
                $users.updateAdvice(advice).success(function (data) {
                    $scope.profile.advice = advice;
                    initialData.advice = advice;
                    $scope.calculatePercentage();
                }).error(function (data) {
                    console.log("Error: " + data);
                    $rootScope.$broadcast("quickNotification", "Something went wrong with updating advice!" + data);
                });
            } else {
                $scope.resetData();

            }

        };

        $scope.updateHobbies = function (hobbies) {
            $scope.clearEditablesActive();
            //$scope.clearField($("#user-job-form"));
            if (typeof (hobbies) != "undefined" && hobbies != "" && hobbies != initialData.hobbies) {
                $users.updateHobbies(hobbies).success(function (data) {
                    $scope.profile.hobbies = hobbies;
                    initialData.hobbies = hobbies;
                    $scope.calculatePercentage();
                }).error(function (data) {
                    console.log("Error: " + data);
                    $rootScope.$broadcast("quickNotification", "Something went wrong with updating hobbies!" + data);
                });
            } else {
                $scope.resetData();

            }

        };

        $scope.updateResources = function (resources) {
            $scope.clearEditablesActive();
            //$scope.clearField($("#user-job-form"));
            if (typeof (resources) != "undefined" && resources != "" && resources != initialData.resources) {
                $users.updateResources(resources).success(function (data) {
                    $scope.profile.resources = resources;
                    initialData.resources = resources;
                    $scope.calculatePercentage();
                }).error(function (data) {
                    console.log("Error: " + data);
                    $rootScope.$broadcast("quickNotification", "Something went wrong with updating resources!" + data);
                });
            } else {
                $scope.resetData();

            }

        };

        $scope.updateShare = function (share) {
            $scope.clearEditablesActive();
            //$scope.clearField($("#user-job-form"));
            if (typeof (share) != "undefined" && share != "" && share != initialData.share) {
                $users.updateShare(share).success(function (data) {
                    $scope.profile.share = share;
                    initialData.share = share;
                    $scope.calculatePercentage();
                }).error(function (data) {
                    console.log("Error: " + data);
                    $rootScope.$broadcast("quickNotification", "Something went wrong with updating share!" + data);
                });
            } else {
                $scope.resetData();

            }

        };

        $scope.updateMessageMe = function (messageme) {
            $scope.clearEditablesActive();
            //$scope.clearField($("#user-job-form"));
            if (typeof (messageme) != "undefined" && messageme != "" && messageme != initialData.messageme) {
                $users.updateMessageMe(messageme).success(function (data) {
                    $scope.profile.messageme = messageme;
                    initialData.messageme = messageme;
                    $scope.calculatePercentage();
                }).error(function (data) {
                    console.log("Error: " + data);
                    $rootScope.$broadcast("quickNotification", "Something went wrong with updating messageme!" + data);
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

                $scope.profile.percentage += 5;
            }
            if ($scope.profile.hasavatar == true) {

                $scope.profile.percentage += 5;
            }
            if ($scope.profile.firstname != "undefined" && $scope.profile.firstname != "") {
                $scope.profile.percentage += 5;

            }
            if ($scope.profile.lastname != "undefined" && $scope.profile.lastname != "") {
                $scope.profile.percentage += 5;

            }
            if ($scope.profile.title != "undefined" && $scope.profile.title != "") {
                $scope.profile.percentage += 10;

            }
            if ($scope.profile.location.description != "undefined" && $scope.profile.location.description != "") {
                $scope.profile.percentage += 10;

            }

            if ($scope.profile.originallyFrom != "undefined" && $scope.profile.originallyFrom != "") {
                $scope.profile.percentage += 10;

            }
            if ($scope.profile.bio != "undefined" && $scope.profile.bio != "") {
                $scope.profile.percentage += 5;

            }
            if ($scope.profile.longbio != "undefined" && $scope.profile.longbio != "") {
                $scope.profile.percentage += 5;

            }
            if ($scope.profile.interests != "undefined" && $scope.profile.interests != "") {
                $scope.profile.percentage += 10;

            }
            if ($scope.profile.lookingFor != "undefined" && $scope.profile.lookingFor != "") {
                $scope.profile.percentage += 10;

            }
            if ($scope.profile.iam != "undefined" && $scope.profile.iam != "") {
                $scope.profile.percentage += 10;

            }
            console.log("The percentage is : " + $scope.profile.percentage);

        };
        
        $scope.previewProfile = function(nickname){
            $rootScope.$broadcast('goTo', "/profile/"+nickname);
        }

        $scope.updateUserLiveProfile = function (live) {

            $users.updateUserLiveProfile(!live).success(function (data) {
                $cookieStore.put('live', !live);
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
        };


        var firstLogin = $cookieStore.get('firstLogin');

        /*
         * This code is executed everytime that we access to the profile page
         */

        angular.element(document).ready(function () {

            if (firstLogin && $scope.auth_token && $scope.auth_token !== "") {
                // If it is the first time that the user is accessing the site using this account
                //  we need to update the information in the server and then load the basic data. 
                $scope.updateUserFirstLogin();


                setTimeout(function () {
                    $scope.bindEditableEvents();
                    
                }, 500);

            } else {
               
                if ($scope.auth_token && $scope.auth_token !== "") {
                    $scope.loadUserData($scope.user_id, $scope.email, $scope.auth_token);
                    $scope.edit = true;

                    setTimeout(function () {
                        $scope.bindEditableEvents();
                    }, 500);
                }
            }

        });

    };

    profileController.$inject = ["$rootScope", "$scope", "$timeout", "$users", "$cookieStore", "appConstants", "$routeParams", "$auth", "location", "$interests", "reverseGeocoder"];
    angular.module("codename").controller("profileController", profileController);
}());