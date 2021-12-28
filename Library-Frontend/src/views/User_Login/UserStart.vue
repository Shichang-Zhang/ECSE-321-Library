<template>
  <div class="user-main">
    <!--Go to home page button-->
    <div class="user-info">
      <b-button @click="gotoStart" variant="primary">Home</b-button>
      <div style="padding-left: 40%">
        Welcome User! Please log in or sign up!
      </div>
    </div>
    <!--    Main field-->
    <b-form-group style="height: 60vh ">
      <!--      Login button-->
      <div style="text-align: center">
        <div @click="gotologin()" class="goToLogin">
          Login
        </div>
      </div>
      <!--      Sign up button-->
      <div style="text-align: center">
        <div @click="gotosignup()" class="goToSignUp">
          SignUp
        </div>
      </div>
    </b-form-group>
    <div style="padding-top: 175px"></div>
  </div>
</template>
<script>
import login from './User_Login_JS'
import start from './UserStart'
import signup from './User_SignUp'
import shelf from '../image/BookShelf.jpeg'

export default {
  image: shelf,
  methods: {
    gotosignup() {
      this.$router.push('/signup');
    },

    gotologin() {
      this.$router.push('/login');
    },

    gotoStart() {
      this.$router.push('/');
    },
  }

}

</script>
<style>

.user-main {
  background-image: url("../image/Library_BookShelves.jpeg");
  background-repeat: no-repeat;
  background-size: cover;
  background-attachment: scroll;
}

.user-info {
  font-size: 32px;
  font-weight: bold;
  display: flex;
  text-align: center;
  box-shadow: 0 0 50px rgba(0, 0, 0, 0.3);
  padding: 10px;
  background: #fff;
}


.goToLogin {
  height: 120px;
  padding: 2% 0 5% 0;
  margin: 5% 40%;
  border-radius: 15px;
  font-size: 40px;
  color: white;
  background-color: dodgerblue;
  cursor: pointer;
}

.goToSignUp {
  height: 120px;
  padding: 2% 0 5% 0;
  margin: 5% 40%;
  border-radius: 15px;
  font-size: 40px;
  color: white;
  background-color: dodgerblue;
  cursor: pointer;
}

.goToLogin:hover {
  background-color: #0000ff;
}

.goToSignUp:hover {
  background-color: #0000ff;
}

</style>
