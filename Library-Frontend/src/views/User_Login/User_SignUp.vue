<template id="signup">
  <div class="user-main">
    <div class="user-info">
      <b-button @click="gotoStart" variant="primary">Home</b-button>
      <div style="padding-left: 40%">
        Sign Up
      </div>
    </div>

    <div class="menu-items">
      <div class="signup-form">


        <div class="signup-form-title" style="font-size: 50px">
          Welcome
        </div>
        <!--        Form of submitting sign up request-->
        <b-form @submit="onSubmit" @reset="onReset" v-if="show" style="color: black;font-weight: bold">
          <!--name-->
          <b-form-group id="input-group-9" label="Name:" label-for="realName" @blur.native.capture="checkName()">
            <b-form-input
              id="realName"
              v-model="form.name"
              placeholder="Enter your name"
              required
            ></b-form-input>
            <div class="formInputError">
              {{ formInputError.nameInputError }}
            </div>
          </b-form-group>
          <!--Street (address)-->
          <b-form-group id="input-group-10" label="Street:" label-for="addressStreet"
                        @blur.native.capture="checkAddressStreet()">
            <b-form-input
              id="addressStreet"
              v-model="form.addressStreet"
              placeholder="Enter your address"
              required
            ></b-form-input>
            <!--Town (address)-->
            <div>
              <div class="selectTown">
                <b-form-group label="Town:" label-for="townSelection">
                  <b-form-select
                    id="townSelection"
                    v-model="form.addressTown"
                    :options="[{ text: 'Choose...', value: null }, 'videha(local)', 'Apara-godaniya', 'Uttarakuru','Jambu-dvipa']"
                    :value="null"
                    required
                  ></b-form-select>
                </b-form-group>
              </div>
              <!--Post Code (address)-->
              <div class="postCode">
                <b-form-group label="PostCode" label-for="emailPostalCode" @blur.native.capture="checkPostalCode()">
                  <b-form-input
                    id="emailPostalCode"
                    v-model="form.addressPostalCode"
                    placeholder="Enter your Postal Code"
                    required
                  ></b-form-input>
                </b-form-group>
              </div>
            </div>
            <!--error-->
            <div class="formInputError">
              {{ formInputError.addressInputError }}
            </div>
          </b-form-group>

          <div></div>
          <!--Select whether to have an online account-->
          <b-form-group id="input-group-5" label="Want an online account?" v-slot="{ ariaDescribedby }">
            <b-form-checkbox v-model="isShow" name="check-button" switch>
              <b>{{ (isShow ? "Yes" : "No") }}</b>
            </b-form-checkbox>
          </b-form-group>

          <!--Online account information-->
          <div id="onlineAccountInfo" class="onlineAccountInfo" v-show="isShow">
            <!--Username-->
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
            <!--Password-->
            <b-form-group id="input-group-5" label="Password:" label-for="password"
                          @blur.native.capture="checkPassword()">
              <b-form-input
                id="password"
                v-model="form.password"
                placeholder="Enter password"
                type="password"
              ></b-form-input>
              <div class="formInputError">
                {{ formInputError.passwordInputError }}
              </div>
            </b-form-group>
            <!--Confirm passoword-->
            <b-form-group id="input-group-6" label="Confirm password:" label-for="confirmPassword"
                          @blur.native.capture="confirmPassword()">
              <b-form-input
                id="confirmPassword"
                v-model="form.confirmPassword"
                placeholder="Confirm password"
                type="password"
              ></b-form-input>
              <div class="formInputError">
                {{ formInputError.confirmPasswordError }}
              </div>
            </b-form-group>
            <!--Email-->
            <b-form-group id="input-group-1" label="Email address:" label-for="email" description=""
                          @blur.native.capture="checkEmail()">
              <b-form-input
                id="email"
                v-model="form.email"
                type="email"
                placeholder="Enter email"
              ></b-form-input>
              <div class="formInputError">
                {{ formInputError.emailInputError }}
              </div>
            </b-form-group>
          </div>

          <div class="errorMessage" id="errorMessage">
            <p>
              {{ form.error }}
            </p>
          </div>

          <div style="text-align: center">
            <b-button type="submit" variant="primary">Submit</b-button>
            <b-button type="reset" variant="danger">Reset</b-button>
          </div>
        </b-form>

        <div style="padding-top: 200px"></div>

      </div>
    </div>

  </div>
</template>
<script src="./User_SignUp_JS.js">
</script>
<style>
.user-main {
  background-image: url("../image/Library_BookShelves.jpeg");
  background-repeat: no-repeat;
  background-size: cover;
  background-attachment: fixed;
}

.user-info {
  font-size: 32px;
  font-weight: bold;
  display: flex;
  align-items: center;
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

.errorMessage {
  color: #dd2222;
}

.formInputError {
  color: #dd2222;
}

.selectTown {
  margin: 15px 0px 15px 0px;
}

.postCode {
  margin: 0px 0px 15px 0px;
}

</style>
