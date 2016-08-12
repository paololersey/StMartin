'use strict';

var type;
define(['app'], function (app) {
	app.controller('NatureOfCasePageCtrl',['$scope', '$http', '$modal', '$log', '$location', 'commonFactory','commonMethodFactory', function($scope, $http, $modal, $log, $location, commonFactory,commonMethodFactory) {

	
           var projectCode = commonFactory.selectedDepartment;
           //navigation check: if I refresh the page the code won't be available, so I send to to the login page
           if(!projectCode){
        	   $location.path("/loginPage");
        	   return;
           }
           
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
           
           $http.post('../views/delayedMilestoneList', projectCode).success(function(data) {          				
   	        var arrayDelayedMilestone=[];
 				for (var i=0;i<data.length;i++ ){					
 					arrayDelayedMilestone.push(data[i].delayedMilestone);
 				}
 				$scope.delayedMilestoneList=arrayDelayedMilestone;
         	});
           
           $http.post('../views/listaBen', $scope.projectPerson).success(
					function(data) {
						$scope.beneficiaries = data;
						 $scope.selectFilter(null);
					});
	
/**********************************************************************************************************************************************/
// This function is for filtering beneficiary, person in charge, and period
           
			var selectedBeneficiary=null;
		    var dateStart=null;
		    var dateEnd=null;
		    var natureOfCase=null;
		   	var status=null;
		   	
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
							              "natureOfCaseStatus": status,
							              "projectCode":projectCode};
					
					$http.post('../views/natureOfCasePersonList', filter).success(
							function(data) {						
								$scope.natureOfCasePersonList = data;		
								commonFactory.natureOfCasePersonData = data;
							});		
			};
			
/**********************************************************************************************************************************************/
// These functions regards the grid row configuration for the activity form
		
			$scope.mySelections = [];

			$scope.natureOfCasePersonList={};

			$scope.totalServerItems = 0;
			$scope.pagingOptions = {
				        pageSizes: [10, 50, 100, 200],
				        pageSize: 10,
				        currentPage: 1
			};	

			 
			$scope.setPagingData = function(data, page, pageSize) {	
			        var pagedData = data.slice((page - 1) * pageSize, page * pageSize);
			        $scope.natureOfCasePersonList = pagedData;
			        $scope.totalServerItems = data.length;
			        if (!$scope.$$phase) {
			            $scope.$apply(); 
			        }
			    };
			
		    $scope.$watch('pagingOptions', function (newVal, oldVal) {
		        if (newVal !== oldVal && newVal.currentPage !== oldVal.currentPage) {	
		        	$scope.setPagingData(commonFactory.natureOfCasePersonData,$scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);        
		        }
		    }, true);
			    
			$scope.$watch('filterOptions', function (newVal, oldVal) {
		        if (newVal !== oldVal) {
		        	 var data = commonFactory.natureOfCasePersonData.filter(function(item) {
		                 return JSON.stringify(item).toLowerCase().indexOf($scope.filterOptions.filterText.toLowerCase()) != -1;
		             });
		        	$scope.setPagingData(data,$scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
		        }
			}, true);

			$scope.gridOptions = {
				data : 'natureOfCasePersonList',
				enableCellEdit : false,
				enableRowSelection : true,
				enableCellSelection: false,
				enablePaging : true,
				enableColumnResize :true,
				totalServerItems: 'totalServerItems',
				multiSelect : false,
				showColumnMenu : true,
				showFilter : true,
				selectedItems : $scope.mySelections,
				pagingOptions : $scope.pagingOptions,
				filterOptions : $scope.filterOptions,
				columnDefs : [ {
					field : 'beneficiary',
					displayName : 'Beneficiary'
				},{
					field : 'natureOfCase',
					displayName : 'Nature Of Case'
				}, {
					field : 'insertDate',
					displayName : 'Insertion Date',
					cellFilter: "date:'dd-MM-yyyy'"
				}, {
					field : 'status',
					displayName : 'Status'
				},{
					field : 'delayedMilestone',
					displayName : 'Delayed Milestone'
				}],
				
				showFooter : true
			};
/**********************************************************************************************************************************************/		
/**********************************************************************************************************************************************/
// These functions regards the grid row selection
						
						$scope.backButton = function(size) {
				    		$location.path("/homePage");
				    	};
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
					  
					  var modalContent= '/StMartin/root/view/dialog/insertUpdateNatureOfCaseDialog.html';
					    if(type == "delete"){
					    	modalContent= 'myModalContentDelete.html';
					    }
						$modal.open({
					        templateUrl: modalContent,
					        controller: natureOfCaseDialogController,
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
					        			    "delayedMilestoneList":$scope.delayedMilestoneList,
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

				  
				  var natureOfCaseDialogController = function ($scope, $modalInstance, items) {
	  
					  $scope.items = items;
					  
					  
					  $scope.ok = function () {		
						$scope.$$childTail.items.natureOfCasePerson.insertDate = $scope.$$childTail.items.date;
						var natureOfCaseOther=$scope.$$childTail.items.natureOfCasePerson.natureOfCaseOther;
						if(natureOfCaseOther){
							$scope.$$childTail.items.natureOfCasePerson.natureOfCase=$scope.$$childTail.items.natureOfCasePerson.natureOfCaseOther;
							delete $scope.$$childTail.items.natureOfCasePerson.natureOfCaseOther;
						}
						
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
					var filter={'personType':$scope.projectPerson.personCode,
						    'projectCode':$scope.projectPerson.projectCode};
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
									commonMethodFactory.getNatureOfCasePersonList(filter).then(function(object) {
										$scope.natureOfCasePersonList = object.data;
						    			commonFactory.natureOfCasePersonData=$scope.natureOfCasePersonList;	
						    			alert("The record has been cancelled");
						    		});
									
								});
							}
							// insert or update
							else{						
							    var globalPerson = {
							    	natureOfCasePerson : $scope.natureOfCasePerson,
							     	person : $scope.beneficiary,
							     	projectPerson: $scope.projectPerson
							    };

							$http({
								url : '../views/insertNatureOfCase',
								method : 'POST',
								data : globalPerson
							}).success(function(data) {
								$scope.messages = data.messages;
								commonMethodFactory.getNatureOfCasePersonList(filter).then(function(object) {
									$scope.natureOfCasePersonList = object.data;
					    			commonFactory.natureOfCasePersonData=$scope.natureOfCasePersonList;	
					    			alert("The record has been added");
					    		});
								
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
	}]);
});					
