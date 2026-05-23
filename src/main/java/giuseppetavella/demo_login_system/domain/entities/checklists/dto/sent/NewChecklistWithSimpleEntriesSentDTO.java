package giuseppetavella.demo_login_system.domain.entities.checklists.dto.sent;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * "Simple entries" refers to the fact that when 
 * the client sends this payload, we do not know the 
 * checklist id, because it has not been generated yet.
 * 
 * Therefore it is a "simple entry" because the checklist entry
 * is not an actual checklist entry yet. An actual checklist entry
 * also has the checklist id (along with task id and position, of course).
 * 
 * @param name
 * @param entries
 */
public record NewChecklistWithSimpleEntriesSentDTO(
    
    //  checklist name
    @NotNull(message = "Missing 'name' field")
    String name,
    
    @NotNull(message = "Missing 'entries' field")
    // the valid annotation triggers cascading validation 
    // if we don't specify it, spring will not validate this "inner" payload
    @Valid
    List<ChecklistSimpleEntrySentDTO> entries
        
) {
}
