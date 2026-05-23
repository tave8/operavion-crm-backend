package giuseppetavella.demo_login_system.domain.entities.addresses;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "addresses")
public class Address {
    
    @Id
    @GeneratedValue
    private UUID id;
    
    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(nullable = false)
    private Double lat;
    
    @Column(nullable = false)
    private Double lon;
    
    protected Address() {}

    public Address(Double lat, Double lon, String displayName) 
    {
        this.displayName = displayName;
        this.lat = lat;
        this.lon = lon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Address{" +
                "displayName='" + displayName + '\'' +
                ", id=" + id +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
