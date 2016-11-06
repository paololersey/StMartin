//define(['angularAMD', 'angular-route','ui-bootstrap', 'ng-grid', 'datePicker','buttons','userModule','app/commonFactory'
  // ], function (angularAMD) {

define(['common'], function (angularAMD) {
  'use strict';
    var app = angular.module("StMartinModule", ['ngRoute','ngGrid','ui.bootstrap','userModule']);

    app.service("loadingIconManager",['$timeout',function($timeout) {
		var counter=0;
		var loadingIconCounter=0;
		
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
            templateUrl: 'view/loginPage.html', 
            controller: 'loginPageCtrl',
            controllerUrl: 'controllers/loginPageController' 
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
            .when("/paymentPage", angularAMD.route({
                templateUrl: 'view/paymentPage.html', controller: 'PaymentPageCtrl',controllerUrl: 'controllers/paymentPageController' 
            }))
            .otherwise({redirectTo: "/loginPage"});
    	});

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




