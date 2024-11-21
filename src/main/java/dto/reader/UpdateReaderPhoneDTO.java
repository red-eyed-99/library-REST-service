package dto.reader;

public class UpdateReaderPhoneDTO {

    private final Long id;

    private final String phone;

    public UpdateReaderPhoneDTO(Long id, String phone) {
        this.id = id;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }
}
