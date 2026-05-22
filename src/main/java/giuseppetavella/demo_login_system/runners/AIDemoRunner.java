package giuseppetavella.demo_login_system.runners;

import giuseppetavella.demo_login_system.entities.clients.ClientAddress;
import giuseppetavella.demo_login_system.payloads.in_response.ShiftToSendDTO;
import giuseppetavella.demo_login_system.services.AppAIService;
import giuseppetavella.demo_login_system.services.AppPdfService;
import giuseppetavella.demo_login_system.services.ClientAddressesService;
import giuseppetavella.demo_login_system.services.ShiftsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;

@Component
public class AIDemoRunner implements CommandLineRunner {
    
    @Autowired
    private AppAIService appAIService;
    
    @Autowired
    private AppPdfService appPdfGenerationService;
    
    @Autowired
    private ShiftsService shiftsService;
    
    @Autowired
    private ClientAddressesService clientAddressesService;
    
    private final ObjectMapper mapper = new ObjectMapper();
    
    @Override
    public void run(String... args) throws Exception {
        
        // TODO: find shifts by client address between date range
        
        ClientAddress clientAddress = clientAddressesService.findById("03dd36c2-68f2-41ac-859d-47e8ba9923bc");

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();
        
        List<ShiftToSendDTO> shiftsDTO = shiftsService.findShiftsByClientAddressBetweenDatesDTO(
                clientAddress,
                startDate,
                endDate
        );

        // System.out.println(shiftsDTO);
        
        String shiftsInfo = shiftsService.stringifyShifts(shiftsDTO);

        System.out.println(shiftsInfo);
        
        // List<ShiftToSendDTO> shiftsDTO = shiftsService.findShiftsBy
        
        // String contractExpectations = "Lunedì, Mercoledì, Venerdì, fascia oraria 18:30-21:30 (uffici svuotati) | Dal Lunedì al Venerdì, cadenza giornaliera, sanificazione servizi igienici e aree comuni | Aprile e Ottobre, cadenza semestrale, lavaggio moquette e sedute in tessuto (preavviso 10 giorni) | Frequenza quadrimestrale, lavaggio vetrate interne ed esterne ad alta quota\n";
        //
        // String actualShifts = "Shift 1:\n" +
        //         "\n" +
        //         "Days: Wednesday, Thursday, Friday, Saturday\n" +
        //         "\n" +
        //         "Time: 06:00:00 - 06:00:00\n" +
        //         "\n" +
        //         "Start Date: 2026-05-19\n" +
        //         "\n" +
        //         "Shift 2:\n" +
        //         "\n" +
        //         "Days: Wednesday\n" +
        //         "\n" +
        //         "Time: 06:00:00 - 06:00:00\n" +
        //         "\n" +
        //         "Start Date: 2026-05-12\n" +
        //         "\n" +
        //         "Client: Turismo con noi\n" +
        //         "Address: Palazzo di cristallo\n" +
        //         "\n" +
        //         "Shift 3:\n" +
        //         "\n" +
        //         "Days: Tuesday, Thursday\n" +
        //         "\n" +
        //         "Time: 06:00:00 - 06:00:00\n" +
        //         "\n" +
        //         "Start Date: 2026-05-22\n";
        //
        // String AIout = this.appAIService.findDiscrepancies(contractExpectations, actualShifts);
        //
        // System.out.println(AIout);

        // byte[] myCvBytes = FileHelper.readPdf("extra/my_cv.pdf");
        
        
        // String answer = aiService.ask("Is this grammar correct? Today i liked it but the pizza was more better than before");

        // byte[] pdfBytes = appPdfGenerationService.generateInvoice(Map.of());

        // String jsonStr = aiService.askWithPdf(myCvBytes,  """
        //              Extract the following fields from this CV and return ONLY a JSON object,
        //                 no markdown, no backticks, no preamble. If a field is not found, set it to null.
        //                 For arrays, return an empty array if nothing is found.
        //         {
        //             "full_name": null,
        //             "date_of_birth": null,
        //             "email": null,
        //             "phone": null,
        //             "address": null,
        //             "nationality": null,
        //             "education": [
        //                 {
        //                     "degree": null,
        //                     "institution": null,
        //                     "year": null
        //                 }
        //             ],
        //             "experience": [
        //                 {
        //                     "company": null,
        //                     "role": null,
        //                     "from": null,
        //                     "to": null,
        //                     "description": null
        //                 }
        //             ],
        //             "skills": [],
        //             "languages": [
        //                 {
        //                     "language": null,
        //                     "level": null
        //                 }
        //             ],
        //             "certifications": []
        //         }
        //             Return ONLY the JSON object, no markdown, no backticks, no preamble.
        //         """);

        // 2. JSON string → Java object (to parse the response)
        //
        // CvData cvData = mapper.readValue(jsonStr, CvData.class);   // "{...}" → WorkerData
        //
        // System.out.println(cvData.getEmail());
        // System.out.println(cvData.getLanguages());
        // System.out.println(cvData.getFullName());
        // System.out.println(cvData.getDateOfBirth());
        // System.out.println(cvData.getPhone());
        // System.out.println(cvData.getFullName());
        
        // System.out.println(worker.address());
        // System.out.println(worker.full_name());
        
        // System.out.println(jsonStr);
        
        
    }
}
