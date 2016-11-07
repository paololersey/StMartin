'use strict';
var type;
define(['app'], function (app) {
app.controller('ActivitiesPageCtrl',['$scope', '$http', '$modal', '$log', '$location', 'commonFactory','commonMethodFactory',
		function($scope, $http, $modal, $log, $location, commonFactory, commonMethodFactory) {
    
	       var personType = null;

		   var flagPersonInChargeSelected = 'false';
		   var flagBeneficiarySelected = 'false';
           var projectCode = commonFactory.selectedDepartment;

           //navigation check: if I refresh the page the code won't be available, so I send to to the login page
           if(!projectCode){
        	   $location.path("/loginPage");
        	   return;
           }
/**********************************************************************************************************************************************/
// These functions regards the tab selection
           
           $scope.volunteerSelected=function(){
        	   personType = 'VO';
        	   personTypeFunction(personType);
        	   $scope.selectActivityfiltered(null);
        	   
           };
           $scope.socialWorkerSelected=function(){
        	   personType = 'SW';
        	   personTypeFunction(personType);
           };           
           $scope.centerManagerSelected=function(){
        	   personType = 'CM';
        	   personTypeFunction(personType);
           };
           $scope.physioterapistSelected=function(){
        	   personType = 'PH';
        	   personTypeFunction(personType);
           };
           
           if(personType){
           personTypeFunction(personType);           
           }
           setFlags();
 /**********************************************************************************************************************************************/
 // This function gets the referrralType,activityType,interventionType,statesList (for levelChange)
         
           $http.post('../views/referralType', projectCode).success(function(data) {
				$scope.referralType=data;
			 });
                   
           $http.post('../views/activityType', projectCode).success(function(data) {          				
          				$scope.activityType=data;
          			 });
 
           $http.post('../views/interventionType', projectCode).success(function(data) {          				
 				$scope.interventionType=data;
 			 });
		
           $http.post('../views/statesList',projectCode).success(function(data) {
				
				$scope.personStateNames=data;
			 });
/**********************************************************************************************************************************************/
// This function is for filtering beneficiary, person in charge, and period

            
			var selectedBeneficiary=null;
		    var selectedPersonIncharge=null;
		    var dateStart=null;
		    var dateEnd=null;
		    var activityType=null;
		    var referral=null;
		    var intervention=null;
		    var filterActivity=null;
		   		    
			$scope.selectActivityfiltered = function(selectedBeneficiaryParam, selectedPersonInchargeParam, dateStartPeriod, dateEndPeriod, 
					                                 activityTypeParam, referralParam, interventionParam) {	
				  
				    if(selectedBeneficiaryParam==null) selectedBeneficiary = null;
				    if(selectedBeneficiaryParam!=null && selectedBeneficiaryParam!='' && selectedBeneficiaryParam.personId>0) {
				    	selectedBeneficiary = selectedBeneficiaryParam.personId;
				    }
				    if(selectedPersonInchargeParam==null) selectedPersonIncharge = null;
				    if(selectedPersonInchargeParam!=null && selectedPersonInchargeParam!='' && selectedPersonInchargeParam.personId>0) {
				    	selectedPersonIncharge = selectedPersonInchargeParam.personId;
				    }
                    if(dateStartPeriod!='') dateStart= dateStartPeriod;
                    if(dateEndPeriod!='') dateEnd= dateEndPeriod;
                    if(activityTypeParam!='') activityType=activityTypeParam;
                    if(referralParam!='') referral=referralParam;
                    if(interventionParam!='') intervention=interventionParam;
					
					filterActivity = {"personIdPersonInCharge":selectedPersonIncharge,
							              "personIdBeneficiary":selectedBeneficiary,
							              "dateStart": dateStart,
							              "dateEnd": dateEnd,
							              "activityType": activityType,
							              "referral": referral,
							              "intervention":intervention,
							              "projectCode":projectCode};
					commonMethodFactory.getActivityList(filterActivity).then(function(object){											
						commonFactory.activitiesData = object.data;
						fillActivitiesNotes(object.data);						
					});
							
			};
/**********************************************************************************************************************************************/
// This function retrieve data about beneficiary and PersonInCharge
			
			function retrievePeople (person,size){
				
                $scope.projectPerson = {"personId" : person.personId,
		                                 "personCode"  : 'BE'
	                                    };
                $http.post('../views/personByPersonId', $scope.projectPerson).success(
		                     function(dataBEN) {
		                    	 if(dataBEN!=null && dataBEN.length!=0){
			                                $scope.beneficiarySelected = dataBEN;
			                                flagBeneficiarySelected = 'true';
			                               
		                    	 }
		                     });

				 $scope.projectPerson = { "personId" : person.personId,
                        				  "personCode"  : personType
              	 };
				 $http.post('../views/personByPersonId', $scope.projectPerson ).success(
							function(dataPCH) {
								if(dataPCH!=null && dataPCH.length!=0){
								$scope.personInChargeSelected = dataPCH;
								flagPersonInChargeSelected = 'true';
								//if($scope.beneficiaryToShow==false) flagBeneficiarySelected=true;
								flagBeneficiarySelected="true";
								if(flagPersonInChargeSelected==='true' && flagBeneficiarySelected==='true'){
									 openModal(size);
								 }
								}
							});
				 
					 
			 };	
			
/**********************************************************************************************************************************************/
// These functions regards the grid row configuration for the activity form
		
			$scope.mySelections = [];

			$scope.activitiesNotes={};

			$scope.totalServerItems = 0;
			$scope.pagingOptions = {
				        pageSizes: [10, 50, 100, 200],
				        pageSize: 10,
				        currentPage: 1
			};	

			 
			$scope.setPagingData = function(data, page, pageSize) {	
			        var pagedData = data.slice((page - 1) * pageSize, page * pageSize);
			        fillActivitiesNotes(pagedData);
			        //$scope.activitiesNotes = pagedData;
			        $scope.totalServerItems = data.length;
			        if (!$scope.$$phase) {
			            $scope.$apply(); 
			        }
			    };
			
		    $scope.$watch('pagingOptions', function (newVal, oldVal) {
		        if (newVal !== oldVal && newVal.currentPage !== oldVal.currentPage) {	
		        	$scope.setPagingData(commonFactory.activitiesData,$scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);        
		        }
		    }, true);
		    
		    $scope.$watch('filterOptions', function (newVal, oldVal) {
		        if (newVal !== oldVal) {
		        	 var data = commonFactory.activitiesNotes.filter(function(item) {
		                 return JSON.stringify(item).toLowerCase().indexOf($scope.filterOptions.filterText.toLowerCase()) != -1;
		             });
		        	$scope.setPagingData(data,$scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
		        }
			}, true);

			$scope.gridOptions = {
				data : 'activitiesNotes',
				enableCellEdit : false,
				enablePaging : true,
				totalServerItems:'totalServerItems',
				enableRowSelection : true,
				enableCellSelection: false,
				multiSelect : false,
				showColumnMenu : true,
				showFilter : true,
				selectedItems : $scope.mySelections,
				pagingOptions : $scope.pagingOptions,
				filterOptions: $scope.filterOptions,
				enableColumnResize :true,
				columnDefs : [ {
					field : 'activity.activityType',
					displayName : 'Activity Type'
				}, {
					field : 'activity.activityDate',
					displayName : 'Activity Date',
					cellFilter: "date:'dd-MM-yyyy'"
				}, {
					field : 'activity.intervention',
					displayName : 'Intervention'
				}, {
					field : 'activity.referral',
					displayName : 'Referral'
				},{
					field : 'note.noteDescription',
					displayName : 'NoteDescription',
					width: '0 px;'
				},{
					field : 'note',
					displayName: 'Note',
					enableCellEditOnFocus: true,
					cellTemplate: '<div><button class="glyphicon glyphicon-pencil" ng-click="openModalNote(row)"></button></div>'
				}],
				showFooter : true
			};
		

/**********************************************************************************************************************************************/		
/**********************************************************************************************************************************************/
// These functions regards the grid row selection
							$scope.backButton = function(size) {
								$location.path("/homePage");
							};
							
			$scope.selectRowButton = function(size) {
							type="modify";
							if ($scope.mySelections[0] == null
									|| $scope.mySelections[0] == "") {
								alert("No activity has been selected!");
							} else {				
								var ben = $scope.mySelections[0].activity.personActivities[1];
								retrievePeople($scope.mySelections[0].activity.personActivities[0],size);
								if(ben!=null && ben!='')retrievePeople(ben,size);
												
							}
							//$scope.activity = $scope.mySelections[0].activity;
						};

						$scope.deleteRowButton = function(size) {
							type="delete";
							if ($scope.mySelections[0] == null
									|| $scope.mySelections[0] == "") {
								alert("No person has been selected!");
							} else {
								type="delete";
								//$scope.activity = $scope.mySelections[0].activity;
								openModal(size);					
							}
						};

						// ROW INSERT
						$scope.insertRowButton = function(size) {
							type="insert";
							openModal(size);			 				
						};
						
/**********************************************************************************************************************************************/		
/**********************************************************************************************************************************************/
// These functions retrieve the records of personInCharge & beneficiaries		
			function personTypeFunction(personTypeParameter) {
				$scope.projectPerson = {
					    "projectCode" : projectCode,
						"personCode"  : personTypeParameter
					};
				$http.post('../views/listaBen', $scope.projectPerson).success(
						function(data) {
							$scope.personInCharge = data;

						});
				$scope.projectPerson = {
					    "projectCode" : projectCode,
						"personCode"  : "BE"
					};
				$http.post('../views/listaBen', $scope.projectPerson).success(
						function(data) {
							$scope.beneficiaries = data;

						});
			}
								
			
      	  
      	  	  
/**********************************************************************************************************************************************/
// This function opens a modal dialog related to the grid data, and initializes the form values			  

				  function openModal(size){
					  var modalContent= '/StMartin/root/view/dialog/insertUpdateActivitiesDialog.html';
					
					    if(type == "delete"){
					    	modalContent= 'myModalContentDelete.html';
					    }
						$modal.open({
					        templateUrl: modalContent,
					        controller: modalActivitiesController,
					        windowClass: 'app-modal-window',
					        resolve: {
					          items: function () {
					        	  
					        	 
					        	  $scope.projectPerson = {
										    "projectCode" : projectCode,
											"personCode"  : personType
										   };
					        	  $http.post('../views/listaBen', $scope.projectPerson).success(				        			  
											function(data) {
												$scope.personInCharge = data;

											});
									       $scope.projectPerson = {
												    "projectCode" : projectCode,
													"personCode"  : 'BE'
												   };
							      $http.post('../views/listaBen', $scope.projectPerson).success(
													function(data) {
														$scope.beneficiaries = data;

													});
								
					        	  if(type == "insert"){				
					        		  
					        		$("#activityId").attr("value", null);		
					        		$("#activityDate").attr("value", null);
					        		
					        		
					        		
					        		return {"activity":null, "activityTypeList": $scope.activityType, "referralList": $scope.referralType, "interventionTypeList": $scope.interventionType,
				  						     "personInCharge":$scope.personInCharge,"beneficiaries":$scope.beneficiaries, "personStateNames":$scope.personStateNames, "date":null,
				  						     "isCPCN":$scope.isCPCN,"isCPPD":$scope.isCPPD};				  				
					  				
					        	  }
					        	  else if (type == "modify"){
					        		
						            return {"activity": $scope.mySelections[0].activity, "activityTypeList": $scope.activityType,"referralList": $scope.referralType, "interventionTypeList": $scope.interventionType,
					                    	"personInCharge":$scope.personInChargeSelected,"beneficiaries":$scope.beneficiarySelected,"personStateNames":$scope.personStateNames,"date":$scope.mySelections[0].activity.activityDate,
					                	    "isCPCN":$scope.isCPCN,"isCPPD":$scope.isCPPD};
					        		
					        	  }	  
					        	  else{
					        		  $scope.mySelections[0].activity.referral=null;
					        		  $scope.mySelections[0].activity.activityType=null;
					        		  $("#activityId").attr("value",$scope.mySelections[0].activityId);
					        		  return {"activity": $scope.mySelections[0].activity};					        		  
					        	  }
					        	  
					          }
					        }
					      });
					}

/**********************************************************************************************************************************************/
// This function defines a new controller for the modal dialog and it is called when pressed ok or cancel after filling the form		  

				  
				  var modalActivitiesController = function ($scope, $modalInstance, items) {
	  
					  $scope.items = items;
					// flag beneficiaryNeeded      
					 
						  $scope.activityTypeChange = function() {
							if (items.activity != null
									&& items.activity.activityType != null) {

								var filter= {"activityType":items.activity.activityType, "projectCode": projectCode};
								items.beneficiaryToShow = false;
								$http.post('../views/beneficiaryNeededForActivityType', filter).success(
			    						function(data) {
			    							if(data!=null && data!="" && data=='Y'){
			    							items.beneficiaryToShow = true;
			    							}
			    						});
								
								
								if( items.activity.activityType == 'THERAPY'){
									items.applianceToShow = true;
								}else{
									items.applianceToShow = false;
								}
							}
						};
						$scope.items.beneficiaryToShow=$scope.beneficiaryToShow;
					 
					  
		        	 
					  $scope.ok = function () {
					    $scope.$$childTail.items.activity.activityDate = ($scope.$$childTail.items.date);
						update($scope.$$childTail.items);
						$modalInstance.dismiss('cancel');	
					  };

					  $scope.cancel = function () {
					    $modalInstance.dismiss('cancel');
					    $scope.activitiesNotes = commonFactory.activitiesData;													
					  };
					  
					  
					 
					};

/*********************************************************************************************************************************************/
//This Function is called when I click ok on the form, and it handles INSERT,UPDATE, AND DELETE of a record of the table activity
				function update($newScope) {	
					        $newScope.activity.levelChange = $newScope.levelChange;
					        $newScope.activity.projectCode=projectCode;  
					        
					        var globalActivity = {
								    activity : $newScope.activity,
							     	beneficiary : $newScope.objectBEN,
							     	personInCharge : $newScope.objectPCH,
							     	levelChange : $newScope.levelChange
							    };
							// delete
							if(!globalActivity.activity.referralType && !globalActivity.activity.activityType && globalActivity.activity.activityId){
								$http({
									url : '../views/deleteActivity',
									method : 'POST',
									data : globalActivity
								}).success(function(data) {
									$scope.messages = data.messages;
									alert("cancelled!");
									commonMethodFactory.getActivityList(filterActivity).then(function(object){																					
										commonFactory.activitiesData = object.data;
										fillActivitiesNotes(object.data);						
									});
									
								});
							}
							// insert or update
							else{
								
								
                            
							$http({
								url : '../views/insertActivity',
								method : 'POST',
								data : globalActivity
							}).success(function(data) {
								$scope.messages = data.messages;
								alert("insert/update succeeded!");
								commonMethodFactory.getActivityList(filterActivity).then(function(object){											
									commonFactory.activitiesData = object.data;
									fillActivitiesNotes(object.data);						
								});
								
							});
					   }
				};
		
	
					
/**********************************************************************************************************************************************/
// This function opens a modal dialog note

    $scope.openModalNote=function(row){
    	var size ='';
    	$scope.isNoteAlreadyWritten = false;
    	$modal.open({
            templateUrl: 'modalNote.html',
            controller: ModalInstanceNoteGrid,
            size: size,
            resolve: {
              notes: function () {
            	  $scope.activityId = row.entity.activity.activityId; 
            	  $scope.noteDescription = row.entity.note;            	  
            	  if($scope.noteDescription!=null && $scope.noteDescription!="") $scope.isNoteAlreadyWritten=true;
            	  return {"noteDescription":$scope.noteDescription, "activityId": $scope.activityId,"isNoteAlreadyWritten": $scope.isNoteAlreadyWritten};
					
              }
            }
          });
    };
					
/*********************************************************************************************************************************************/
// This function defines a new controller for the modal dialog note, and diaplys a note grid

			  
			  var ModalInstanceNoteGrid = function ($scope, $modalInstance, notes) {
  
				  $scope.notes = notes;
				  $scope.myNotesSelections = [];

					$scope.gridNotesOptions = {
							data : 'notes.notesList',
							enableCellEdit : false,
							enableRowSelection : true,
							enableCellSelection: true,
							multiSelect : false,
							showColumnMenu : true,
							showFilter : true,
							selectedItems : $scope.myNotesSelections,
							columnDefs : [ {
								field : 'notes.notesList.noteDescription',
								displayName : 'Note'
							}]
						};
						
	
				  $scope.cancel = function () {
				    $modalInstance.dismiss('cancel');							
				  };
				  
				  $scope.insertNote =  function() {
					  $modalInstance.dismiss('cancel');	
					  openModalAddNote($scope.notes.activityId);
				  };
				  
				  $scope.deleteNote =  function() {					  
					  var note = new Object();
					  note.activityId=$scope.notes.activityId;
					  $http({
							url : '../views/deleteNote',
							method : 'POST',
							data : note
						}).success(function(data) {
							$scope.messages = data.messages;
							commonMethodFactory.getActivityList(filterActivity).then(function(object){						
								$scope.activities = object.data;						
								commonFactory.activitiesData = object.data;
								fillActivitiesNotes($scope.activities);						
							});
						});
					  
				  };
				  

/*********************************************************************************************************************************************/
// insertNote
 				function openModalAddNote(activityId){
 			    	var size ='';
 			    	$modal.open({
 			            templateUrl: 'modalAddNote.html',
 			            controller: ModalInstanceAddNote,
 			            size: size,
 			            resolve: {
 			              addNotes: function () {		            	  
 			            	 return { "activityId": activityId}
 			              }
 			            }
 			          });
 			    };
 
}

			    
/*********************************************************************************************************************************************/
// This function defines a new controller for the modal dialog note, and diaplys a note grid

			 			  
 			  var ModalInstanceAddNote = function ($scope, $modalInstance, addNotes) {
   
 				  $scope.addNotes = addNotes; 				  						
 				  $scope.ok = function () {	
 					 $http.post('../views/insertNote', addNotes).success(
	    						function(data) {
	    							$scope.notes = data;
	    						});
 					$modalInstance.dismiss('cancel');
 					commonMethodFactory.getActivityList(filterActivity).then(function(object){						
						$scope.activities = object.data;						
						commonFactory.activitiesData = object.data;
						fillActivitiesNotes($scope.activities);						
					});
 				  };

 				  $scope.cancel = function () {
 				    $modalInstance.dismiss('cancel');							
 				  };
 				  
 				  $scope.insertNote =  function() {
 					  openModalAddNote($scope.notes.activityId);
 					};
 				};

/**********************************************************************************************************************************************/
// Report
 						
 				$scope.report = function(){
 					var activityArray=[];
 					var noteArray=[];
 					for(var i=0;i<$scope.activitiesNotes.length;i++){
 						activityArray.push($scope.activitiesNotes[i].activity);
 						noteArray.push($scope.activitiesNotes[i].note);
 					}
 					var globalActivityPdf = {"activityList":activityArray, "noteList": noteArray, "filter": filterActivity};
 					$http.post("../pdf/createPdf",globalActivityPdf,{ responseType: 'arraybuffer' }).success(function(data){
 						 var file = new Blob([data], { type: 'application/pdf' });
 		                 var fileURL = URL.createObjectURL(file);
 		                 window.open(fileURL);    
// 						
// 						if(data!=null && data!=""){
// 						    	openMessageDialog(data);
// 						    }
// 						  
 					});
 				}; 				
 				
/*********************************************************************************************************************************************/
// flag personType

      		function setFlags(){
      			$scope.isBeneficiaryNotCPPR=false;
      			$scope.isVolunteerNotCPPR=false;
      			$scope.isCPPRBeneficiary=false;
      			$scope.isBeneficiary=false;
      			$scope.isCPPR=false;
      			$scope.isCPPD=false;
      			$scope.isCPCN=false;
      			  
      			  if(personType=="BE" && projectCode=="CPPR") {
      				  $scope.isCPPRBeneficiary=true;
      			  }
      			  if(projectCode=="CPCN") {
      				  $scope.isCPCN=true;
      			  }
      			  if(projectCode=="CPPR") {
      				  $scope.isCPPR=true;
      			  }
      			  if(projectCode=="CPPD") {
      				  $scope.isCPPD=true;
      			  }
      			  if(personType=="BE") {
      				  $scope.isBeneficiary=true;
      			  }
      			  if(personType=="VO" && projectCode!="CPPR") {
      				  $scope.isVolunteerNotCPPR=true;
      			  }
      			  if(personType=="BE" && projectCode!="CPPR") {
      				  $scope.isBeneficiaryNotCPPR=true;
      			  }
      			 
      		};

/**********************************************************************************************************************************************/
// Open error dialog	
    			function openMessageDialog(code){
    				$modal.open({
    					templateUrl: 'messageDialog.html',
    					controller: ModalMessageDialog,
    			    	size: "",
    					resolve: {		          
    				          items: function () {
    				        	  if(code=="filePdfAlreadyInUse"){
    				        		  $scope.message = "Please, store and close the pdf that is already in use";
    				        	  }
    				        	  if(code=="okPdf"){
    				        		  $scope.message = "Pdf correctly generated"; 
    				        	  }
    				        	  
    				        	  var message = {"message":$scope.message};
    				        	  return message;
    				          }
    					}
    				});			
    			}
    			
    			var ModalMessageDialog= function ($scope, $modalInstance, items) {				  
    				  
    				$scope.items = items;	
    				$scope.cancel = function () {
    				    $modalInstance.dismiss('cancel');
    				  };

    		};		
/**********************************************************************************************************************************************/
    		
    		function fillActivitiesNotes (activities){
    			var activitiesNotesArray=[];
    			for (var i=0;i<activities.length;i++ ){	   				
    				var act=activities[i][0];
    				var note="";
    				if (activities[i][1]==null){
    					act = activities[i][0];						
    				}else{
    					note=activities[i][1].noteDescription;
    				}   
    				activitiesNotesArray.push({"activity":act,"note":note});
    			};
    			$scope.activitiesNotes = activitiesNotesArray;	
    		};
	}]);
});					
