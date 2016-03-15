'use strict';




define(['app'], function (app) {
    app.controller('HomePageCtrl', ['$scope', '$http','$location','commonFactory',function ($scope, $http,$location,commonFactory) {
        if(commonFactory.selectedDepartment =='CPPD') $scope.CPPDenabled = true;
        
    	$scope.setSelection = function(code){
    		commonFactory.codePage=code;
    		if(code==="PEO"){
    			$location.path("/peoplePage");
    		//	location.assign("../resources/people.html?projectCode='"+projectCode+"'&selectionCode='"+code+"' ");	
    		}
    		else if(code==="ACT"){
    			location.assign("../resources/activities.html?projectCode='"+projectCode+"'&selectionCode='"+code+"' ");	
    		}
    		else if(code==="APP"){
    			location.assign("../resources/appliances.html?projectCode='"+projectCode+"'&selectionCode='"+code+"' ");	
    		}
    		else if(code==="NOF"){
    			location.assign("../resources/natureOfCase.html?projectCode='"+projectCode+"'&selectionCode='"+code+"' ");	
    		}
    			
    	};
    }]);
}); 

