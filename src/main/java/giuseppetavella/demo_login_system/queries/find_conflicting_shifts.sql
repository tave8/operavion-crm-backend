/**
 * Find conflicting shifts of an operator,
 * between a date range, between a time range,
 * in a list of days.
 */
    

/**
 * The shifts of the given operator.
 */

WITH q_operator_shifts AS (
    SELECT
        *
    FROM
        shift_operators
    WHERE
        operator_id = :operatorId
),

/**
 * The shifts of the operator that conflict in date range.  
 */
 q_shifts_in_date_range AS (
     SELECT
         *
     FROM
         shifts s 
     WHERE (
        :endDate >= s.start_date
        AND :startDate <= s.end_date
     )
     AND 
        s.id IN (SELECT os.id FROM q_operator_shifts os)
            
 ),

/**
 * The days of the operator's shifts that are actually work days
    and also conflict in date range.
 */
 q_operator_shift_days AS (
     SELECT
         day
     FROM
         shift_days sd
     WHERE
         sd.shift_id IN (SELECT os.id FROM q_shifts_in_date_range os)   
 ) 
