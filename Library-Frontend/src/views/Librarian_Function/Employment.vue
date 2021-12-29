<template>
  <div class="Start">
    <!--    Setting of buttons and input boxes in the top of the page-->
    <b-form inline>
<!--      <b-form-group>-->
<!--        <b-form-input-->
<!--          id="input-1"-->
<!--          class="mb-2 mr-sm-2 mb-sm-0"-->
<!--          placeholder="Name"-->
<!--          v-model="librarianName"-->
<!--        ></b-form-input>-->
<!--        <b-form-input-->
<!--          id="input-1"-->
<!--          class="mb-2 mr-sm-2 mb-sm-0"-->
<!--          placeholder="Address"-->
<!--          v-model="librarianAddress"-->
<!--        ></b-form-input>-->
<!--        <b-button v-on:click="hireLibrarian(librarianName,librarianAddress)" variant="primary"-->
<!--                  style="margin-left: 10px;" type="reset">hire-->
<!--        </b-button>-->

<!--      </b-form-group>-->
<!--      <br>-->
<!--      <b-form-group id="input-group-3" style="margin: 0px;" label-for="input-1">-->
<!--        <b-form-input-->
<!--          id="input-1"-->
<!--          class="mb-2 mr-sm-2 mb-sm-0"-->
<!--          placeholder="BusinessHour"-->
<!--          v-model="librarianBusinessHour"-->
<!--        ></b-form-input>-->
<!--      </b-form-group>-->
<!--      <b-button v-on:click="findLibrarian(librarianId)" variant="primary" style="margin-left: 0px;" type="reset">-->
<!--        Search-->
<!--      </b-button>-->
<!--      <b-button v-on:click="showAllLibrarian" variant="primary" style="margin-left: 10px;" type="reset">Show All-->
<!--      </b-button>-->
    </b-form>
    <!--Setting of bootstrap table in the middle of the page-->
    <div class="table" style="overflow: auto;height: 55vh;">
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
      <b-button variant="primary" @click="hireStep1">Hire</b-button>
      <b-button variant="primary" @click="fireLibrarian(selectedLibrarians)">Fire</b-button>
      <b-button variant="primary" @click="updateIsHeadStep1(selectedLibrarians)">Update to Head Librarian</b-button>
      <b-button variant="primary" @click="assignBusinessHourStep1(selectedLibrarians)">Assign Business Hour</b-button>
      <b-button variant="primary" @click="unassignBusinessHourStep1(selectedLibrarians)">Unassign Business Hour</b-button>
      <b-button variant="outline-primary" @click="handleCancel">Close</b-button>
    </div>
    <!--Setting of panel of hiring new librarian-->
    <b-modal id="createNewLibrarian" title="Hire new librarian"
             @ok="hireStep2(librarianName,librarianAddress)">
      <div>
        <b-form>
          <b-form-group
            label="Name"
            label-for="librarianName">
            <b-form-input
              id="librarianName"
              placeholder="Librarian's Name"
              v-model="librarianName"
            ></b-form-input>
          </b-form-group>

          <b-form-group
            label="Address"
            label-for="librarianAddress">
            <b-form-input
              id="librarianAddress"
              placeholder="Librarian's Address"
              v-model="librarianAddress"
            ></b-form-input>
          </b-form-group>
        </b-form>
      </div>
    </b-modal>
<!--Warning when updating another librarian to head librarian-->
    <b-modal id="updateIsHeadWarning" title="Warning"
             @ok="updateIsHeadStep2(selectedLibrarians)">
      <div>
        Are you sure you want to transfer your head librarian identity to {{selectedLibrarians.length>0?selectedLibrarians[0].name:null}} ?<br>
        The systems will automatically logout after this operation.
      </div>
    </b-modal>

    <!--Setting of panel of assigning business hour to a librarian-->
    <b-modal id="assignBusinessHour" title="Assign Business Hour"
             @ok="assignBusinessHourStep2(selectedLibrarians,librarianBusinessHour)">
      <div>
        <b-form>
          <b-form-group
            label="Day Of Week"
            label-for="businessHourDayOfWeek"
          >
            <b-form-select
              id="businessHourDayOfWeek"
              v-model="librarianBusinessHour"
              :options="dayOfWeekOptions"
              required
            ></b-form-select>
          </b-form-group>
        </b-form>
      </div>
    </b-modal>
    <!--Setting of panel of unassigning business hour to a librarian-->
    <b-modal id="unassignBusinessHour" title="Unassign Business Hour"
             @ok="unassignBusinessHourStep2(selectedLibrarians,librarianBusinessHour)">
      <div>
        <b-form>
          <b-form-group
            label="Day Of Week"
            label-for="businessHourDayOfWeek"
          >
            <b-form-select
              id="businessHourDayOfWeek"
              v-model="librarianBusinessHour"
              :options="dayOfWeekOptions"
              required
            ></b-form-select>
          </b-form-group>
        </b-form>
      </div>
    </b-modal>
  </div>
</template>
<script src="./LibrarianEmployment_JS.js">
</script>
<style scoped>
</style>
