require.config({
    baseUrl: "js",    
    paths: {
        'angular': 'vendors/angular/1.2.16/angular.min',
        'angular-route': 'vendors/angular/angular-route.min',
        'angularAMD': 'vendors/angular/angularAMD.min',   
        'ui-bootstrap': 'vendors/ang-ui-bootstrap/ui-bootstrap-tpls-0.11.0.min',
        'jquery': 'vendors/jquery/1.8/jquery',       
        'ng-grid': 'vendors/ng-grid/2.0.11/ng-grid-2.0.11.debug',
        'datePicker':'controllers/datePicker'
    },
    shim: { 'angularAMD': ['angular'], 
    	'angular-route': ['angular'],
    	'ui-bootstrap': ['angular'],
    	'jquery':['angular'],
    	'ng-grid': ['angular','jquery'],
    	 'datePicker':['angular']},
    deps: ['app']
});
