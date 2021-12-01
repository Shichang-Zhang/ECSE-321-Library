package ca.mcgill.ecse321.library.controller;
import ca.mcgill.ecse321.library.dto.PersonDto;
import ca.mcgill.ecse321.library.dto.UserDto;
import ca.mcgill.ecse321.library.model.OnlineAccount;
import ca.mcgill.ecse321.library.model.User;
import ca.mcgill.ecse321.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * this class contains the controller methods to call perform user database operations using business service methods
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    /**
     * create user with input parameters
     * @param name user's name
     * @param address user's address
     * @param isLocal whether user is local
     * @return the data transfer object of the created user object
     * @throws IllegalArgumentException
     */
    @PostMapping("/createUser")
    public UserDto createUsers(@RequestParam("name") String name,
                               @RequestParam("address") String address,
                               @RequestParam boolean isLocal)
            throws IllegalArgumentException{
        User user= userService.createUser(name, address, isLocal);
        return HelperMethods.convertToDto(user);
    }

    /**
     * get all users of the library
     * @return a list of the data transfer object of the users in the library
     */
    @GetMapping("/userList")
    public List<PersonDto> getAllUsers(){
        return userService.getAllUser().stream().map(u->HelperMethods.convertToDto((User)u)).collect(Collectors.toList());
    }

    /**
     * update the user's attribute isLocal
     * @param uid user id
     * @param isLocal
     * @return the data transfer object of the user object with the update attribute isLocal
     * @throws IllegalArgumentException
     */
    @PutMapping("/updateIsLocal")
    public UserDto updateIsLocal(@RequestParam("uid") int uid,@RequestParam("isLocal") boolean isLocal)
            throws IllegalArgumentException{
        User user=userService.updateIsLocal(uid,isLocal);
        UserDto userDto=HelperMethods.convertToDto(user);
        return userDto;
    }

    /**
     * update the user's attribute address
     * @param uid user id
     * @param address user's address
     * @return the data transfer object of the user object with the update attribute address
     * @throws IllegalArgumentException
     */
    @PutMapping("/updateAddress")
    public UserDto updateAddress(@RequestParam("uid") int uid,@RequestParam("address") String address)throws IllegalArgumentException{
        User user = (User) userService.updateAddress(uid,address);
        return HelperMethods.convertToDto(user);
    }

    /**
     * update the user's online account information.
     * If the user does not have the online account, an online account with the input information will be created.
     * @param uid user id
     * @param username
     * @param password
     * @param email
     * @return the data transfer object of the user object with the update online account
     * @throws IllegalArgumentException
     */
    @PutMapping("/updateOnlineAccount")
    public UserDto updateOnlineAccount(@RequestParam("uid") int uid, @RequestParam("username") String username,
                                                @RequestParam("password") String password, @RequestParam("email") String email)
    throws IllegalArgumentException{
        User user=userService.updateOnlineAccount(uid, username, password, email);
        UserDto userDto=HelperMethods.convertToDto(user);
        userDto.setOnlineAccountDto(HelperMethods.convertToDto(user.getOnlineAccount()));
        return userDto;
    }

    /**
     * update the user's online account username
     * If the user does not have the online account, an online account with the input information will be created.
     * @param uid user id
     * @param username
     * @return the data transfer object of the user object with the update online account
     * @throws IllegalArgumentException
     */
    @PutMapping("/updateOnlineAccountUsername")
    public UserDto updateOnlineAccountUsername(@RequestParam("uid") int uid, @RequestParam("username") String username)
            throws IllegalArgumentException{
        User user=userService.updateOnlineAccountUsername(uid, username);
        UserDto userDto=HelperMethods.convertToDto(user);
        userDto.setOnlineAccountDto(HelperMethods.convertToDto(user.getOnlineAccount()));
        return userDto;
    }

    @PutMapping("/updateOnlineAccountEmail")
    public UserDto updateOnlineAccountEmail(@RequestParam("uid") int uid, @RequestParam("email") String email)
            throws IllegalArgumentException{
        User user=userService.updateOnlineAccountEmail(uid, email);
        UserDto userDto=HelperMethods.convertToDto(user);
        userDto.setOnlineAccountDto(HelperMethods.convertToDto(user.getOnlineAccount()));
        return userDto;
    }


    /**
     * get user by id
     * @param uid user id
     * @return the data transfer object of the user with input id
     * @throws IllegalArgumentException
     */
    @GetMapping("/getUserById")
    public UserDto getUserById(@RequestParam("uid") int uid) throws IllegalArgumentException {
        User user = userService.getUserById(uid);
        return HelperMethods.convertToDto(user);
    }

    /**
     * when user clicked the login online account button, find the online account by username and then check the input passward with the online
     * account password. This method will return a message. If login successfully, the message contains "success:{uid}". If fail to login,
     * the message contains "error:{error message}".
     * Ex. return "success:12345678"
     * Ex. return "error:password incorrect!"
     * @param username
     * @param password
     * @return If login successfully, the message contains "success:{uid}". If fail to login, the message contrains "error:{error message}".
     */
    @PostMapping("/login")
    public UserDto login(@RequestParam("username") String username,@RequestParam("password") String password)
    throws IllegalArgumentException{
        String message="";
        OnlineAccount onlineAccount = userService.findOnlineAccountByUsername(username);

        //fail to login
        if (onlineAccount==null){
            message = "error:online account with given username does not exit!";
        }
        if ( onlineAccount!=null &&!onlineAccount.getPassword().equals(password)){
            message= "error:password incorrect!";
        }

        if(message.length()>0){
            throw new IllegalArgumentException(message);
        }

        UserDto userDto=HelperMethods.convertToDto(onlineAccount.getUser());

        return userDto;
    }

    /**
     * when register/login online account, after typing the username, the frontend send ajax request to check whether the username exist and give the feedback
     * if return true, means username not exist, if return false, means username has existed.
     * @param username the username of the user's online account
     * @return if return true, means username not exist, if return false, means username has existed.
     * @throws IllegalArgumentException
     */
    @PostMapping("/checkUsernameExistence")
    public boolean checkUsernameExistence(@RequestParam("username") String username) throws IllegalArgumentException{
        boolean flag=false;
        OnlineAccount onlineAccount;
        try{
            onlineAccount=userService.findOnlineAccountByUsername(username);
        }catch (IllegalArgumentException e ){
            onlineAccount=null;
        }
        if (onlineAccount==null){
            flag=true;
        }
        return flag;
    }

    /**
     * a user clicks the sign up online account
     * @param uid user id
     * @param username
     * @param password
     * @param email
     * @return a empty message means sign up successfully, otherwise an error message with alert
     * @throws IllegalArgumentException
     */
    @PostMapping("/signUpOnlineAccount")
    public String signUp (@RequestParam("uid") int uid,
                          @RequestParam("username") String username,
                          @RequestParam("password") String password,
                          @RequestParam("email") String email) throws IllegalArgumentException{
        String error = "";
        User user = userService.getUserById(uid);
        if (user==null){
            error=error+"user does not exist";
        }
        OnlineAccount onlineAccount = userService.findOnlineAccountByUsername(username);
        if (onlineAccount!=null){
            error=error+"username already exist";
        }
        if (error.equals("")){
            try {
                userService.updateOnlineAccount(uid,username,password,email);
            }catch (IllegalArgumentException e){
                error=error+e.getMessage();
            }
        }
        return error;
    }




}
