package giuseppetavella.demo_login_system.services;

import giuseppetavella.demo_login_system.models.CvData;
import giuseppetavella.demo_login_system.exceptions.AIException;
import giuseppetavella.demo_login_system.exceptions.FileException;
import giuseppetavella.demo_login_system.exceptions.PayloadValidationException;
import giuseppetavella.demo_login_system.exceptions.UnknownFileTypeException;
import giuseppetavella.demo_login_system.helpers.PayloadValidationHelper;
import giuseppetavella.demo_login_system.services.base.AIService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Business-specific AI-powered features.
 */
@Service
public class AppAIService extends AIService {
    
    private final ObjectMapper mapper = new ObjectMapper();


    /**
     * Parse a CV into JSON.
     */
    public CvData extractCv(byte[] cvBytes) throws AIException
    {
        
        String jsonStr = this.askWithPdf(cvBytes,  """
                     Extract the following fields from this CV and return ONLY a JSON object,
                        no markdown, no backticks, no preamble. If a field is not found, set it to null.
                        For arrays, return an empty array if nothing is found.
                {
                    "fullName": null,
                    "dateOfBirth": null,
                    "email": null,
                    "phone": null,
                    "address": null,
                    "nationality": null,
                    "education": [
                        {
                            "degree": null,
                            "institution": null,
                            "year": null
                        }
                    ],
                    "experience": [
                        {
                            "company": null,
                            "role": null,
                            "from": null,
                            "to": null,
                            "description": null
                        }
                    ],
                    "skills": [],
                    "languages": [
                        {
                            "language": null,
                            "level": null
                        }
                    ],
                    "certifications": []
                }
                    Return ONLY the JSON object, no markdown, no backticks, no preamble.
                """);

        // 2. JSON string → Java object (to parse the response)

        CvData cvData = mapper.readValue(jsonStr, CvData.class);  
        
        return cvData;
        
    }

    /**
     * 
     * 
     */
    public CvData extractCv(MultipartFile file) throws FileException, 
                                                       UnknownFileTypeException,
                                                       PayloadValidationException,
                                                        AIException
    {
        PayloadValidationHelper.requiredPdf(file);
        
        try {
            
            return this.extractCv(file.getBytes());
            
        } catch (IOException e) {
            throw new FileException(e.getMessage());
        }
        
    }
    
    
}
