require.config({
    baseUrl: "js",    
    paths: {
        'angular': 'vendors/angular/1.2.16/angular.min',
        'angular-route': 'vendors/angular/angular-route.min',
        'angularAMD': 'vendors/angular/angularAMD.min',
        'ngload': 'vendors/angular/ngload.min',
        'ui-bootstrap': 'vendors/ang-ui-bootstrap/ui-bootstrap-tpls-0.11.0.min',
        'jquery': 'vendors/jquery/1.8/jquery',       
        'ng-grid': 'vendors/ng-grid/2.0.11/ng-grid-2.0.11.debug',
        'datePicker':'controllers/datePicker',
        'buttons':'controllers/buttons',
        // factories
        'commonFactory':'factories/commonFactory',
        'commonMethodFactory':'factories/commonMethodFactory',
       
        // directives
        'directive':'directives/directive',
        // auth module
        'userModule': 'user/userModule',
        'authenticationSvc':'user/factory/authenticationSvc',
        'authenticationController':'user/controllers/authenticationController',
        
    },
    shim: { 'angularAMD': ['angular'],
    	 'ngload': [ 'angularAMD' ],
    	'angular-route': ['angular'],
    	'ui-bootstrap': ['angular'],
    	'jquery':['angular'],
    	'ng-grid': ['angular','jquery'],
    	 'datePicker':['angular'],
    	 'buttons':['angular']
    	 },
    deps: ['app']
});
