angular.module('buttons', [ 'ui.bootstrap' ]).controller("ButtonsController"),
		[
				'$scope','$location',
				function($scope, $location) {
					/** ******************************************************************************************************************************************* */
					// These functions regards the grid row selection
					$scope.backButton = function(size) {
						$location.path("/homePage");
					};

					$scope.deleteRowButton = function(size) {
						type = "delete";
						if ($scope.mySelections[0] == null
								|| $scope.mySelections[0] == "") {
							alert("No row has been selected!");
						} else {
							type = "delete";
							openModal(size);
						}
					};

					// ROW INSERT
					$scope.insertRowButton = function(size) {
						type = "insert";
						openModal(size);
					};

					/** ******************************************************************************************************************************************* */

				} ];
