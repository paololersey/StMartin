'use strict';

/* Controllers */

var activitiesApp = angular.module('activitiesApp', ['ngGrid', 'ui.bootstrap']);

var type;
activitiesApp.controller('activitiesStMartin',
		function($scope, $http, $modal,$log) {
	
	
	      
	       var personType = null;

		   var flagPersonInChargeSelected = 'false';
		   var flagBeneficiarySelected = 'false';
		       
           var url = window.location.toString();
           url.match(/\?(.+)$/);
           var params= RegExp.$1;
           
           var startIndex = params.indexOf("=");
           var projectCode = params.substring(startIndex+4,startIndex+8);
           
/**********************************************************************************************************************************************/
// These functions regards the tab selection
           
           $scope.volunteerSelected=function(){
        	   personType = 'VO';
        	   personTypeFunction(personType);
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
           
 /**********************************************************************************************************************************************/
 // This function gets the referrralType,activityType,interventionType
         
           $http.post('../views/referralType', projectCode).success(function(data) {
				$scope.referralType=data;
			 });
                   
           $http.post('../views/activityType', projectCode).success(function(data) {          				
          				$scope.activityType=data;
          			 });
 
           $http.post('../views/interventionType', projectCode).success(function(data) {          				
 				$scope.interventionType=data;
 			 });
		
/**********************************************************************************************************************************************/
// This function is for filtering beneficiary, person in charge, and period

            
			var selectedBeneficiary;
		    var selectedPersonIncharge;
		    var dateStart;
		    var dateEnd;
		    var activityType;
		    var referral;
		    var intervention;
		   		    
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
					
					var filterActivity = {"personIdPersonInCharge":selectedPersonIncharge,
							              "personIdBeneficiary":selectedBeneficiary,
							              "dateStart": dateStart,
							              "dateEnd": dateEnd,
							              "activityType": activityType,
							              "referral": referral,
							              "intervention":intervention};
					$http.post('../views/activityList', filterActivity).success(
							function(data) {
																
								var activitiesNotesArray=[];
								$scope.activities = data;
								for (var i=0;i<$scope.activities.length;i++ ){					
									var act=$scope.activities[i][0];
									var note="";
									if ($scope.activities[i][1]==null){
										act = $scope.activities[i][0];						
									}else{
										note=$scope.activities[i][1].noteDescription;
									}
									
									activitiesNotesArray.push({"act":act,"note":note});
												
    								$scope.isNoteAlreadyWritten=true;
    							}
								$scope.activitiesNotes = activitiesNotesArray;	
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
								if(flagPersonInChargeSelected==='true' && flagBeneficiarySelected==='true'){
									 openModal(size);
								 }
								}
							});
				 
					 
			 };	
			
/**********************************************************************************************************************************************/
// These functions regards the grid row configuration for the activity form
		
			$scope.mySelections = [];

			$scope.filterOptions = {
				filterText : ''
			};

			$scope.gridOptions = {
				data : 'activitiesNotes',
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
					field : 'act.activityType',
					displayName : 'Activity Type'
				}, {
					field : 'act.activityDate',
					displayName : 'Activity Date',
					cellFilter: "date:'dd-MM-yyyy'"
				}, {
					field : 'act.intervention',
					displayName : 'Intervention'
				}, {
					field : 'act.referral',
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
						$scope.selectRowButton = function(size) {
							type="modify";
							if ($scope.mySelections[0] == null
									|| $scope.mySelections[0] == "") {
								alert("No activity has been selected!");
							} else {				
								retrievePeople($scope.mySelections[0].act.personActivities[0],size);
								retrievePeople($scope.mySelections[0].act.personActivities[1],size);
												
							}
							$scope.activity = $scope.mySelections[0].act;
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
								
			$scope.getBen = function (){
      		  alert("Ciao scope");
      	  }
      	  
      	  	  
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
					        	  
					        	// flag beneficiaryNeeded      		
					        	  $scope.setBenAccordingActivityType = function(){
					        			$scope.beneficiaryNeeded=false;
					        			if($scope.activity.activityType=='TRAINING')$scope.beneficiaryNeeded=true;
					        				
					        	  };
					        	  
					        	  
					        	  $scope.isCPCN = false;								  
								  if (projectCode=='CPCN'){
									  $scope.isCPCN = true;
								  }
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
				  						     "personInCharge":$scope.personInCharge,"beneficiaries":$scope.beneficiaries, "date":null,
				  						     "isCPCN":$scope.isCPCN};				  				
					  				
					        	  }
					        	  else if (type == "modify"){
					        		
						            return {"activity": $scope.mySelections[0].act, "activityTypeList": $scope.activityType,"referralList": $scope.referralType, "interventionTypeList": $scope.interventionType,
					                    	"personInCharge":$scope.personInChargeSelected,"beneficiaries":$scope.beneficiarySelected,"date":$scope.mySelections[0].act.activityDate,
					                	    "isCPCN":$scope.isCPCN};
					        		
					        	  }	  
					        	  else{
					        		  $scope.mySelections[0].referral=null;
					        		  $scope.mySelections[0].activityType=null;
					        		  $("#activityId").attr("value",$scope.mySelections[0].activityId);
					        		  return {"activity": $scope.mySelections[0]};					        		  
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
					    $scope.$$childTail.items.activity.activityDate = ($scope.$$childTail.items.date);
						update($scope.$$childTail.items);
						$modalInstance.dismiss('cancel');				    
					  };

					  $scope.cancel = function () {
					    $modalInstance.dismiss('cancel');
					    $scope.personInCharge =selectedPersonInchargeObject;					
					  };
					  
					  
					 
					};

/*********************************************************************************************************************************************/
//This Function is called when I click ok on the form, and it handles INSERT,UPDATE, AND DELETE of a record of the table activity
				function update($newScope) {	
					        
							$scope.activityData = $newScope.activity;
					        $scope.personInCharge = $newScope.objectPCH;
							$scope.beneficiary = $newScope.objectBEN;
						
							
							// delete
							if(!$newScope.referralType && !$newScope.activityType && $newScope.activityId){
								$http({
									url : '../views/deleteActivity',
									method : 'POST',
									data : $scope.activityData
								}).success(function(data) {
									$scope.messages = data.messages;
									location.reload();
									alert("cancelled!");
								});
							}
							// insert or update
							else{
								
							    var globalActivity = {
								    activity : $scope.activityData,
							     	beneficiary : $scope.beneficiary,
							     	personInCharge : $scope.personInCharge,
							    };

							$http({
								url : '../views/insertActivity',
								method : 'POST',
								data : globalActivity
							}).success(function(data) {
								$scope.messages = data.messages;
								location.reload();
								alert("inserito!");
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
            	  $scope.activityId = row.entity.act.activityId; 
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
							location.reload();
						});
					  openModal($scope.notes.activityId);
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
 				  };

 				  $scope.cancel = function () {
 				    $modalInstance.dismiss('cancel');							
 				  };
 				  
 				  $scope.insertNote =  function() {
 					  openModalAddNote($scope.notes.activityId);
 					};
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
      			$scope.isCPCN=fal
      			  
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
      			 
      		}	
      		
      		
      		function getBen(){
      			alert("Ciao");
      		}
      		
      		$scope.getBen = function (){
      		  alert("Ciao scope fuori");
      	  };
				
      	
});					
