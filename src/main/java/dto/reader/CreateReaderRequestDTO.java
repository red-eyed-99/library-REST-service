package dto.reader;

public class CreateReaderRequestDTO {

    private final String firstName;
    private final String lastName;

    private final String phone;

    public CreateReaderRequestDTO(String firstName, String lastName, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
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
