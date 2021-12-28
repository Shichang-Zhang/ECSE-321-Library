<template>
  <div>
    <div>
      <!--      Setting of green button in the top of the page-->
      <b-form inline>
        <b-button v-on:click="signUpPage" variant="outline-success">Create a new account for a user</b-button>
      </b-form>
    </div>
    <!--    Setting of bootstrap table displayed the users' information-->
    <div class="table" style="overflow: auto;height: 51vh;">
      <b-table style="margin-top: 40px;" hover :items="userDisplayList"
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
    <div class="btns" style="text-align: right;border-top: #e7e7e7 1px solid;padding-top: 20px">
      <b-button variant="outline-primary" @click="handleCancel">Close</b-button>
      <b-button @click="deleteUser(selectedUser)" variant="primary">Delete</b-button>
    </div>

  </div>
</template>

<script src="./LibrarianUser_JS.js">
</script>

<style scoped>
</style>
