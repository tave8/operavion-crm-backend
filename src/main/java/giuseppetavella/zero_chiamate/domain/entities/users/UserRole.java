package giuseppetavella.zero_chiamate.domain.entities.users;

public enum UserRole {
    ADMIN,
    COORDINATOR,
    OPERATOR;
    
    public boolean isAdmin() {
        return this.equals(UserRole.ADMIN);
    }

    public boolean isOperator() {
        return this.equals(UserRole.OPERATOR);
    }

    public boolean isCoordinator() {
        return this.equals(UserRole.COORDINATOR);
    }
    
    
}
