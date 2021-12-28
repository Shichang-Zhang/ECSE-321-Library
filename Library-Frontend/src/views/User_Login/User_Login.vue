<template id="login">
  <div class="user-main">
    <div class="user-info">
      <b-button @click="gotoStart" variant="primary">Home</b-button>
      <div style="padding-left: 40%">
        Login
      </div>
    </div>

    <div class="menu-items">
      <div class="signup-form">
        <div class="signup-form-title" style="font-size: 50px">
          Welcome
        </div>
        <!--Form of submitting user login request-->
        <b-form @submit="onSubmit" @reset="onReset" v-if="show">

          <div id="onlineAccountInfo" class="onlineAccountInfo">
            <!--username-->
            <b-form-group id="input-group-2" label="Username:" label-for="username"
                          @blur.native.capture="checkUsername()">
              <b-form-input
                id="username"
                v-model="form.username"
                placeholder="Enter username"
              ></b-form-input>
              <div class="formInputError">
                {{ formInputError.usernameInputError }}
              </div>
            </b-form-group>
            <!--password-->
            <b-form-group id="input-group-5" label="Password:" label-for="password">
              <b-form-input
                id="password"
                v-model="form.password"
                placeholder="Enter password"
                type="password"
              ></b-form-input>
            </b-form-group>

          </div>

          <div class="errorMessage" id="errorMessage">
            <p>
              {{ form.error }}
            </p>
          </div>

          <div style="text-align: center">
            <b-button size="lg" type="submit" variant="primary">Login</b-button>
            <b-button size="lg" type="reset" variant="danger">Reset</b-button>
          </div>
        </b-form>

        <div class="signUp">
          <b-button @click="signUpPage" size="lg" type="submit" variant="success">Don't have an online acount?
          </b-button>
        </div>
        <div style="padding-top: 550px"></div>

      </div>
    </div>


  </div>
</template>
<script src="./User_Login_JS.js">
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

.menu-items {
  margin-top: 50px;
}

.signup-form {
  margin: 0px 400px 0px 400px;
  padding-bottom: 50px;
}

.signup-form-title {
  text-align: center;
  font-size: 26px;
  font-weight: bold;
  margin-bottom: 20px;
}

.signUp {
  padding-top: 30px;
  text-align: center
}

.errorMessage {
  color: #dd2222;
}

.formInputError {
  color: #dd2222;
}

</style>
