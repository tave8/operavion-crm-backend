package giuseppetavella.zero_chiamate.unit.state_transition;

import giuseppetavella.zero_chiamate.helpers.ValidationHelper;
import giuseppetavella.zero_chiamate.integrations.stripe.StripeAPISubscriptionStatus;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class StripeSubscriptionStateTransitionTest {
    
    // @Test
    // public void canStartWithIncomplete() {
    //     StripeAPISubscriptionStatus currentStatus = this.getStripeSubscriptionStatus();
    //
    //     boolean noStateYet = currentStatus == null;
    //
    //     List<StripeAPISubscriptionStatus> firstStates = List.of(
    //             StripeAPISubscriptionStatus.INCOMPLETE
    //     );
    //
    //     Map<StripeAPISubscriptionStatus, List<StripeAPISubscriptionStatus>> stateMap = Map.of(
    //             StripeAPISubscriptionStatus.INCOMPLETE, List.of(
    //                     StripeAPISubscriptionStatus.TRIALING,
    //                     StripeAPISubscriptionStatus.ACTIVE
    //             ),
    //             StripeAPISubscriptionStatus.TRIALING, List.of(
    //                     StripeAPISubscriptionStatus.ACTIVE,
    //                     StripeAPISubscriptionStatus.PAST_DUE,
    //                     StripeAPISubscriptionStatus.CANCELED
    //             ),
    //             StripeAPISubscriptionStatus.ACTIVE, List.of(
    //                     StripeAPISubscriptionStatus.PAST_DUE,
    //                     StripeAPISubscriptionStatus.CANCELED
    //             ),
    //             StripeAPISubscriptionStatus.PAST_DUE, List.of(
    //                     StripeAPISubscriptionStatus.ACTIVE,
    //                     StripeAPISubscriptionStatus.CANCELED
    //             ),
    //             StripeAPISubscriptionStatus.CANCELED, List.of()
    //     );
    //
    //     ValidationHelper.requireValidStateTransition(
    //             StripeAPISubscriptionStatus.class,
    //             currentStatus,
    //             stripeSubscriptionStatus,
    //             firstStates,
    //             stateMap,
    //             noStateYet,
    //             "stripe subscription"
    //     );
    //
    //     this.stripeSubscriptionStatus = stripeSubscriptionStatus;
    // }
    //
}
