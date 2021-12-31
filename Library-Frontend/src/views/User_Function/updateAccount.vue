<template>
  <div>

    <div class="mainBody" style="height: 65vh">
      <!--Buttons to select the information want to update, located in the left of the page-->
      <div class="leftBar">
        <div v-for="(list,index) in leftPart" @click="showUpdateContent(index)" :class="{ changeBackgroundColor:changeLeftBackground === index}">{{list.name}}</div>
      </div>

      <div class="update-form" style="float: right">

        <div class="content">
          <!--Form to update address-->
          <div class="updateAddressForm" v-show=addressUpdateShow>

            <b-form v-if="show">
              <div style="font-weight: bold">
                Address Update:
              </div>
              <div class="innerForm">
                <div class="currentUserInfo">Current Address : {{ currentUserAddress }}</div>
                <b-form-group id="input-group-10" label="Street:" label-for="addressStreet"
                              @blur.native.capture="checkAddressStreet()">
                  <b-form-input
                    id="addressStreet"
                    v-model="form.addressStreet"
                    placeholder="Enter your address"
                    required
                  ></b-form-input>

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

                    <div class="postCode">
                      <b-form-group label="PostCode" label-for="emailPostalCode"
                                    @blur.native.capture="checkPostalCode()">
                        <b-form-input
                          id="emailPostalCode"
                          v-model="form.addressPostalCode"
                          placeholder="Enter your Postal Code"
                          required
                        ></b-form-input>
                      </b-form-group>
                    </div>
                  </div>

                  <div class="formInputError">
                    {{ formInputError.addressInputError }}
                  </div>
                </b-form-group>
              </div>

              <div class="errorMessage">
                <p>
                  {{ form.error }}
                </p>
              </div>
            </b-form>

          </div>
          <!--Form to update username-->
          <div class="updateUsernameForm" v-show=usernameUpdateShow>

            <b-form v-if="show">
              <div style="font-weight: bold">
                Username update:
              </div>
              <div class="innerForm">
                <div class="currentUserInfo">Current Username : {{ currentUserUsername }}</div>
                <b-form-group id="input-group-2" label="New username:" label-for="username"
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
              </div>

              <div class="errorMessage">
                <p>
                  {{ form.error }}
                </p>
              </div>
            </b-form>

          </div>
          <!--form to update password-->
          <div class="updatePasswordForm" v-show=passwordUpdateShow>

            <b-form v-if="show">
              <div style="font-weight: bold">
                Password Update:
              </div>

              <div class="innerForm">

                <div class="confirmPassword">
                  <b-form-group label="confirmPassword" label-for="confirmPassword"
                                @blur.native.capture="checkOldPassword()">
                    <b-form-input
                      id="confirmPassword"
                      v-model="form.confirmPassword"
                      placeholder="Enter your old password"
                      type="password"
                      required
                    ></b-form-input>
                  </b-form-group>
                  <div class="formInputError">
                    {{ formInputError.confirmPasswordInputError}}
                  </div>
                </div>

                <b-form-group id="input-group-1" label="New password:" label-for="password" description=""
                              @blur.native.capture="checkPassword()" v-show="showNewPasswordInputBox">
                  <b-form-input
                    id="password"
                    v-model="form.password"
                    type="password"
                    placeholder="Enter password"
                  ></b-form-input>
                  <div class="formInputError">
                    {{ formInputError.passwordInputError }}
                  </div>
                </b-form-group>
              </div>

              <div class="errorMessage">
                <p>
                  {{ form.error }}
                </p>
              </div>
            </b-form>

          </div>
          <!--form to update email-->
          <div class="updateEmailForm" v-show=emailUpdateShow>

            <b-form v-if="show">
              <div style="font-weight: bold">
                Email address Update:
              </div>

              <div class="innerForm">
                <div class="currentUserInfo">Current Email : {{ currentUserEmail }}</div>
                <b-form-group id="input-group-1" label="New email address:" label-for="email" description=""
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

              <div class="errorMessage">
                <p>
                  {{ form.error }}
                </p>
              </div>
            </b-form>

          </div>
        </div>

      </div>

    </div>
<!--Buttons to update information or quit, located in the bottom of the page-->
    <div class="btns" style="text-align: right;margin-top: 30px">
      <b-button variant="outline-primary" @click="handleCancel">Close</b-button>
      <b-button @click="updateUserInfo()" variant="primary">Update</b-button>
    </div>

  </div>
</template>

<script src="./updateAccountJS.js">
</script>

<style scoped>
.update-form {
  margin: 0px 200px 0px 100px;
  padding-bottom: 50px;
}

.errorMessage {
  color: #dd2222;
}

.formInputError {
  color: #dd2222;
}

.selectTown {
  margin: 10px 0px 10px 0px;
}

.postCode {
  margin: 0px 0px 15px 0px;
}

.innerForm {
  margin: 10px 100px 0px 100px;
}

.leftBar {
  border: 1px solid gray;
  float: left;
  height: 65vh;
  width: 200px;
}

.leftBar div {
  height: 25%;
  padding: 20% 0;
  text-align: center;
  border: 1px solid gray;
}

.leftBar div:hover {
  background-color: deepskyblue;
}

.content {
  width: 600px
}

.currentUserInfo {
  margin-bottom: 5px;
}

.changeBackgroundColor{
  background-color: #CCD1D1;
}

</style>
