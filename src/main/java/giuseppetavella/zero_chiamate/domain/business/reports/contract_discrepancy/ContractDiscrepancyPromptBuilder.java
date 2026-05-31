package giuseppetavella.zero_chiamate.domain.business.reports.contract_discrepancy;

import org.springframework.stereotype.Component;

/**
 * AI prompt builder for contract detection system.
 * Goal: decouple prompt building from core logic.
 * 
 * Conventions: 
 * <pre>
 *     Methods are suffixed like this:
 *     
 *          ...UserPrompt
 *          ...SystemPrompt
 *     
 *     Examples:
 *     
 *          contractClassificationUserPrompt
 *          contractClassificationSystemPrompt
 *          
 * </pre>
 * 
 */
@Component
public class ContractDiscrepancyPromptBuilder {

    
    public String extractContractExpectationsUserPrompt(String contractText) {
        return "Here's the contract:\n" + contractText;
    }
    
    public String extractContractExpectationsSystemPrompt() {
        return "You are an operational data extractor for an Italian cleaning company CRM. " +
                "Analyze the contract text for the client/cantiere. " +
                "Ignore all legal, financial, and safety clauses. " +
                "\n\n" +
                "Extract ONLY the operational schedule expectations. " +
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
                "Output: 1 volta a settimana (giorno flessibile), 2 ore per turno";
    }


    public String findDiscrepanciesSystemPrompt() {
        return "You are a compliance auditing assistant. Compare the service rules defined in " +
                "a commercial CONTRACT of a cleaning company against the ACTUAL_SHIFTS executed for " +
                "ONE specific client address of that cleaning company. The output is expectation minus reality.\n" +
                "\n" +
                "### WHAT TO LOOK FOR (ANALYSIS RULES)\n" +
                "Analyze the shifts against the contract and flag only these 4 specific operational breaches:\n" +
                "1. Missing days. The contract specifies a day or frequency, but no shift was recorded on that day.\n" +
                "2. Short duration. A shift was performed, but the total time worked is shorter than what the contract dictates.\n" +
                "3. Wrong timing. A shift was performed, but the hours fall outside the specific time window requested (e.g., working during the day when the contract specifies evening hours).\n" +
                "4. If everything is missing, meaning there are no shifts, say it.\n" +
                "\n" +
                "### OUTPUT FORMAT\n" +
                "Return ONLY a plain text string in Italian listing the discrepancies found.\n" +
                "- If there are multiple discrepancies, separate them with a simple bullet point or line break.\n" +
                "- Be specific about days, expected times, and actual times.\n" +
                "- If everything matches perfectly and there are no breaches, reply exactly with: \"Nessuna discrepanza riscontrata.\"\n" +
                "- CRITICAL: The output will be read directly by the company's owner, so avoid any introductory phrases or pleasantries. Just output the raw text.";
        
    }

    public String findDiscrepanciesUserPrompt(String contractExpectations,
                                              String actualShifts) 
    {
        return "### CONTRACT SUMMARY (expectation)\n" +
            contractExpectations + "\n" +
            "### ACTUAL_SHIFTS (reality)\n" +
            actualShifts;

    }
    

    
    /**
     * Build the user prompt for contract classification.
     * 
     * @param startOfContract
     * @return
     */
    public String contractClassificationUserPrompt(String startOfContract) {
        return "Classify the following document opening:\n\n" + startOfContract;
    }

    
    /**
     * Build the system prompt for contract classification.
     * @return
     */
    public String contractClassificationSystemPrompt() {
        return  """
            You are a legal document classifier.
            You receive the opening lines of a document and determine whether it is a legal contract.
        
            Respond ONLY with a valid JSON object — no markdown, no explanation, no preamble.
        
            Schema:
            {
              "isContract": boolean,
              "whatIfNotContract": string | null
            }
            """;
    }

}
