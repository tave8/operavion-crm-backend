package giuseppetavella.zero_chiamate.config;

public enum ReportTemplate implements Template {

    // reports
    CONTRACT_DISCREPANCY("reports/contract_discrepancy"),
    SHIFTS_COUNT_BY_OPERATOR("reports/shifts_count_by_operator");
    // add more report templates here...

    private final String value;

    ReportTemplate(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
    
}
