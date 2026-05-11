package giuseppetavella.demo_login_system.entities;

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
    private double lat;
    
    @Column(nullable = false)
    private double lon;
    
    protected Address() {}

    public Address(double lat, double lon, String displayName) 
    {
        this.displayName = displayName;
        this.lat = lat;
        this.lon = lon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
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
