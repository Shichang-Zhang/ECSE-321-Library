package ca.mcgill.ecse321.library.dto;

/**
 * data transfer object of user model.
 * extends the person DTO, so it can also have access to the user's name, address and id.
 * link to a nullable online DTO which is the user's online account.
 * have getter and setter methods for private fields.
 */
public class UserDto extends PersonDto{
    private boolean isLocal;
    private OnlineAccountDto onlineAccountDto;

    public UserDto(int id) {
        super(id);
    }

    public UserDto(int id, String name, String address) {
        super(id, name, address);
    }

    public UserDto(int id, boolean isLocal, OnlineAccountDto onlineAccountDto) {
        super(id);
        this.isLocal = isLocal;
        this.onlineAccountDto = onlineAccountDto;
    }

    public UserDto(int id, String name, String address, boolean isLocal, OnlineAccountDto onlineAccountDto) {
        super(id, name, address);
        this.isLocal = isLocal;
        this.onlineAccountDto = onlineAccountDto;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public OnlineAccountDto getOnlineAccountDto() {
        return onlineAccountDto;
    }

    public void setOnlineAccountDto(OnlineAccountDto onlineAccountDto) {
        this.onlineAccountDto = onlineAccountDto;
    }
}
