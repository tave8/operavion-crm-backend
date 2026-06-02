package giuseppetavella.zero_chiamate.unit.time;

import giuseppetavella.zero_chiamate.helpers.TimeHelper;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TimeTest {


    @Test
    void isFuture() {
        // given
        var time = OffsetDateTime.now().plusSeconds(1);
                
        // when
        var result = TimeHelper.isFuture(time);
        
        // then     
        assertTrue(result);
    }


    @Test
    void isNotFuture() {
        // given
        var time = OffsetDateTime.now().minusSeconds(1);

        // when
        var result = TimeHelper.isFuture(time);

        // then     
        assertFalse(result);
    }
    

    @Test
    void isPast() {
        // given
        var time = OffsetDateTime.now().minusSeconds(1);

        // when
        var result = TimeHelper.isPast(time);

        // then     
        assertTrue(result);
    }

    @Test
    void isNotPast() {
        // given
        var time = OffsetDateTime.now().plusSeconds(1);

        // when
        var result = TimeHelper.isPast(time);

        // then     
        assertFalse(result);
    }



    @Test
    void isExpiredWithin1() {
        // given
        var minutes = 1;
        var time = OffsetDateTime.now().minusSeconds(61);

        // when
        var result = TimeHelper.isExpiredWithin(time, minutes);

        // then     
        assertTrue(result);
    }

    
    @Test
    void isExpiredWithin2() {
        // given
        var minutes = 1;
        var time = OffsetDateTime.now().minusSeconds(61);

        // when
        var result = TimeHelper.isNotExpiredWithin(time, minutes);

        // then     
        assertFalse(result);
    }


    @Test
    void isNotExpiredWithin1() {
        // given
        var minutes = 1;
        var time = OffsetDateTime.now().minusSeconds(59);

        // when
        var result = TimeHelper.isNotExpiredWithin(time, minutes);

        // then     
        assertTrue(result);
    }


    @Test
    void isNotExpiredWithin2() {
        // given
        var minutes = 1;
        var time = OffsetDateTime.now().minusSeconds(59);

        // when
        var result = TimeHelper.isExpiredWithin(time, minutes);

        // then     
        assertFalse(result);
    }

}