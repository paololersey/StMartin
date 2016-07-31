package org.springframework.samples.mvc.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;



public class OutSimplePdf extends HttpServlet {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    makePdf(request, response, "GET");
  }

  protected void doPost(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    makePdf(request, response, "POST");
  }
  public void makePdf(HttpServletRequest request,
      HttpServletResponse response, String methodGetPost)
      throws ServletException, IOException {
    try {

      String msg = "your message";

      Document document = new Document();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PdfWriter.getInstance(document, baos);
      document.open();
      document.add(new Paragraph(msg));
      document.add(Chunk.NEWLINE);
      document.add(new Paragraph("a paragraph"));
      document.close();

      response.setHeader("Expires", "0");
      response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
      response.setHeader("Pragma", "public");

      response.setContentType("application/pdf");

      response.setContentLength(baos.size());

      ServletOutputStream out = response.getOutputStream();
      baos.writeTo(out);
      out.flush();

    } catch (Exception e2) {
      System.out.println("Error in " + getClass().getName() + "\n" + e2);
    }
  }
  public void destroy() {
  }
}