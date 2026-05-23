package giuseppetavella.demo_login_system.domain.business.cv_extraction;

import giuseppetavella.demo_login_system.exceptions.AIException;
import giuseppetavella.demo_login_system.exceptions.FileException;
import giuseppetavella.demo_login_system.exceptions.PayloadValidationException;
import giuseppetavella.demo_login_system.exceptions.UnknownFileTypeException;
import giuseppetavella.demo_login_system.helpers.PayloadValidationHelper;
import giuseppetavella.demo_login_system.infrastructure.CvDataModel;
import giuseppetavella.demo_login_system.infrastructure.ai.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Service
public class CvExtractionService {


    private final ObjectMapper mapper = new ObjectMapper();
    
    
    @Autowired
    private AIService aiService;

    


    /**
     * Parse a CV into JSON.
     */
    public CvDataModel extractCv(byte[] cvBytes) throws AIException
    {

        String jsonStr = aiService.askWithPdf("""
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
                """, cvBytes);

        // 2. JSON string → Java object (to parse the response)

        CvDataModel cvDataModel = mapper.readValue(jsonStr, CvDataModel.class);

        return cvDataModel;

    }

    /**
     *
     *
     */
    public CvDataModel extractCv(MultipartFile file) throws FileException,
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
