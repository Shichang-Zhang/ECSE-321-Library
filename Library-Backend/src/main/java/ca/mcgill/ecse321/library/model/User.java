package ca.mcgill.ecse321.library.model;
import javax.persistence.*;

/**
 * users, have an attribute isLocal that librarian will or will not charge them when using facilities.
 * users can choose whether he or she should link to an online account.
 */
@Entity
public class User extends Person {

  @Column
  private boolean isLocal;
  @Column
  private OnlineAccount onlineAccount;


  public User(){
    super();
  }

  public void setIsLocal(boolean aIsLocal) {
    isLocal = aIsLocal;
  }

  public boolean getIsLocal()
  {
    return isLocal;
  }

  public void setOnlineAccount(OnlineAccount onlineAccount) {
    this.onlineAccount = onlineAccount;
  }
  @OneToOne(cascade = { CascadeType.ALL })
  public OnlineAccount getOnlineAccount()
  {
    return onlineAccount;
  }

}