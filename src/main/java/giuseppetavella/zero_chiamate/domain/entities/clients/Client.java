package giuseppetavella.zero_chiamate.domain.entities.clients;

import giuseppetavella.zero_chiamate.domain.entities.addresses.Address;
import giuseppetavella.zero_chiamate.domain.entities.companies.Company;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    
    @ManyToOne
    @JoinColumn(name = "legal_address_id", nullable = false)
    private Address legalAddress;

    @Column(name = "legal_name", nullable = false)
    private String legalName;

    @Column(nullable = false)
    private String phone;
    
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String vat;
    
    protected Client() {}

    public Client(Company company, Address legalAddress, String email, String legalName, String vat, String phone) {
        this.company = company;
        this.email = email;
        this.legalAddress = legalAddress;
        this.legalName = legalName;
        this.vat = vat;
        this.phone = phone;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getEmail() {
        return email;
    }

    public UUID getId() {
        return id;
    }

    public Company getCompany() {
        return company;
    }

    public Address getLegalAddress() {
        return legalAddress;
    }

    public String getLegalName() {
        return legalName;
    }

    public String getPhone() {
        return phone;
    }

    public String getVat() {
        return vat;
    }

    @Override
    public String toString() {
        return "Client{" +
                "company=" + company +
                ", id=" + id +
                ", legalAddress=" + legalAddress +
                ", legalName='" + legalName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", vat='" + vat + '\'' +
                '}';
    }
}
