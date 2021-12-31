<template>
  <div>
    <div>
      <!--      Setting of green button in the top of the page-->
      <b-form inline style="margin: 15px 0">
        <b-form-group
          label-for="eventSearchName">
          <b-form-input
            id="newEventNameInput"
            placeholder="Librarian Name"
            v-model="eventSearchName"
          ></b-form-input>
          <b-button variant="outline-primary" @click="findEventsByName(eventSearchName)">Search</b-button>
          <b-button variant="outline-primary" @click="refreshEvent()">Show All</b-button>
          <b-button v-on:click="signUpPage" variant="outline-success">Create a new account for a user</b-button>
        </b-form-group>
      </b-form>
    </div>
    <!--    Setting of bootstrap table displayed the users' information-->
    <div class="table" style="overflow: auto;height: 49vh;">
      <b-table hover :items="userDisplayList"
               @row-selected="onRowSelected"
               select-mode="single"
               selectable>
      </b-table>
    </div>

    <div style="display: flex;justify-content: flex-end;">
      <b-pagination
        v-model="currentPage"
        :total-rows="rows"
        :per-page="perPage"
        aria-controls="my-table"
      ></b-pagination>
    </div>
    <!--Setting of buttons in the bottom of the page-->
    <div class="btns" style="text-align: right;padding-top: 10px">
      <b-button variant="outline-primary" @click="handleCancel">Close</b-button>
    </div>

  </div>
</template>

<script src="./LibrarianUser_JS.js">
</script>

<style scoped>
</style>
