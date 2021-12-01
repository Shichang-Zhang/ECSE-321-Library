package ca.mcgill.ecse321.library;

import ca.mcgill.ecse321.library.service.LibrarianService;
import ca.mcgill.ecse321.library.service.LibrarySystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@SpringBootApplication
public class LibraryApplication {
  @Autowired
  LibrarySystemService librarySystemService;
  @Autowired
  LibrarianService librarianService;

  public static void main(String[] args) {
    SpringApplication.run(LibraryApplication.class, args);
  }

  @RequestMapping("/")
  public String greeting(){
    return "Welcome to Group 18 CarShop!!!!!!!";
  }

  @GetMapping("/init")
  public String initialize(){
    boolean flag=false;
    try{
      librarySystemService.createLibrarySystem();
      librarianService.initHeadLibrarian();

     flag=true;
   }catch (Exception e){
     e.printStackTrace();
   }

   if(flag){
      return "Success!";
   }else{
     return "Fail!";
   }
  }


}

