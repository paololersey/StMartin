package org.springframework.samples.mvc.pdf;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.hibernate.PersonDao;
import org.springframework.samples.hibernate.beans.Activity;
import org.springframework.samples.hibernate.beans.Filter;
import org.springframework.samples.hibernate.beans.GlobalPdf;
import org.springframework.samples.hibernate.beans.NatureOfCasePerson;
import org.springframework.samples.hibernate.beans.Person;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
 
@Controller
@RequestMapping("/pdf/*")
@Transactional
public class CreatePdf {
 private static String FILE = "C:\\Temp\\ProgressReport.pdf";

 private static Font bigFont  = new Font(Font.FontFamily.HELVETICA, 18,  Font.BOLD);
 private static Font redFont  = new Font(Font.FontFamily.HELVETICA, 10,  Font.NORMAL, BaseColor.RED);
 private static Font subFont  = new Font(Font.FontFamily.HELVETICA, 14,  Font.BOLD);
 private static Font smallBold  = new Font(Font.FontFamily.HELVETICA, 10,  Font.BOLD);
 private static Font smallNormal  = new Font(Font.FontFamily.HELVETICA, 8,  Font.NORMAL);
 
 static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
 
 @Autowired
	private ApplicationContext appContext;
 
	@RequestMapping(value = "createPdf", method = RequestMethod.POST, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public 
	ResponseEntity<byte[]> createPdf(@RequestBody GlobalPdf globalPdf){
 
       try {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        
       
        document.open();       
        addTitleAndFilters(document,globalPdf);
        addContent(document,globalPdf);
        document.close();
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Expires", "0");
        headers.add("Cache-Control","must-revalidate, post-check=0, pre-check=0");
        headers.add("Pragma", "public");
        headers.add("content-disposition", "attachment; filename=" + "report.pdf");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(baos.size());
          
        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(baos.toByteArray(), headers, HttpStatus.OK);
        return response;
       } catch (Exception e) {
        e.printStackTrace();
       }
	return null;
 }
  
 
 private void addTitleAndFilters(Document document, GlobalPdf globalPdf) throws DocumentException {
  Paragraph titleParagraph = new Paragraph();
   
  // Aggiungiamo una linea vuota
  addBlankLine(titleParagraph, 1);
   
  // Aggiungiamo il titolo
  Element titleElement = new Paragraph("Progress Report, "+  DATE_FORMAT.format(new Date()).toString(), bigFont);
  titleParagraph.setAlignment(Element.ALIGN_CENTER);
  titleParagraph.add(titleElement);
 
  addBlankLine(titleParagraph, 1);
   
  // Questa linea scrive "Documento generato da: nome utente, data"
  titleParagraph.add(new Paragraph("Generated report by " + System.getProperty("user.name")));
   
  addBlankLine(titleParagraph, 3);

  setFilterTitles(titleParagraph, globalPdf.getFilter());
 
  // Aggiunta al documento
  document.add(titleParagraph);
   
  // Nuova pagina
  //document.newPage();
 }


 
 private static void addContent(Document document, GlobalPdf globalPdf) throws DocumentException {
  PdfPTable tableHead = new PdfPTable(4);
  setTableWidth(tableHead,4);
  if(isThisProgram("CPPD",globalPdf)){
	  tableHead =  new PdfPTable(5);
	  setTableWidth(tableHead,5);
  }
  
  // tableActivity.setBorderColor(BaseColor.GRAY);
  // tableActivity.setPadding(4);
  // tableActivity.setSpacing(4);
  // tableActivity.setBorderWidth(1);
  java.util.List<Activity> activityList=globalPdf.getActivityList();
  
  PdfPCell c1 = new PdfPCell(new Phrase("Act.Type"));
  c1.setHorizontalAlignment(Element.ALIGN_CENTER);
  c1.setGrayFill(0.8f);
  tableHead.addCell(c1);

  c1 = new PdfPCell(new Phrase("Date"));
  c1.setHorizontalAlignment(Element.ALIGN_CENTER);
  c1.setGrayFill(0.8f);
  tableHead.addCell(c1);
  
  c1 = new PdfPCell(new Phrase("Intervention"));
  c1.setHorizontalAlignment(Element.ALIGN_CENTER);
  c1.setGrayFill(0.8f);
  tableHead.addCell(c1);
  
  if(!isThisProgram("CPCN",globalPdf)){
  c1 = new PdfPCell(new Phrase("Referral"));
  c1.setHorizontalAlignment(Element.ALIGN_CENTER);
  c1.setGrayFill(0.8f);
  tableHead.addCell(c1);
 // tableHead.setHeaderRows(1);
  }
  
  if(!isThisProgram("CPPR",globalPdf)){
	  c1 = new PdfPCell(new Phrase("Level"));
	  c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	  c1.setGrayFill(0.8f);
	  tableHead.addCell(c1);
  }
  document.add(tableHead);
 
  
  for(int i=0; i<activityList.size();i++){
	  PdfPTable tableActivity = new PdfPTable(4);
	  setTableWidth(tableActivity,4);
	  if(isThisProgram("CPPD",globalPdf)){
		  tableActivity =  new PdfPTable(5);
		  setTableWidth(tableActivity,5);
		  
	  }
	  c1 = new PdfPCell(new Phrase(activityList.get(i).getActivityType(),smallBold));
	  tableActivity.addCell(c1);
	  
	  
      tableActivity.addCell(new PdfPCell(new Phrase(DATE_FORMAT.format(activityList.get(i).getActivityDate()),smallNormal)));
	  tableActivity.addCell(new PdfPCell(new Phrase(activityList.get(i).getIntervention(),smallNormal)));
	  if(!isThisProgram("CPCN",globalPdf)) tableActivity.addCell(new PdfPCell(new Phrase(activityList.get(i).getReferral(),smallNormal)));
	  if(!isThisProgram("CPPR",globalPdf)) tableActivity.addCell(new PdfPCell(new Phrase(activityList.get(i).getLevelChange(),smallNormal)));
	  document.add(tableActivity);
	  if(globalPdf.getNoteList()!=null && globalPdf.getNoteList().size()>0){
		  PdfPTable tableNote = new PdfPTable(1);
		  String iNote = globalPdf.getNoteList().get(i);
		  if (iNote!=null){
			  tableNote.addCell(new PdfPCell(new Phrase("Report note: "+ iNote,smallNormal)));		  
		  }
		  document.add(tableNote);
	  }
  }
 
 }

private static void setTableWidth(PdfPTable table,int length) throws DocumentException{
	if(length==4)table.setWidths(new int[]{300,100,120,100});	
	if(length==5){
		table.setTotalWidth(1200);
		table.setWidths(new int[]{300,180,200,200,170});	
	}
}

private static boolean isThisProgram(String thisProgram, GlobalPdf globalPdf) {
	if(globalPdf.getFilter()!=null && globalPdf.getFilter().getProjectCode()!=null && thisProgram.equals(globalPdf.getFilter().getProjectCode())){
		return true;
	}
	return false;
}


private static void addBlankLine(Paragraph paragraph, int number) {
  for (int i = 0; i < number; i++) {
   paragraph.add(new Paragraph(" "));
  }
 }
 
 
 public void setFilterTitles(Paragraph titleParagraph, Filter filter) {
	 if(filter!=null && filter.getPersonIdBeneficiary()!=null){
		  PersonDao personDao = (PersonDao) appContext.getBean("personDao");
		  java.util.List<Person> personList = personDao.findPersonByPersonId(filter.getPersonIdBeneficiary(), "BE");
			if(personList!=null && personList.size()>0){
				Person person = personList.get(0);
				java.util.List<String> natureOfcases = personDao.getNatureOfCaseByPersonId(person);
				titleParagraph.add(new Paragraph("Beneficiary : " + person.getFirstName() +" " +person.getLastName() +" " + person.getThirdName(), smallBold));	  
				for(String natureOfcase:natureOfcases){
					titleParagraph.add(new Paragraph("Nature of case : " + natureOfcase, smallBold));	  
				}
				addBlankLine(titleParagraph, 1);
		  }		
		 
	  }
	  if(filter!=null && filter.getPersonIdPersonInCharge()!=null){
		  PersonDao personDao = (PersonDao) appContext.getBean("personDao");
		  ArrayList<String> personTypeArray = new ArrayList<String>();
		  personTypeArray.add("VO");
		  personTypeArray.add("SW");
		  personTypeArray.add("PH");
		  personTypeArray.add("CM");
		  for(String personType: personTypeArray){
		  java.util.List<Person> personList = personDao.findPersonByPersonId(filter.getPersonIdPersonInCharge(), personType);
				if(personList!=null && personList.size()>0){
				 	Person person = personList.get(0);
				 	String thirdname =  person.getThirdName();
				 	if(thirdname==null) thirdname="";
					titleParagraph.add(new Paragraph("Person In Charge : " + person.getFirstName() +" " +person.getLastName() +" " + thirdname, smallBold));	  
					addBlankLine(titleParagraph, 1);
			}		
		  }
		 
	  }
	  if(filter!=null && filter.getDateStart()!=null){
		  titleParagraph.add(new Paragraph("Start date : " + DATE_FORMAT.format(filter.getDateStart()) , smallBold));
		  titleParagraph.setAlignment(Element.ALIGN_CENTER);
		  addBlankLine(titleParagraph, 1);
	  }
	  if(filter!=null && filter.getDateEnd()!=null){
		  titleParagraph.add(new Paragraph("End date : " + DATE_FORMAT.format(filter.getDateStart()) , smallBold));
		  addBlankLine(titleParagraph, 1);
	  }
		
	  if(filter!=null && filter.getActivityType()!=null){
		  titleParagraph.add(new Paragraph("Type of activity : " + filter.getActivityType() , smallBold));
		  addBlankLine(titleParagraph, 1);
	  }
	  if(filter!=null && filter.getReferral()!=null){
		  titleParagraph.add(new Paragraph("Referral Type : " + filter.getReferral() , smallBold));
		  addBlankLine(titleParagraph, 1);
	  }
	  if(filter!=null && filter.getIntervention()!=null){
		  if (filter.getProjectCode()!=null && "CPPD".equals(filter.getProjectCode())){
			  titleParagraph.add(new Paragraph("Applicance/Kits : " + filter.getIntervention() , smallBold));
		  }
		  else{
			  titleParagraph.add(new Paragraph("Referral Type : " + filter.getIntervention() , smallBold));
		  }
		  
		  addBlankLine(titleParagraph, 1);
	  }
	}

}