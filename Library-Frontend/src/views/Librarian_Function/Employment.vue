<template>
  <div class="Start">
    <!--    Setting of buttons and input boxes in the top of the page-->
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
      </b-form-group>
    </b-form>
    <!--Setting of bootstrap table in the middle of the page-->
    <div class="table" style="overflow: auto;height: 49vh;">
      <b-table  hover :items="librarians"
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
