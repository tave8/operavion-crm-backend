package giuseppetavella.demo_login_system.services;

import giuseppetavella.demo_login_system.entities.Company;
import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.entities.checklists.Checklist;
import giuseppetavella.demo_login_system.entities.clients.ClientAddress;
import giuseppetavella.demo_login_system.entities.shifts.Shift;
import giuseppetavella.demo_login_system.entities.shifts.ShiftDay;
import giuseppetavella.demo_login_system.helpers.AuthorizationHelper;
import giuseppetavella.demo_login_system.payloads.in_request.NewShiftSentDTO;
import giuseppetavella.demo_login_system.payloads.in_response.*;
import giuseppetavella.demo_login_system.repositories.ShiftDaysRepository;
import giuseppetavella.demo_login_system.repositories.ShiftsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ShiftsService {

    @Autowired
    private ShiftsRepository shiftsRepository;
    
    @Autowired
    private ShiftDaysRepository shiftDaysRepository;
    
    @Autowired
    private ClientAddressesService clientAddressesService;
    
    @Autowired
    private ChecklistsService checklistsService;
    
    @Autowired
    private ChecklistEntriesService checklistEntriesService;

    /**
     * Add a shift.
     * 
     * @param company
     * @param body
     * @return
     */
    @Transactional
    public ShiftToSendDTO addShift(Company company, NewShiftSentDTO body) 
    {
        
        ClientAddress clientAddressFromDB = this.clientAddressesService.findById(body.clientAddressId());
        
        Checklist checklistFromDB = this.checklistsService.findById(body.checklistId());
        
        // check that client address and checklist belong to the same company 
        // that is trying to add the shift
        AuthorizationHelper.requireSameCompany(company, clientAddressFromDB.getClient().getCompany());
        
        AuthorizationHelper.requireSameCompany(company, checklistFromDB.getCompany());
        
       
        Shift newShift = new Shift(
                clientAddressFromDB,
                checklistFromDB,
                body.startDate(),
                body.endDate(),
                body.startTime(),
                body.endTime()
        );
        
        // save the shift in DB
        Shift shiftFromDB = this.shiftsRepository.save(newShift);
        
        // save the days of the shift in DB
        
        // but first, map the days to a ShiftDay non-managed instance
        List<ShiftDay> shiftDays = body.days()
                                    .stream()
                                    .map(day -> new ShiftDay(shiftFromDB, day))
                                    .toList();
        
        // save shift days
        List<ShiftDay> shiftDaysFromDB = this.shiftDaysRepository.saveAll(shiftDays);
        
        List<ShiftDayToSendDTO> shiftDaysToSendDTO = shiftDaysFromDB.stream()
                                                    .map(shiftDay -> new ShiftDayToSendDTO(shiftDay))
                                                    .toList();
        
        List<ChecklistEntryToSendDTO> entriesDTO = this.checklistEntriesService.getEntriesByChecklistAsDTO(checklistFromDB);

        ClientAddressToSendDTO clientAddressToSendDTO = new ClientAddressToSendDTO(clientAddressFromDB);
        
        ChecklistToSendDTO checklistToSendDTO = new ChecklistToSendDTO(checklistFromDB, entriesDTO);
        
        return new ShiftToSendDTO(
                shiftFromDB,
                shiftDaysToSendDTO,
                clientAddressToSendDTO,
                checklistToSendDTO
        );
        
    }
    
    
    /**
     * Find all shifts between two dates.
     */
    public List<Shift> findShiftsBetween(Company company, LocalDate startDate, LocalDate endDate) {
        return shiftsRepository.findShiftsBetween(company, startDate, endDate);
    }

    /**
     * Find all shifts of an operator.
     */
    // public List<Shift> findShiftsByOperator(User operator) {
    //     return shiftsRepository.findShiftsByOperator(operator);
    // }
    //
    // /**
    //  * Find all shifts of an operator between two dates.
    //  */
    // public List<Shift> findShiftsByOperatorBetween(User operator, LocalDate startDate, LocalDate endDate) {
    //     return shiftsRepository.findShiftsByOperatorBetween(operator, startDate, endDate);
    // }
    //
    // /**
    //  * Find all operators that have shifts between two dates.
    //  */
    // public List<User> findOperatorsBetween(LocalDate startDate, LocalDate endDate) {
    //     return shiftsRepository.findOperatorsBetween(startDate, endDate);
    // }
    //
    // /**
    //  * Find all operators that have no shifts between two dates.
    //  */
    // public List<User> findOperatorsWithoutShiftsBetween(LocalDate startDate, LocalDate endDate) {
    //     return shiftsRepository.findOperatorsWithoutShiftsBetween(startDate, endDate);
    // }

}