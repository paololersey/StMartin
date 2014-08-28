package org.springframework.samples.mvc.pdf;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.samples.hibernate.PersonDao;
import org.springframework.samples.hibernate.beans.Activity;
import org.springframework.samples.hibernate.beans.Filter;
import org.springframework.samples.hibernate.beans.GlobalPdf;
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
 private static String FILE = "C:\\Users\\DanielaPaolo\\Documents\\Paolo\\Lavoro\\Africa\\Test.pdf";

 private static Font bigFont  = new Font(Font.FontFamily.HELVETICA, 20,  Font.BOLD);
 private static Font redFont  = new Font(Font.FontFamily.HELVETICA, 12,  Font.NORMAL, BaseColor.RED);
 private static Font subFont  = new Font(Font.FontFamily.HELVETICA, 16,  Font.BOLD);
 private static Font smallBold  = new Font(Font.FontFamily.HELVETICA, 12,  Font.BOLD);
 
 static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
 
 @Autowired
	private ApplicationContext appContext;
 
	@RequestMapping(value = "createPdf", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String createPdf(@RequestBody GlobalPdf globalPdf){
 
       try {
        Document document = new Document();
        try{
        	 PdfWriter.getInstance(document, new FileOutputStream(FILE));	
        }
        catch (Exception e) {
            return "filePdfAlreadyInUse";
           }
       
        document.open();
        
        addTitleAndFilters(document,globalPdf);
        addContent(document,globalPdf);
        document.close();
       } catch (Exception e) {
        e.printStackTrace();
       }
	return "okPdf";
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
	  c1 = new PdfPCell(new Phrase(activityList.get(i).getActivityType()));
	  tableActivity.addCell(c1);
	  
	  
      String date = DATE_FORMAT.format(activityList.get(i).getActivityDate());
	  tableActivity.addCell(date.toString());
	  tableActivity.addCell(activityList.get(i).getIntervention());
	  if(!isThisProgram("CPCN",globalPdf)) tableActivity.addCell(activityList.get(i).getReferral());
	  if(!isThisProgram("CPPR",globalPdf)) tableActivity.addCell(activityList.get(i).getLevelChange());
	  document.add(tableActivity);
	  if(globalPdf.getNoteList()!=null && globalPdf.getNoteList().size()>0){
		  PdfPTable tableNote = new PdfPTable(1);
		  String iNote = globalPdf.getNoteList().get(i);
		  if (iNote!=null){
			  tableNote.addCell("Report note: "+ iNote);		  
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
				titleParagraph.add(new Paragraph("Beneficiary : " + person.getFirstName() +" " +person.getLastName() +" " + person.getThirdName(), smallBold));	  
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