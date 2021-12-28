<template>
  <div class="Start">
    <!--    Setting of buttons and input boxes in the top of the page-->
    <b-form inline>
      <b-form-group>
        <b-form-input
          id="input-1"
          class="mb-2 mr-sm-2 mb-sm-0"
          placeholder="Name"
          v-model="librarianName"
        ></b-form-input>
        <b-form-input
          id="input-1"
          class="mb-2 mr-sm-2 mb-sm-0"
          placeholder="Address"
          v-model="librarianAddress"
        ></b-form-input>
        <b-button v-on:click="hireLibrarian(librarianName,librarianAddress)" variant="primary"
                  style="margin-left: 10px;" type="reset">hire
        </b-button>

      </b-form-group>
      <br>
      <b-form-group id="input-group-3" style="margin: 0px;" label-for="input-1">
        <b-form-input
          id="input-1"
          class="mb-2 mr-sm-2 mb-sm-0"
          placeholder="BusinessHour"
          v-model="librarianBusinessHour"
        ></b-form-input>
      </b-form-group>
      <b-button v-on:click="findLibrarian(librarianId)" variant="primary" style="margin-left: 0px;" type="reset">
        Search
      </b-button>
      <b-button v-on:click="showAllLibrarian" variant="primary" style="margin-left: 10px;" type="reset">Show All
      </b-button>
    </b-form>
    <!--Setting of bootstrap table in the middle of the page-->
    <div class="table" style="overflow: auto;height: 45vh;">
      <b-table style="margin-top: 10px;" hover :items="librarians"
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
    <div class="btns" style="text-align: right;border-top: #e7e7e7 1px solid;padding-top: 10px">
      <b-button v-on:click="fireLibrarian(parseInt(selectedLibrarians[0].id))" variant="primary"
                style="margin-left: 10px;" type="reset">fire
      </b-button>
      <b-button v-on:click="assignBusinessHour(selectedLibrarians[0].id, librarianBusinessHour)" variant="primary"
                style="margin-left: 10px;" type="reset">assign
      </b-button>
      <b-button v-on:click="unassignBusinessHour(selectedLibrarians[0].id, librarianBusinessHour)" variant="primary"
                style="margin-left: 10px" type="reset">unassign
      </b-button>
      <b-button v-on:click="updateHead(selectedLibrarians[0].id)" variant="primary"
                style="margin-left: 10px;margin-right: 10px" type="reset">updateHead
      </b-button>

      <b-button variant="outline-primary" @click="handleCancel">Close</b-button>
    </div>
  </div>
</template>
<script src="./LibrarianEmployment_JS.js">
</script>
<style scoped>
</style>
