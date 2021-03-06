shioharaApp.controller('ShWidgetRelatorSelectCtrl', [
		'$scope',
		'shAPIServerService',
		'$http',
		'$uibModalInstance',
		'shChildrenRelatorItem',
		'shWidgetFileFactory',
		function($scope, shAPIServerService, $http, $uibModalInstance,
				shChildrenRelatorItem,shWidgetFileFactory) {
			var $ctrl = this;
			$scope.shPostAttrs = shChildrenRelatorItem.shChildrenPostAttrs;
			$ctrl.shPostSelected = null;
		
			$scope.shSite = null;
			$scope.shFolders = null;
			$scope.shPosts = null;
			$scope.breadcrumb = null;

			// BEGIN Functions
			$ctrl.ok = function() {
				$scope.title = null;
				$scope.summary = null;
				angular.forEach($scope.shPostAttrs, function(shPostAttr, key) {		
					if (shPostAttr.shPostTypeAttr.isTitle == 1) {
						$scope.title = shPostAttr.strValue;
				    }
					if (shPostAttr.shPostTypeAttr.isSummary == 1) {
						$scope.summary = shPostAttr.strValue;
					}
				});	
				$uibModalInstance.close({shPostAttrs: $scope.shPostAttrs, title: $scope.title, summary: $scope.summary});
			};

			$ctrl.cancel = function() {
				$ctrl.shPostSelected = null;
				$uibModalInstance.dismiss('cancel');
			};
			
			$scope.selectedPost = function(shPost) {
				$ctrl.shPostSelected = shPost;
			}
			// END Functions

		} ]);
