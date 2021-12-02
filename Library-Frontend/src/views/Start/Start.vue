
<template>
  <div class="Start">
        <div class="StartMenu">
          Welcome to Group 18 Library!!!!
        </div>
    <b-form-group style="height: 60vh ">
      <div style="text-align: center">
            <div @click="gotoUser()" class="goToUser">
              User
            </div>
      </div>
      <div style="text-align: center" >
        <div @click="gotoLibrarian()" class="goToLibrarian">
          Librarian
        </div>
      </div>
    </b-form-group>
    <div style="padding-top: 175px"></div>
  </div>
</template>
<script>
import userStart from '../User_Login/UserStart'
import LibrarianMenu from '../librarian-side/LibrarianMenu'
 export default{
 methods:{
   gotoUser(){
   this.$router.push('/userStart');
   },

   gotoLibrarian(){
   this.$router.push('/LibrarianMenu');
   }
   }

 }

</script>
<style>
.Start{
  background-image: url("../image/Library_BookShelves.jpeg");
  background-repeat: no-repeat;
  background-size: cover;
  background-attachment: scroll;
}
.StartMenu {
  font-size: 32px;
  font-weight: bold;
  display: flex;
  align-items: center;
  box-shadow: 0 0 50px rgba(0, 0, 0, 0.3);
  padding: 10px;
  background: #fff;
}
#Userbutton {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    color: #2c3e50;
    background: #f2ece8;
  }
#Librarianbutton {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    color: #2c3e50;
    background: #f2ece8;
  }

.goToUser{
  height:120px;
  padding: 2% 0 5% 0;
  margin : 5% 40%;
  border-radius: 15px;
  font-size: 40px;
  color: white;
  background-color: dodgerblue;
  cursor: pointer;
}

.goToLibrarian{
  height:120px;
  padding: 2% 0 5% 0;
  margin : 5% 40%;
  border-radius: 15px;
  font-size: 40px;
  color: white;
  background-color: dodgerblue;
  cursor: pointer;
}

.goToUser:hover{
  background-color: #0000ff;
}

.goToLibrarian:hover{
  background-color: #0000ff;
}
</style>
