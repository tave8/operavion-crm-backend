package giuseppetavella.zero_chiamate.domain.business.reports.contract_discrepancy;

/**
 * Do you trust this is a contract?
 * If yes (because we've just checked this is a contract)
 * avoids making an extra API call.
 */
public enum TrustThisIsContract {
    YES,
    NO;
    
    public boolean yes() {
        return this.equals(TrustThisIsContract.YES);
    }
    
    public boolean no() {
        return this.equals(TrustThisIsContract.NO);    
    }

}
