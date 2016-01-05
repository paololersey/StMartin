'use strict';

var loginPage = angular.module('loginPage',[]);

loginPage.controller('loginPageController',
		function($scope, $http) {
		
    $scope.submitOk = function() {
    	$scope.submitCredentials = {"username": $scope.username, "password": $scope.password};
    	$http.post("../views/submitCredentials",$scope.submitCredentials).success(function(department){
    		if(department != null && department!=""){    			
    				location.assign("../resources/selectPeopleCasesActivities.html?projectCode='"+department+"'");
    		}
    		else{
    			alert ("Your username/password are wrong");
    		}
			
    	});
    };
    
    $scope.submitCancel = function() {
    	$scope.password=null;
    	$scope.username=null;
    };
    /*$scope.submitLoginPassword = function(login,password){	
		$scope.submitCredentials = {"username": $scope.username, "password": $scope.password};
		if ( $scope.username && $scope.password ){
			$http.post("../views/submitCredentials",$scope.submitCredentials).function(){
				
			}
		}
			./resources/selectPeopleCasesActivities.html?projelocation.assign(".ctCode='"+$scope.code+"'");	
	};*/
            
});
