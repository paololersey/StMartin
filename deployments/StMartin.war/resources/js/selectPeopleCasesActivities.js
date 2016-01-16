'use strict';

var select = angular.module('select',[]);

select.controller('selectController',
		function($scope, $http) {
		
	var url = window.location.toString();
    url.match(/\?(.+)$/);
    var params= RegExp.$1;
    
    var startIndex = params.indexOf("=");
    var projectCode = params.substring(startIndex+4,startIndex+8);
    
    if(projectCode =='CPPD') $scope.CPPDenabled = true;
    
	$scope.setSelection = function(code){	
		if(code==="PEO"){
			location.assign("../resources/people.html?projectCode='"+projectCode+"'&selectionCode='"+code+"' ");	
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
            
});
