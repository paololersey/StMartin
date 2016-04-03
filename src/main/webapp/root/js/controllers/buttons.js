'use strict';
angular.module('buttons', ['ui.bootstrap']);
var ButtonsController= function ($scope) {
    	/**********************************************************************************************************************************************/
    	// These functions regards the grid row selection
    								$scope.backButton = function(size) {
    									$location.path("/homePage");
    								};
    								
    				$scope.selectRowButton = function(size) {
    								type="modify";
    								if ($scope.mySelections[0] == null
    										|| $scope.mySelections[0] == "") {
    									alert("No activity has been selected!");
    								} else {				
    									var ben = $scope.mySelections[0].activity.personActivities[1];
    									retrievePeople($scope.mySelections[0].activity.personActivities[0],size);
    									if(ben!=null && ben!='')retrievePeople(ben,size);
    													
    								}
    								//$scope.activity = $scope.mySelections[0].activity;
    							};

    							$scope.deleteRowButton = function(size) {
    								type="delete";
    								if ($scope.mySelections[0] == null
    										|| $scope.mySelections[0] == "") {
    									alert("No person has been selected!");
    								} else {
    									type="delete";
    									//$scope.activity = $scope.mySelections[0].activity;
    									openModal(size);					
    								}
    							};

    							// ROW INSERT
    							$scope.insertRowButton = function(size) {
    								type="insert";
    								openModal(size);			 				
    							};
    							
    	/**********************************************************************************************************************************************/		

    };
