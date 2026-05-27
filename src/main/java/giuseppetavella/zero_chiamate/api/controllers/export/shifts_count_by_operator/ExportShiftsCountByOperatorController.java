package giuseppetavella.zero_chiamate.api.controllers.export.shifts_count_by_operator;

import giuseppetavella.zero_chiamate.api.controllers.export.shifts_count_by_operator.dto.sent.ShiftsCountByOperatorReportParamsSentDTO;
import giuseppetavella.zero_chiamate.domain.business.reports.shifts_count_by_operator.ShiftsCountByOperatorReportGenerator;
import giuseppetavella.zero_chiamate.domain.business.reports.shifts_count_by_operator.params.ShiftsCountByOperatorReportParams;
import giuseppetavella.zero_chiamate.domain.entities.shifts.ShiftsService;
import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.helpers.PayloadValidationHelper;
import giuseppetavella.zero_chiamate.helpers.ValidationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

/**
 * Controller for all things export.
 * PDF export, CSV export etc.
 */
@RestController
@RequestMapping("/export")
public class ExportShiftsCountByOperatorController {
    
    @Autowired
    private ShiftsCountByOperatorReportGenerator reportGenerator;
    
    @Autowired
    private ShiftsService shiftsService;
    

    /**
     * Export shifts count by operator as csv
     */
    @PostMapping("/shifts-count-by-operator/csv")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<byte[]> exportShiftCountByOperator(@AuthenticationPrincipal User currentUser,
                                                             @RequestBody @Validated ShiftsCountByOperatorReportParamsSentDTO body,
                                                             BindingResult validation)
    {

        PayloadValidationHelper.requireNoErrors(validation);
        
        var company = currentUser.getCompany();

        ValidationHelper.requireValidRange(body.startDate(), body.endDate());

        // find shifts by operator
        Map<User, Integer> shiftsCountByOperator  = shiftsService.countShiftsByOperator(
                company, 
                body.startDate(), 
                body.endDate()
        );

        var reportParams = new ShiftsCountByOperatorReportParams(
                shiftsCountByOperator,
                body.startDate(),
                body.endDate()
        );
            
        var csv = reportGenerator.generate(reportParams);


        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"shifts_report.csv\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv.toBytes());
        
        
    }
    
    
}
