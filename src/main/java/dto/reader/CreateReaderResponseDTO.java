package dto.reader;

public class CreateReaderResponseDTO {

    private final Long id;

    private final String firstName;
    private final String lastName;

    private final String phone;

    public CreateReaderResponseDTO(Long id, String firstName, String lastName, String phone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }
}
