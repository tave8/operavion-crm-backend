package giuseppetavella.demo_login_system.integrations;

import giuseppetavella.demo_login_system.helpers.FileHelper;
import giuseppetavella.demo_login_system.integrations.anthropic.AnthropicAPIService;
import giuseppetavella.demo_login_system.infrastructure.CvDataModel;
import giuseppetavella.demo_login_system.exceptions.AIException;
import giuseppetavella.demo_login_system.exceptions.FileException;
import giuseppetavella.demo_login_system.exceptions.PayloadValidationException;
import giuseppetavella.demo_login_system.exceptions.UnknownFileTypeException;
import giuseppetavella.demo_login_system.helpers.PayloadValidationHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Business-specific AI-powered features.
 */
@Service
public class AppAnthropicAPIService extends AnthropicAPIService {
    
    private final ObjectMapper mapper = new ObjectMapper();


    /**
     * Find discrepancies between a legal contract 
     * and the actual data of the admin's account.
     */
    public String findDiscrepancies(String contractExpectations,
                                    String actualShifts) 
    {
    
        String prompt = "You are a compliance auditing assistant. Compare the service rules defined in "
                        +"a commercial CONTRACT of a cleaning company against the ACTUAL_SHIFTS executed for "
                        +"ONE specific client address of that cleaning company. The output is expectation minus reality.\n" +
                        "\n" +
                        "### CONTRACT SUMMARY (expectation)\n" +
                        contractExpectations +
                        "### ACTUAL_SHIFTS (reality)\n" +
                        actualShifts +
                        "### WHAT TO LOOK FOR (ANALYSIS RULES)\n" +
                        "Analyze the shifts against the contract and flag only these 4 specific operational breaches:\n" +
                        "1. Missing days. The contract specifies a day or frequency, but no shift was recorded on that day.\n" +
                        "2. Short duration. A shift was performed, but the total time worked is shorter than what the contract dictates.\n" +
                        "3. Wrong timing. A shift was performed, but the hours fall outside the specific time window requested (e.g., working during the day when the contract specifies evening hours).\n" +
                        "4. If everything is missing, meaning there are no shifts, say it.\n" +
                        "\n" +
                        "### OUTPUT FORMAT\n" +
                        "Return ONLY a plain text string in Italian listing the discrepancies found. \n" +
                        "- If there are multiple discrepancies, separate them with a simple bullet point or line break.\n" +
                        "- Be specific about days, expected times, and actual times.\n" +
                        "- If everything matches perfectly and there are no breaches, reply exactly with: \"Nessuna discrepanza riscontrata.\"\n" +
                        "- CRITICAL: The output will be read directly by the company's owner, so avoid any introductory phrases or pleasantries. Just output the raw text.";
        
        
        return this.ask(prompt);
    }
    
    

    /**
     * Extract expectations from legal contract.
     */
    public String extractContractExpectations(byte[] contractPdf)
    {
        
        String prompt = "You are an operational data extractor for an Italian cleaning company CRM. \n" +
                "Analyze the attached contract text for the client/cantiere. \n" +
                "Ignore all legal, financial, and safety clauses. \n" +
                "\n" +
                "Extract ONLY the operational schedule expectations. \n" +
                "CRITICAL RULE: You must format the output as a single-line list of items, where each item represents a specific schedule requirement or operational shift condition. Items MUST be separated strictly by the pipe character ( | ).\n" +
                "\n" +
                "CRITICAL RULE: DO NOT include any introductory text, pleasantries, bolding (**), markdown lists (- or *), explanations, or preambles. You must output ONLY the raw, pipe-separated final string itself. If you output anything else, the system parser will crash.\n" +
                "\n" +
                "Follow the exact formatting shown in the examples below, returning ONLY the raw text.\n" +
                "\n" +
                "### EXAMPLES OF EXPECTED RAW OUTPUT FORMATS\n" +
                "\n" +
                "Input: \"...garantendo la presenza del proprio personale nelle giornate di Lunedì e Giovedì nella fascia oraria mattutina. Ciascun intervento dovrà prevedere la durata di 2 ore per turno...\"\n" +
                "Output: Lunedì, 2 ore per turno | Giovedì, 2 ore per turno\n" +
                "\n" +
                "Input: \"...il servizio di sanificazione e pulizia degli spazi comuni verrà espletato con cadenza bisettimanale, ripartendo equamente un monte ore complessivo di 6 ore settimanali...\"\n" +
                "Output: Turno 1: Bisettimanale, 3 ore | Turno 2: Bisettimanale, 3 ore\n" +
                "\n" +
                "Input: \"...interventi di igienizzazione ordinaria da eseguirsi esclusivamente nella giornata di Sabato, per un totale di 4 ore di servizio continuativo...\"\n" +
                "Output: Sabato, 4 ore per turno\n" +
                "\n" +
                "Input: \"...passaggio programmato dal Lunedì al Venerdì per la svuotatura dei cestini e riordino (1 ora al giorno), con l'aggiunta di una pulizia approfondita il Mercoledì pomeriggio per 3 ore...\"\n" +
                "Output: Dal Lunedì al Venerdì, 1 ora al giorno | Mercoledì, 3 ore al pomeriggio\n" +
                "\n" +
                "Input: \"...l'appaltatore si impegna a garantire un intervento di pulizia a settimana della durata di 2 ore, da concordarsi preventivamente con la direzione dello stabilimento...\"\n" +
                "Output: 1 volta a settimana (giorno flessibile), 2 ore per turno\n" +
                "\n" +
                "### CONTRACT TEXT TO ANALYZE\n" +
                "[see contract in attachment]";
                
        String contractExpectationsFromAI = this.askWithPdf(contractPdf, prompt);
        
        
        return contractExpectationsFromAI;
    }

    public String extractContractExpectations(MultipartFile contractPdf)
    {
        byte[] bytes = FileHelper.getBytes(contractPdf);
        
        return this.extractContractExpectations(bytes);
    }
    

    /**
     * Parse a CV into JSON.
     */
    public CvDataModel extractCv(byte[] cvBytes) throws AIException
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
