shioharaApp.controller('ShObjectChildrenCtrl', [
						"$scope"
						, "$state"
						, "$stateParams"
						, "$rootScope"
						, "$translate"
						, "$http"
						, "$window"
						, "shAPIServerService"
						, 'vigLocale'
						, "shFolderFactory"
						, "shPostFactory"
						, "ShDialogSelectObject"
						, "ShDialogDeleteFactory"
						, "shPostResource"
						, "shFolderResource"
						, "$filter"
						, "Notification"
						, "moment"
						, "shUserResource"
						, "shPostTypeResource"
    , function ($scope, $state, $stateParams, $rootScope, $translate, $http, $window, shAPIServerService, vigLocale, shFolderFactory, shPostFactory, ShDialogSelectObject, ShDialogDeleteFactory, shPostResource, shFolderResource, $filter, Notification, moment, shUserResource, shPostTypeResource) {
        
		$scope.objectId = $stateParams.objectId;
        $scope.vigLanguage = vigLocale.getLocale().substring(0, 2);
        $translate.use($scope.vigLanguage);
        $scope.shCurrentFolder = null;
        $scope.shSite = null;
        $scope.shFolders = null;
        $scope.shPosts = null;
        $scope.breadcrumb = null;
        $rootScope.$state = $state;
        $scope.shStateObjects = [];
        $scope.shObjects = [];
        $scope.actions = [];
        $scope.shUser = null;
        $scope.shLastPostType = null;
        $scope.itemSelected = false;
        
        $scope.checkSomeItemSelected = function () {
        	$scope.itemSelected = false;
        	for (var stateKey in $scope.shStateObjects) {
        		if ($scope.shStateObjects[stateKey]) {
        			$scope.itemSelected = true;
        		}
        	}        
        }
        $scope.sortableFolders = {
       		  stop: function(e, ui) {
        			  console.log("Test update sort");
        			  var sortObject = {};
        			  var i = 1 ;
        			  angular.forEach($scope.shFolders, function (shFolder, key) {
        				  sortObject[shFolder.id] = shFolder.position;
        	            });         			 
        			  var parameter = JSON.stringify(sortObject);
                      $http.put(shAPIServerService.get().concat("/v2/object/sort"), parameter).then(function (response) {
                    	  console.log("Sort was updated");
                      });
        		  }
        		};
        $scope.sortablePosts = {
         		  stop: function(e, ui) {
          			  console.log("Test update sort");
          			  var sortObject = {};
          			  var i = 1 ;
          			  angular.forEach($scope.shPosts, function (shPost, key) {
          				sortObject[shPost.id] = shPost.position;
          	            });         			 
          			  var parameter = JSON.stringify(sortObject);
                        $http.put(shAPIServerService.get().concat("/v2/object/sort"), parameter).then(function (response) {
                      	  console.log("Sort was updated");
                        });
          		  }
          		};
    	$scope.shUser = shUserResource.get({
			id : "admin",
			access_token : $scope.accessToken
		}, function() {
			$scope.shLastPostType = shPostTypeResource.get({
				id : $scope.shUser.lastPostType
			});
			
		});
    	 $scope.$evalAsync($http.get(shAPIServerService.get().concat("/v2/history/object/" + $scope.objectId ))
    			 .then(function (response) {
             $scope.commits = response.data.length;
         }));
    	 
        $scope.$evalAsync($http.get(shAPIServerService.get().concat("/v2/object/" + $scope.objectId + "/list")).then(function (response) {
            $scope.processResponse(response);
        }));
        $scope.processResponse = function (response) {
            $scope.shFolders = response.data.shFolders;
            $scope.shFolders = $filter('orderBy')($scope.shFolders, 'position');
            $scope.shPosts = response.data.shPosts;
            $scope.shPosts = $filter('orderBy')($scope.shPosts, 'position');
            $scope.breadcrumb = response.data.breadcrumb;         
            $scope.shCurrentFolder = null;
            if ($scope.breadcrumb != null) {	
            	$scope.shCurrentFolder = $scope.breadcrumb.slice(-1).pop();
            }
            $scope.shSite = response.data.shSite;         
            angular.forEach($scope.shFolders, function (shFolder, key) {
                $scope.shStateObjects[shFolder.id] = false;
                $scope.shObjects[shFolder.id] = shFolder;
                $scope.actions[shFolder.id] = false;
            });
            angular.forEach($scope.shPosts, function (shPost, key) {
                $scope.shStateObjects[shPost.id] = false;
                $scope.shObjects[shPost.id] = shPost;
                $scope.actions[shPost.id] = false;
            });
            $scope.itemSelected = false;
        }
     
        $scope.selectContents = function () {	
       	 for (var stateKey in $scope.shStateObjects) {
       		 if ($scope.shObjects[stateKey].objectType === "POST") {
       			 $scope.shStateObjects[stateKey] = true;
       			 $scope.itemSelected = true;
       		 }
       		 else {
       			$scope.shStateObjects[stateKey] = false;
       		 }
       	 }
       	 $scope.checkSomeItemSelected();
        }
        
        $scope.selectFolders = function () {	
        	 for (var stateKey in $scope.shStateObjects) {
        		 if ($scope.shObjects[stateKey].objectType === "FOLDER") {
        			 $scope.shStateObjects[stateKey] = true;
        		 }
        		 else {
        			 $scope.shStateObjects[stateKey] = false;
        		 }
        	 }
        	 $scope.checkSomeItemSelected();
        }
        
        $scope.selectEverything = function () {	
          	 for (var stateKey in $scope.shStateObjects) {          		
          			 $scope.shStateObjects[stateKey] = true;
          	 }
          	$scope.checkSomeItemSelected();
        }
        
        $scope.selectNothing = function () {	
         	 for (var stateKey in $scope.shStateObjects) {          		
         			 $scope.shStateObjects[stateKey] = false;
         	 }
         	 $scope.itemSelected = false;
        }
        
        $scope.selectInverted = function () {	
        	 for (var stateKey in $scope.shStateObjects) {  
        		 if ($scope.shStateObjects[stateKey]) {
        			 $scope.shStateObjects[stateKey] = false;
        		 } else {
        			 $scope.shStateObjects[stateKey] = true;
        		 }
        		 
        	 }
        	 $scope.checkSomeItemSelected();
       }

        $scope.updateAction = function (id, value) {
            $scope.actions[id] = value;
            $scope.checkSomeItemSelected();
        }
        $scope.isRecent = function (date) {
        	var momentDate = moment(date);
        	var now = new moment();
        	var duration = moment.duration(momentDate.diff(now))        
        	if (duration.as('minutes') >= -5) {        		
        		return true;
        	}
        	else {
        		return false;
        	}
        	
            return false;
        }
        $scope.objectsCopy = function () {
            var objectGlobalIds = [];
            for (var stateKey in $scope.shStateObjects) {
                if ($scope.shStateObjects[stateKey] === true) {
                    var objectGlobalId = "" + stateKey;
                    objectGlobalIds.push(objectGlobalId);                    
                }
            }
            $scope.objectsCopyDialog(objectGlobalIds);
        }
        
        $scope.objectCopy = function (shObject) {
            var objectGlobalIds = [];
            objectGlobalIds.push(shObject.id);
            $scope.objectsCopyDialog(objectGlobalIds);
        }
        
        $scope.objectsCopyDialog = function (objectGlobalIds) {
            var modalInstance = ShDialogSelectObject.dialog($scope.objectId, "shFolder");
            modalInstance.result.then(function (shObjectSelected) {
                var parameter = JSON.stringify(objectGlobalIds);
                $http.put(shAPIServerService.get().concat("/v2/object/copyto/" + shObjectSelected.id), parameter).then(function (response) {
                    var shObjects = response.data;
                    for (i = 0; i < shObjects.length; i++) {
                    	shObject = shObjects[i];
                    	if (shObjectSelected.id == $scope.objectId) {
                    		$scope.shStateObjects[shObject.id] = false;
                    		$scope.shObjects[shObject.id] = shObject;
                            $scope.actions[shObject.id] = false;
                		}
                    	var copiedMessage = null;
                        if (shObject.objectType == "POST") {
                        	if (shObjectSelected.id == $scope.objectId) {
                        		$scope.shPosts.push(shObject);
                        	}
                        	copiedMessage = 'The ' + shObject.title + ' Post was copied.';
                        }
                        else if (shObject.objectType == "FOLDER") {
                        	if (shObjectSelected.id == $scope.objectId) {
                        		$scope.shFolders.push(shObject);
                        	}
                        	copiedMessage = 'The ' + shObject.name + ' Folder was copied.';
                        }
                        Notification.warning(copiedMessage);
                    }
                    $scope.checkSomeItemSelected();
                });
            }, function () {
                // Selected CANCEL
            });
        }
        $scope.objectsMove = function () {
            var objectGlobalIds = [];
            for (var stateKey in $scope.shStateObjects) {
                if ($scope.shStateObjects[stateKey] === true) {
                    var objectGlobalId = "" + stateKey;
                    objectGlobalIds.push(objectGlobalId);
                    
                }
            }
            $scope.objectsMoveDialog(objectGlobalIds);
        }
        
        $scope.objectMove = function (shObject) {
            var objectGlobalIds = [];
            objectGlobalIds.push(shObject.id);
            $scope.objectsMoveDialog(objectGlobalIds);
        }
        
        $scope.objectsMoveDialog = function (objectGlobalIds) {
            var modalInstance = ShDialogSelectObject.dialog($scope.objectId, "shFolder");
            modalInstance.result.then(function (shObjectSelected) {
            	if (shObjectSelected.id == $scope.objectId) {
            		movedMessage = 'No moved, because you selected the same folder as destination';
            		Notification.warning(movedMessage);
            	}
            	else {
	                var parameter = JSON.stringify(objectGlobalIds);
	                $http.put(shAPIServerService.get().concat("/v2/object/moveto/" + shObjectSelected.id), parameter).then(function (response) {
	                    var shObjects = response.data;
	                    for (i = 0; i < shObjects.length; i++) {
	                    	shObject = shObjects[i];
	                    	$scope.shStateObjects[shObject.id] = false;
	                        var movedMessage = null;
	                        if (shObject.objectType == "POST") {
	                        	movedMessage = 'The ' + shObject.title + ' Post was moved.';
		                        var foundItem = $filter('filter')
		                            ($scope.shPosts, {
		                                id: shObject.id
		                            }, true)[0];
		                        var index = $scope.shPosts.indexOf(foundItem);
		                        $scope.shPosts.splice(index, 1);
	                        } else if (shObject.objectType == "FOLDER") {
	                        	movedMessage = 'The ' + shObject.name + ' Folder was moved.';
	                        	  var foundItem = $filter('filter')
		                            ($scope.shFolders, {
		                                id: shObject.id
		                            }, true)[0];
		                        var index = $scope.shFolders.indexOf(foundItem);
		                        $scope.shFolders.splice(index, 1);
	                        }
	                        Notification.warning(movedMessage);
	                    }
	                    $scope.checkSomeItemSelected();
	                });
            	}
            }, function () {
                // Selected CANCEL
            });
        }
        
        $scope.objectClone = function (shObject) {
            var objectGlobalIds = [];
            objectGlobalIds.push(shObject.id);
            var parameter = JSON.stringify(objectGlobalIds);
            var parentObjectId = null;
            if ($scope.shCurrentFolder == null) {
            	parentObjectId = $scope.shSite.id;
            }
            else {
            	parentObjectId = $scope.shCurrentFolder.id;
            }
            $http.put(shAPIServerService.get().concat("/v2/object/copyto/" + parentObjectId), parameter).then(function (response) {
                var shObjects = response.data;
                for (i = 0; i < shObjects.length; i++) {
                	shObject = shObjects[i];
                	$scope.shStateObjects[shObject.id] = false;
                    $scope.shObjects[shObject.id] = shObject;
                    $scope.actions[shObject.id] = false;
                	var clonedMessage = null;
                	if (shObject.objectType == "POST") {
                		$scope.shPosts.push(shObject);
                		clonedMessage = 'The ' + shObject.title + ' Post was cloned.';                	
                	} else if (shObject.objectType == "FOLDER") {
                		$scope.shFolders.push(shObject);
                		clonedMessage = 'The ' + shObject.name + ' Folder was cloned.';                		
                	}
                	Notification.warning(clonedMessage);
                }
                $scope.checkSomeItemSelected();
            });
        }       
        $scope.objectsRename = function () {
            for (var stateKey in $scope.shStateObjects) {
                if ($scope.shStateObjects[stateKey] === true) {
                    console.log("Rename " + stateKey);
                    $scope.shStateObjects[stateKey] = false;
                }                
            }
            $scope.checkSomeItemSelected();
        }
        $scope.objectsDelete = function () {
        	 var shSelectedObjects = [];
             for (var stateKey in $scope.shStateObjects) {
                 if ($scope.shStateObjects[stateKey] === true) {                     
                	 shSelectedObjects.push($scope.shObjects[stateKey]);                	
                 }
             }
             $scope.objectsDeleteDialog(shSelectedObjects);
        }
        $scope.folderDelete = function (shFolder) {
            shFolderFactory.deleteFromList(shFolder, $scope.shFolders);
        }
        $scope.postDelete = function (shPost) {         
            shPostFactory.deleteFromList(shPost, $scope.shPosts);
        }
        
        
        $scope.objectsDeleteDialog = function (shSelectedObjects) {
            var modalInstance = ShDialogDeleteFactory.dialog(shSelectedObjects);
            modalInstance.result.then(function () {
            	angular.forEach(shSelectedObjects, function(value, key) {  
            		 $scope.shStateObjects[value.id] = false;
            		if (value.objectType === "POST") {
            		shPost = value;
            		var deletedMessage = 'The ' + shPost.title + ' Post was deleted.';
                    shPostResource.delete({
                        id: shPost.id
                    }, function () {
                    	delete $scope.shStateObjects[shPost.id];
                        // filter the array
                        var foundItem = $filter('filter')($scope.shPosts, {
                            id: shPost.id
                        }, true)[0];
                        // get the index
                        var index = $scope.shPosts.indexOf(foundItem);
                        // remove the item from array
                        $scope.shPosts.splice(index, 1);
                        Notification.error(deletedMessage);
                    });
            	} else if (value.objectType === "FOLDER") {
            		shFolder = value;
            		var deletedMessage = 'The ' + shFolder.name + ' Folder was deleted.';
                    shFolderResource.delete({
                        id: shFolder.id
                    }, function () {
                    	delete $scope.shStateObjects[shFolder.id];
                        // filter the array
                        var foundItem = $filter('filter')($scope.shFolders, {
                            id: shFolder.id
                        }, true)[0];
                        // get the index
                        var index = $scope.shFolders.indexOf(foundItem);
                        // remove the item from array
                        $scope.shFolders.splice(index, 1);
                        Notification.error(deletedMessage);
                    });
            	}
            		 $scope.checkSomeItemSelected();
            	});
            }, function () {
                // Selected CANCEL
            });
        }
        
        $scope.objectPreview = function (shObject) {
            var link = shAPIServerService.get().concat("/v2/object/" + shObject.id + "/preview");
            $window.open(link);
        }
						}]);