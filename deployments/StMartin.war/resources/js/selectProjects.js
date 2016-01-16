'use strict';

var selectProject = angular.module('selectProject',[]);

selectProject.controller('selectStMartinProject',
		function($scope, $http) {
		

	$scope.setProject = function(code){	
		$scope.code=code;	
			location.assign("../resources/selectPeopleCasesActivities.html?projectCode='"+$scope.code+"'");	
	};
            
});
