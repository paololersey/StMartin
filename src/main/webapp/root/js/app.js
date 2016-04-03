define(['angularAMD', 'angular-route','ui-bootstrap', 'ng-grid', 'datePicker'  ], function (angularAMD) {
    var app = angular.module("StMartinModule", ['ngRoute','ngGrid','ui.bootstrap']);
    app.service("loadingIconManager",['$timeout',function($timeout) {
		var counter=0;
		var loadingIconCounter=0;
		var loadingIconRequests=[];
		var configIdGenerator=0;
		
		return {		
			isLoading: function() {
				return counter!=0;
			},
			showLoadingIcon: function() {
				return loadingIconCounter!=0;
			}
		};	
	}]);
    app.config(function ($routeProvider) {
        $routeProvider.when(
        	"/loginPage", angularAMD.route({
            templateUrl: 'view/loginPage.html', controller: 'loginPageCtrl',controllerUrl: 'controllers/loginPageController' 
            }))
            .when("/homePage", angularAMD.route({
            templateUrl: 'view/homePage.html', controller: 'HomePageCtrl',controllerUrl: 'controllers/homePageController' 
            }))
            .when("/peoplePage", angularAMD.route({
            templateUrl: 'view/peoplePage.html', controller: 'PeoplePageCtrl',controllerUrl: 'controllers/peoplePageController' 
            }))
            .when("/activitiesPage", angularAMD.route({
            templateUrl: 'view/activitiesPage.html', controller: 'ActivitiesPageCtrl',controllerUrl: 'controllers/activitiesPageController' 
            }))
            .when("/natureOfCasePage", angularAMD.route({
                templateUrl: 'view/natureOfCasePage.html', controller: 'NatureOfCasePageCtrl',controllerUrl: 'controllers/natureOfCasePageController' 
            }))
            .otherwise({redirectTo: "/loginPage"});
    	});
    // Before bootstrapping define common factory
    app.factory('commonFactory', ['$http',function ($http) {
		return {
			selectedDepartment : null,
			codePage : null,
			peopleData : null
		};
   }]);
   app.factory('commonMethodFactory',['$http', function($http){
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


   
   
   app.controller('messageDialogController', ['$scope', '$modalInstance','message', function($scope, $modalInstance, message) {
		$scope.testo=message;
		$scope.ok = function () {
			   $modalInstance.close("ok");
			};
			$scope.cancel = function () {
			   $modalInstance.dismiss("cancel");
			};
	}]);
    
    return angularAMD.bootstrap(app);
});




