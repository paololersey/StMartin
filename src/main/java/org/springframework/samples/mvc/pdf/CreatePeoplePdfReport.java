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
import org.springframework.samples.hibernate.beans.Filter;
import org.springframework.samples.hibernate.beans.GlobalPdf;
import org.springframework.samples.hibernate.beans.Person;
import org.springframework.samples.hibernate.beans.ProjectPerson;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
 
@Controller
@RequestMapping("/pdf/*")
@Transactional
public class CreatePeoplePdfReport{
 private static String FILE = "C:\\Temp\\PeopleReport.pdf";

 private static Font bigFont  = new Font(Font.FontFamily.HELVETICA, 18,  Font.BOLD);
 private static Font redFont  = new Font(Font.FontFamily.HELVETICA, 10,  Font.NORMAL, BaseColor.RED);
 private static Font subFont  = new Font(Font.FontFamily.HELVETICA, 10,  Font.BOLD);
 private static Font smallBold  = new Font(Font.FontFamily.HELVETICA, 10,  Font.BOLD);
 private static Font smallNormal  = new Font(Font.FontFamily.HELVETICA, 8,  Font.NORMAL);

 static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
 
 @Autowired
	private ApplicationContext appContext;
 
	@RequestMapping(value = "createPeoplePdf", method = RequestMethod.POST, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public 
	ResponseEntity<byte[]> createPeoplePdf(@RequestBody GlobalPdf globalPdf){
 
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
  Element titleElement = new Paragraph("People Report, "+  DATE_FORMAT.format(new Date()).toString(), bigFont);
  titleParagraph.setAlignment(Element.ALIGN_CENTER);
  titleParagraph.add(titleElement);
 
  addBlankLine(titleParagraph, 1);
   
  // Questa linea scrive "Documento generato da: nome utente, data"
  titleParagraph.add(new Paragraph("Generated report by " + System.getProperty("user.name"),subFont));
   
  addBlankLine(titleParagraph, 3);

  setFilterTitles(titleParagraph, globalPdf.getFilter());
 
  // Aggiunta al documento
  document.add(titleParagraph);
   
  // Nuova pagina
  //document.newPage();
 }


 
 private static void addContent(Document document, GlobalPdf globalPdf) throws DocumentException {
  PdfPTable tableHead = new PdfPTable(5);
 
  if(!isThisProgram("CPPR",globalPdf.getProjectPerson())){
	  tableHead =  new PdfPTable(6);
	  setTableWidth(tableHead,6);
  }else{
	  setTableWidth(tableHead,5);
  }
  
  java.util.List<Person> peopleList=globalPdf.getPeopleList();
  
  PdfPCell c1 = new PdfPCell(new Phrase("Names",subFont));
  c1.setHorizontalAlignment(Element.ALIGN_CENTER);
  c1.setGrayFill(0.8f);
  tableHead.addCell(c1);
  
  c1 = new PdfPCell(new Phrase("G",subFont));
  c1.setHorizontalAlignment(Element.ALIGN_CENTER);
  c1.setGrayFill(0.8f);
  tableHead.addCell(c1);
  

  c1 = new PdfPCell(new Phrase("Adm.Date",subFont));
  c1.setHorizontalAlignment(Element.ALIGN_CENTER);
  c1.setGrayFill(0.8f);
  tableHead.addCell(c1);
  
  c1 = new PdfPCell(new Phrase("File number",subFont));
  c1.setHorizontalAlignment(Element.ALIGN_CENTER);
  c1.setGrayFill(0.8f);
  tableHead.addCell(c1);
  
  c1 = new PdfPCell(new Phrase("Village",subFont));
  c1.setHorizontalAlignment(Element.ALIGN_CENTER);
  c1.setGrayFill(0.8f);
  tableHead.addCell(c1);
  
  if(!isThisProgram("CPPR",globalPdf.getProjectPerson())){
	  c1 = new PdfPCell(new Phrase("Level",subFont));
	  c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	  c1.setGrayFill(0.8f);
	  tableHead.addCell(c1);
  }
  document.add(tableHead);
 
  
  for(int i=0; i<peopleList.size();i++){
	  PdfPTable tablePeople = new PdfPTable(5);
	  
	  setTableWidth(tablePeople,5);
	  if(!isThisProgram("CPPR",globalPdf.getProjectPerson())){
		  tablePeople =  new PdfPTable(6);
		  setTableWidth(tablePeople,6);
		  
	  }
	  String baptismName =peopleList.get(i).getFirstName();
	  String middleName=peopleList.get(i).getLastName();
	  String familyName=peopleList.get(i).getThirdName();
	  c1 = tablePeople.addCell(new PdfPCell (new Phrase(baptismName+" "+middleName+" "+(familyName!=null?familyName:""),smallBold)));	
	  
	  tablePeople.addCell(new PdfPCell(new Phrase(peopleList.get(i).getGender(),smallNormal)));
	  if(peopleList.get(i).getInsertDate()!=null){
		  tablePeople.addCell(new PdfPCell(new Phrase(DATE_FORMAT.format(peopleList.get(i).getInsertDate()),smallNormal)));
      }
	  else{
		  tablePeople.addCell("");
	  }
	  tablePeople.addCell(new PdfPCell(new Phrase(peopleList.get(i).getFileNumber(),smallNormal)));
	  tablePeople.addCell(new PdfPCell(new Phrase(peopleList.get(i).getVillage(),smallNormal)));
	  if(!isThisProgram("CPPR",globalPdf.getProjectPerson()))tablePeople.addCell(new PdfPCell(new Phrase(peopleList.get(i).getState(),smallNormal)));
	
	  document.add(tablePeople);
  }
 
 }

private static void setTableWidth(PdfPTable table,int length) throws DocumentException{
	if(length==5)table.setWidths(new int[]{300,30,100,120,100});	
	if(length==6){
		table.setTotalWidth(1200);
		table.setWidths(new int[]{300,30,180,200,230,170});	
	}
}

private static boolean isThisProgram(String thisProgram, ProjectPerson projectPerson) {
	if( projectPerson!=null && projectPerson.getProjectCode()!=null && thisProgram.equals(projectPerson.getProjectCode())){
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
		
	  if(filter!=null && filter.getZone()!=null){
		  titleParagraph.add(new Paragraph("Zone : " + filter.getZone() , smallBold));
		  addBlankLine(titleParagraph, 1);
	  }
	  if(filter!=null && filter.getReferral()!=null){
		  titleParagraph.add(new Paragraph("Staus/level : " + filter.getStatus() , smallBold));
		  addBlankLine(titleParagraph, 1);
	  }
	  if(filter!=null && filter.getMajorTraining()!=null){
		 
			  titleParagraph.add(new Paragraph("Major training : " + filter.getMajorTraining() , smallBold));
		  
		  
		  addBlankLine(titleParagraph, 1);
	  }
	}

}