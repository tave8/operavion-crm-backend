package giuseppetavella.demo_login_system.infrastructure;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CvDataModel {

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("date_of_birth")
    private String dateOfBirth;

    private String email;
    private String phone;
    private String address;
    private String nationality;
    private List<Education> education;
    private List<Experience> experience;
    private List<String> skills;
    private List<Language> languages;
    private List<String> certifications;

    @Data
    public static class Education {
        private String degree;
        private String institution;
        private String year;
    }

    @Data
    public static class Experience {
        private String company;
        private String role;
        private String from;
        private String to;
        private String description;
    }

    @Data
    public static class Language {
        private String language;
        private String level;
    }
}