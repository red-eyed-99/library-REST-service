package dto.author;

public class CreateAuthorRequestDTO {

    private final String firstName;
    private final String lastName;

    public CreateAuthorRequestDTO(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
