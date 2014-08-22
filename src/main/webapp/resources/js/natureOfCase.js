'use strict';

/* Controllers */

var activitiesApp = angular.module('activitiesApp', ['ngGrid', 'ui.bootstrap']);

var type;
activitiesApp.controller('activitiesStMartin',
		function($scope, $http, $modal,$log) {
	
	
	      
	       var personType = null;

		   var flagBeneficiarySelected = 'false';
		       
           var url = window.location.toString();
           url.match(/\?(.+)$/);
           var params= RegExp.$1;
           
           var startIndex = params.indexOf("=");
           var projectCode = params.substring(startIndex+4,startIndex+8);
           var lastIndex = params.lastIndexOf("=");
           var selectionCode = params.substring(lastIndex+4,lastIndex+7);
           
           setDepartment();
           
/**********************************************************************************************************************************************/
 // This function gets the nature of cases type

           $scope.projectPerson = {"projectCode" : projectCode,
                   "personCode"  : 'BE'
                  };
           $http.post('../views/natureOfCasesList',$scope.projectPerson).success(function(data) {
				$scope.natureOfCasesLista=data;
			 });
           
           $http.post('../views/natureOfCasesStatusList',$scope.projectPerson).success(function(data) {
				$scope.natureOfCasesStatusList=data;
			 });
           
          
           
           $http.post('../views/listaBen', $scope.projectPerson).success(
					function(data) {
						$scope.beneficiaries = data;
					});
	
/**********************************************************************************************************************************************/
// This function is for filtering beneficiary, person in charge, and period
           
			var selectedBeneficiary;
		    var dateStart;
		    var dateEnd;
		    var natureOfCase;
		   	var status;
		   	
			$scope.selectFilter = function(selectedPerson, dateStartPeriod, dateEndPeriod, natureOfCaseParam, statusParam) {	
				  
				    if(selectedPerson==null)selectedBeneficiary = null;
				    if(selectedPerson!=null && selectedPerson!='' && selectedPerson.personId>0) {
				    	selectedBeneficiary = selectedPerson.personId;
				    }
				    
                    if(dateStartPeriod!='') dateStart= dateStartPeriod;
                    if(dateEndPeriod!='') dateEnd= dateEndPeriod;
                    if(natureOfCaseParam!='') natureOfCase=natureOfCaseParam;
                    if(statusParam!='') status=statusParam;
					
					var filter = {"personIdBeneficiary":selectedBeneficiary,
							              "dateStart": dateStart,
							              "dateEnd": dateEnd,
							              "natureOfCase": natureOfCase,
							              "natureOfCaseStatus": status};
					$http.post('../views/natureOfCasePersonList', filter).success(
							function(data) {						
								$scope.natureOfCasePersonList = data;								
							});		
			};
			
/**********************************************************************************************************************************************/
// These functions regards the grid row configuration for the activity form
		
			$scope.mySelections = [];

			$scope.filterOptions = {
				filterText : ''
			};

			$scope.gridOptions = {
				data : 'natureOfCasePersonList',
				enableCellEdit : false,
				enableRowSelection : true,
				enableCellSelection: false,
				multiSelect : false,
				showColumnMenu : true,
				showFilter : true,
				selectedItems : $scope.mySelections,
				pagingOptions : $scope.pagingOptions,
				filterOptions : $scope.filterOptions,
				columnDefs : [ {
					field : 'natureOfCase',
					displayName : 'Nature Of Case'
				}, {
					field : 'insertDate',
					displayName : 'Insertion Date',
					cellFilter: "date:'dd-MM-yyyy'"
				}, {
					field : 'status',
					displayName : 'Status'
				}],
				enablePaging : true,
				showFooter : true
			};
			
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

/**********************************************************************************************************************************************/		
/**********************************************************************************************************************************************/
// These functions regards the grid row selection
						

						$scope.deleteRowButton = function(size) {
							type="delete";
							if ($scope.mySelections[0] == null
									|| $scope.mySelections[0] == "") {
								alert("No case has been selected!");
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
	
				  
/**********************************************************************************************************************************************/
// This function opens a modal dialog related to the grid data, and initializes the form values			  

				  function openModal(size){
					  
					  var modalContent= 'myModalContent.html';
					    if(type == "delete"){
					    	modalContent= 'myModalContentDelete.html';
					    }
						$modal.open({
					        templateUrl: modalContent,
					        controller: ModalInstanceCtrlUpdate,
					        windowClass: 'app-modal-window',
					        resolve: {
					          items: function () {
					        	  
					        	  setDepartment();
								  		        	 
							      $http.post('../views/listaBen', $scope.projectPerson).success(
													function(data) {
														$scope.beneficiaries = data;
													});								
					        	  if(type == "insert"){									        		  
					        		$("#activityId").attr("value", null);		
					        		$("#activityDate").attr("value", null);
					        		
					        		return {"date":null, "natureOfCasesList": $scope.natureOfCasesLista, "natureOfCasesStatusList": $scope.natureOfCasesStatusList,
				  						    "beneficiaries":$scope.beneficiaries,"isCPPD":$scope.isCPPD,"isCPPR":$scope.isCPPR};				  				
					  				
					        	  }
					        	    
					        	  else{
					        		  $scope.mySelections[0].natureOfCase=null;
					        		  return {"natureOfCasePerson": $scope.mySelections[0]};					        		  
					        	  }
					        	  
					          }
					        }
					      });
					}

/**********************************************************************************************************************************************/
// This function defines a new controller for the modal dialog and it is called when pressed ok or cancel after filling the form		  

				  
				  var ModalInstanceCtrlUpdate = function ($scope, $modalInstance, items) {
	  
					  $scope.items = items;
					  
					  
					  $scope.ok = function () {		
						$scope.$$childTail.items.natureOfCasePerson.insertDate = $scope.$$childTail.items.date;
						update($scope.$$childTail.items);
						$modalInstance.dismiss('cancel');				    
					  };

					  $scope.cancel = function () {
					    $modalInstance.dismiss('cancel');					
					  };
					  
					  
					 
					};

/*********************************************************************************************************************************************/
//This Function is called when I click ok on the form, and it handles INSERT,UPDATE, AND DELETE of a record of the table activity
				function update($newScope) {	
					$scope.natureOfCasePerson = $newScope.natureOfCasePerson;
						//	$scope.natureOfCasePerson = { "natureOfCase":$newScope.natureOfCaseObject, "status":$newScope.natureOfCaseStatus, "insertDate":$newScope.natureOfCaseDate  };							
							$scope.beneficiary = $newScope.objectBEN;
						
							
							// delete
							if(!$newScope.natureOfCasePerson.natureOfCase && !$newScope.natureOfCasePerson.natureOfCaseDate && $newScope.natureOfCasePerson.natureOfCasePersonId){
								$http({
									url : '../views/deleteNatureOfCase',
									method : 'POST',
									data : $scope.natureOfCasePerson
								}).success(function(data) {
									$scope.messages = data.messages;
									location.reload();
									alert("cancelled!");
								});
							}
							// insert or update
							else{
								
							    var globalPerson = {
							    	natureOfCasePerson : $scope.natureOfCasePerson,
							     	person : $scope.beneficiary
							    };

							$http({
								url : '../views/insertNatureOfCase',
								method : 'POST',
								data : globalPerson
							}).success(function(data) {
								$scope.messages = data.messages;
								location.reload();
								alert("inserito!");
							});
					   }
				};
/*********************************************************************************************************************************************/	
			function setDepartment(){
				$scope.isCPPD = false;	
	        	  $scope.isCPPR = false;
				  if (projectCode=='CPPD') $scope.isCPPD = true;
				  if (projectCode=='CPPR') $scope.isCPPR = true;
			}	
				
});					
