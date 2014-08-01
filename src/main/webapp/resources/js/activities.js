'use strict';

/* Controllers */

var activitiesApp = angular.module('activitiesApp', ['ngGrid', 'ui.bootstrap']);

var type;
activitiesApp.controller('activitiesStMartin',
		function($scope, $http, $modal,$log) {
	
	 /*$.getScript("part.js", function(){

		  alert("Script loaded and executed.");
		   // Here you can use anything you defined in the loaded script
		   });*/
	
           var url = window.location.toString();
           url.match(/\?(.+)$/);
           var params= RegExp.$1;
           
           var startIndex = params.indexOf("=");
           var projectCode = params.substring(startIndex+4,startIndex+8);
           var lastIndex = params.lastIndexOf("=");
           var selectionCode = params.substring(lastIndex+4,lastIndex+7);
           
           // TAB SELECTION
          
           
           $scope.beneficiarySelected=function(){
        	   personTypeFunction("BE");
           };
           $scope.volunteersSelected=function(){
        	   personTypeFunction("VO");
           };
           $scope.socialWorkersSelected=function(){
        	   personTypeFunction("SW");
           };           
           $scope.physioterapistsSelected=function(){
        	   personTypeFunction("PH");
           };
           

           
			// ROW GRID SELECTION
			$scope.selectRowButton = function(size) {
				type="modify";
				if ($scope.mySelections[0] == null
						|| $scope.mySelections[0] == "") {
					alert("No person has been selected!");
				} else {					
					openModal(size);					
				}
				$scope.beneficiario = $scope.mySelections[0];
			};

			$scope.deleteRowButton = function(size) {
				type="delete";
				if ($scope.mySelections[0] == null
						|| $scope.mySelections[0] == "") {
					alert("No person has been selected!");
				} else {
					type="delete";
					openModal(size);					
				}
			};

			// ROW INSERT
			$scope.insertRowButton = function(size) {
				type="insert";
				openModal(size);			 				
			};
			
        	$scope.master = {};

			
			// HTTP services
			$http.get('../views/citiesList').success(function(data) {
					$scope.citiesList=data;								 	
				 });
			
			$scope.deleted = function() {
				$http({
					url : '../views/deletePerson',
					method : 'POST',
					data : $scope.beneficiario
				}).success(function(data) {
					$scope.messages = data.messages;
					location.reload();
					alert("cancelled!");
				});
			};

			$scope.projectPerson = {
			    "projectCode" : projectCode,
				"personCode" : "BE"
			};

			/** FUNZIONE PER FARE LA PERSISTENZA SUL BACKEND */
			function update($newScope) {
				$scope.beneficiario = $newScope;
				// sono in delete
				if(!$newScope.firstName && $newScope.personId){
					$http({
						url : '../views/deletePerson',
						method : 'POST',
						data : $scope.beneficiario
					}).success(function(data) {
						$scope.messages = data.messages;
						location.reload();
						alert("cancelled!");
					});
				}
				else{
				var arrayData = {
					person : $scope.beneficiario,
					projectPerson : $scope.projectPerson
				};

				$http({
					url : '../views/inserisciBen',
					method : 'POST',
					data : arrayData
				}).success(function(data) {
					$scope.messages = data.messages;
					location.reload();
					alert("inserito!");
				});
			  }
			};

			$http.post('../views/listaBen', $scope.projectPerson).success(
					function(data) {
						$scope.beneficiari = data;
					});

			$scope.isUnchanged = function(beneficiario) {
				return angular.equals(beneficiario, $scope.master);
			};

			// NG-GRID conf. options

			$scope.mySelections = [];

			$scope.filterOptions = {
				filterText : ''
			};

			$scope.gridOptions = {
				data : 'beneficiari',
				enableCellEdit : false,
				enableRowSelection : true,
				multiSelect : false,
				showColumnMenu : true,
				showFilter : true,
				selectedItems : $scope.mySelections,
				pagingOptions : $scope.pagingOptions,
				filterOptions : $scope.filterOptions,
				columnDefs : [ {
					field : 'firstName',
					displayName : 'Name'
				}, {
					field : 'lastName',
					displayName : 'Surname'
				}, {
					field : 'address',
					displayName : 'Address'
				} ,{
					field : 'city',
					displayName : 'City'
				},{
					field : 'telephone',
					displayName : 'Tel.'
				},{
					field : 'email',
					displayName : 'E-mail'
				}],
				enablePaging : true,
				showFooter : true
			};
			// $scope.reset();

			$scope.pagingOptions = {
				pageSizes : [ 250, 500, 1000 ],
				pageSize : 250,
				currentPage : 1
			};
			$scope.setPagingData = function(data, page, pageSize) {
				var pagedData = data.slice((page - 1) * pageSize, page
						* pageSize);
				$scope.myData = pagedData;
				$scope.totalServerItems = data.length;
				if (!$scope.$$phase) {
					$scope.$apply();
				}
			};

			
			
			
			
			
			
			
			
			
			
			
			
// JS function utilities
			
			
			
			function personTypeFunction(personType) {
				$scope.projectPerson = {
					    "projectCode" : projectCode,
						"personCode"  : personType
					};
				$http.post('../views/listaBen', $scope.projectPerson).success(
						function(data) {
							$scope.beneficiari = data;

						});
			}
			
			//datePicker
				  $scope.today = function() {
				    $scope.dt = new Date();
				  };
				  $scope.today();

				  $scope.clear = function () {
				    $scope.dt = null;
				  };

				  // Disable weekend selection
				  $scope.disabled = function(date, mode) {
				    return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
				  };

				  $scope.toggleMin = function() {
				    $scope.minDate = $scope.minDate ? null : new Date();
				  };
				  $scope.toggleMin();

				  $scope.open = function($event) {
				    $event.preventDefault();
				    $event.stopPropagation();

				    $scope.opened = true;
				  };

				  $scope.dateOptions = {
				    formatYear: 'yy',
				    startingDay: 1
				  };

				  $scope.initDate = new Date('2016-15-20');
				  $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
				  $scope.format = $scope.formats[0];

				  //MODAL DIALOG
				  function openModal(size){
					  var modalContent= 'myModalContent.html';
					    if(type == "delete"){
					    	modalContent= 'myModalContentDelete.html';
					    }
						$modal.open({
					        templateUrl: modalContent,
					        controller: ModalInstanceCtrlUpdate,
					        size: size,
					        resolve: {
					          items: function () {
					        	  if(type == "insert"){
					        		
					        		$("#personId").attr("value", null);
					  				$("#firstNameId").attr("value", null);
					  				$("#lastNameId").attr("value", null);
					  				$("#cityId").attr("value", null);
					  				$http.get('../views/citiesList').success(function(data) {
											$scope.citiesList=data;								 	
										 });
					  				 
					  				var array = {"people": null, "cities": $scope.citiesList};
					  				return array;
					  				
					        	  }
					        	  else if (type == "modify"){
					        		$("#personId").attr("value",$scope.mySelections[0].personId);
					            	$("#firstNameId").attr("value",$scope.mySelections[0].firstName);
						            $("#lastNameId").attr("value",$scope.mySelections[0].lastName);
						            $("#cityId").attr("value", $scope.mySelections[0].city);
						            $http.get('../views/citiesList').success(function(data) {
										$scope.citiesList=data;										
										 $scope.options = [
										                   { label: 'one', value: 1 },
										                   { label: 'two', value: 2 }
										                 ];
									 });
						            return {"people": $scope.mySelections[0], "cities": $scope.citiesList};
						            
					        	  }	  
					        	  else{
					        		  $scope.mySelections[0].firstName=null;
					        		  $("#personId").attr("value",$scope.mySelections[0].personId);
					        		  return {"people": $scope.mySelections[0]};					        		  
					        	  }
					        	  
					          }
					        }
					      });
						
						

					}


				  
				  var ModalInstanceCtrlUpdate = function ($scope, $modalInstance, items) {
	  
					  $scope.items = items;
					  $scope.ok = function () {
						update($scope.$$childTail.items.people);
						$modalInstance.dismiss('cancel');
					    
					  };

					  $scope.cancel = function () {
					    $modalInstance.dismiss('cancel');
					  };
					};
					
		});
