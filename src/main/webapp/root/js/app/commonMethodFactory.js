define(['angularAMD'], function (angularAMD) {
	angularAMD.factory('commonMethodFactory',['$http', function($http){
    	return {
	    	openDialogMessage: function(message, callbackFunction) {
				var modalInstance = $modal.open({
				    templateUrl: 'view/dialog/errorDialog.html',
				    controller: 'messageDialogController',
				    windowClass: 'error-dialog',
				    resolve: {
				        message: function () {
				            return message;
				        }
				    }
			  	});
	
				modalInstance.result.then(function() {
				    if (callbackFunction !== undefined && callbackFunction !== null) {
				        callbackFunction();
				    }
				});
			},
			getPeopleList: function(projectPerson) {
				return $http.post('../views/listaBen', projectPerson);
			},
			getActivityList: function(filterActivity){
				return $http.post('../views/activityList', filterActivity);
			}
    	};
    }]);
});
