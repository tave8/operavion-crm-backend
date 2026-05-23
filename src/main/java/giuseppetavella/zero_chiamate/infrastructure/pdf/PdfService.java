package giuseppetavella.zero_chiamate.infrastructure.pdf;

import giuseppetavella.zero_chiamate.infrastructure.BrowserContentDispositionHeader;
import giuseppetavella.zero_chiamate.exceptions.PdfGenerationException;
import giuseppetavella.zero_chiamate.infrastructure.template.HtmlTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.util.Map;

@Service
public class PdfService {

    @Autowired
    private HtmlTemplateService htmlTemplateService;
    
    
    /**
     * template + vars -> HTTP response entity 
     */
    protected ResponseEntity<byte[]> templateToHttpResponse(String template,
                                                        Map<String, Object> vars,
                                                        String outputFilename,
                                                        BrowserContentDispositionHeader contentDispositionHeader) 
    {
        // template -> html
        String html = this.htmlTemplateService.fillTemplate(template, vars);
        // html -> pdf
        byte[] bytes = this.htmlToPdf(html);
        // pdf -> http response
        return this.pdfToHttpResponse(bytes, outputFilename, contentDispositionHeader);
    }

    
    /**
     * template + vars -> PDF
     */
    protected byte[] templateToPdf(String template, Map<String, ? extends Object> vars) throws PdfGenerationException
    {
        // template -> html 
        String html = this.htmlTemplateService.fillTemplate(template, vars);
        // html -> pdf 
        return this.htmlToPdf(html);
    }
    
    
    /**
     * HTML -> PDF
     */
    protected byte[] htmlToPdf(String html) throws PdfGenerationException 
    {
        // Generate PDF into memory
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        
        try {
            
            renderer.setDocumentFromString(html, null);
            renderer.layout();
            renderer.createPDF(baos);
            return baos.toByteArray();
            
        } catch (Exception ex) {
            
            throw new PdfGenerationException("Failed to generate PDF from HTML. DETAILS: " + ex.getMessage());
       
        }
        
    }

    

    /**
     * PDF -> HTTP response entity
     */
    protected ResponseEntity<byte[]> pdfToHttpResponse(byte[] pdf, 
                                                         String outputFilename,
                                                         BrowserContentDispositionHeader contentDispositionHeader) 
    {
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData(contentDispositionHeader.getValue(), outputFilename);

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
    


    /**
     *
     * PDF -> upload
     *
     * @return URL of uploaded file
     */
    // protected String pdfToUpload(String template, Map<String, Object> vars) throws PdfGenerationException, 
    //                                                                             InvalidFileUploadedException, 
    //                                                                             FileUploadException
    // {
    //     ByteArrayOutputStream pdf = this.templateToPdf(template, vars);
    //     byte[] byteArray = pdf.toByteArray();
    //     return this.mediaUploadService.uploadFile(byteArray);
    // }

    

    /**
     * PDF -> save local
     */
    // protected void pdfToSaveLocal(String template, 
    //                            Map<String, Object> vars, 
    //                            String outputDir, 
    //                            String outputFilename) throws PdfGenerationException 
    // {
    //
    //     try {
    //        
    //         String html = this.templateToHtml(template, vars);
    //
    //         // Convert HTML to PDF
    //         ITextRenderer renderer = new ITextRenderer();
    //         renderer.setDocumentFromString(html, null);
    //         renderer.layout();
    //
    //         // Save to file
    //         String path = System.getProperty("user.dir") + outputDir + "/" + outputFilename;
    //         new File(System.getProperty("user.dir") + outputDir).mkdirs();
    //
    //         try (OutputStream os = new FileOutputStream(path)) {
    //             renderer.createPDF(os);
    //         }
    //        
    //     } catch(IOException ex) {
    //        
    //         throw new PdfGenerationException(ex.getMessage());
    //        
    //     }
    //
    // }
    
    
    
    
}
