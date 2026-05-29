package giuseppetavella.zero_chiamate.infrastructure.pdf;

import giuseppetavella.zero_chiamate.config.EmailTemplate;
import giuseppetavella.zero_chiamate.config.Template;
import giuseppetavella.zero_chiamate.infrastructure.pdf.exceptions.PdfGenerationException;
import giuseppetavella.zero_chiamate.infrastructure.template.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * Base class for working with PDFs.
 * It deals with bytes and lower level details
 */
@Service
public class BasePdfService {
    
    @Autowired
    private TemplateService templateService;


    /**
     * HTML -> PDF
     */
    public byte[] htmlToPdf(String html) throws PdfGenerationException
    {
        // Generate PDF into memory
        var baos = new ByteArrayOutputStream();
        var renderer = new ITextRenderer();

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
     * template + vars -> PDF
     */
    public byte[] templateToPdf(Template template,
                                Map<String, ? extends Object> vars) throws PdfGenerationException
    {
        // template -> html 
        var html = this.templateService.fillTemplate(template, vars);
        // html -> pdf 
        return this.htmlToPdf(html);
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
