<template>
  <div class="LibrarianMenu">
    <div class="librarian-main">
      <div class="librarian-info">
        <b-button @click="gotoStart" variant="primary">Home</b-button>
        <div style="padding-left: 200px">
          Welcome librarian login!
        </div>
      </div>

      <div class="menu-items">
        <div class="signup-form">
          <!--          Title-->
          <div class="signup-form-title" style="font-size: 50px">
            Welcome
          </div>
          <!--          Form to submit the request of logging in-->
          <b-form @submit="onSubmit" @reset="onReset" v-if="show">

            <div id="onlineAccountInfo" class="onlineAccountInfo">
              <b-form-group id="input-group-2" label="Librarian Id:" label-for="librarianId">
                <b-form-input
                  id="librarianId"
                  v-model="form.id"
                  placeholder="Enter Librarian Id"
                ></b-form-input>
              </b-form-group>
            </div>
            <!--error message div-->
            <div class="errorMessage" id="errorMessage">
              <p>
                {{ form.error }}
              </p>
            </div>
            <!--Buttons of login and reset-->
            <div style="text-align: center">
              <b-button type="submit" size="lg" variant="primary">Login</b-button>
              <b-button type="reset" size="lg" variant="danger">Reset</b-button>
            </div>
          </b-form>
          <div style="padding-top: 550px"></div>

        </div>
      </div>
    </div>

  </div>
</template>

<script src="./LibrarianMenu.js">
</script>

<style>
.librarian-main {
  background-image: url("../image/Library_BookShelves.jpeg");
  background-repeat: no-repeat;
  background-size: cover;
  background-attachment: scroll;
}

.librarian-info {
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

.errorMessage {
  color: #dd2222;
}

</style>

