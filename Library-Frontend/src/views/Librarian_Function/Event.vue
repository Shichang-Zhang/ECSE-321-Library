<template>
  <div>
    <b-form inline>
      <b-form-group
        label="Name:"
        label-for="eventSearchName">
        <b-form-input
          id="newEventNameInput"
          placeholder="Event Name"
          v-model="eventSearchName"
        ></b-form-input>
      </b-form-group>
      <b-button>Search</b-button>

    </b-form>
    <div class="table" style="overflow: auto;height: 51vh;">
      <b-table style="margin-top: 40px;" hover :items="eventDisplay"
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

    <div class="btns" style="text-align: right;border-top: #e7e7e7 1px solid;padding-top: 20px">
      <b-button variant="outline-primary" @click="handleCancel">Close</b-button>
      <b-button variant="outline-primary" @click="handleCreateStep1">Create New Event</b-button>
      <b-button variant="outline-primary" @click="handleUpdate">Update Event</b-button>
    </div>

    <b-modal id="createNewEventPanel" title="Create New Event"
             @ok="handleCreateStep2(newEventName,newEventStartTime,newEventStartDate,newEventEndTime,newEventEndDate)">
      <div>
        <b-form>
          <b-form-group
          label="Name"
          label-for="newEventNameInput">
              <b-form-input
                id="newEventNameInput"
                placeholder="Event Name"
                v-model="newEventName"
              ></b-form-input>
          </b-form-group>

          <b-form-group
            label="Start Date:"
            label-for="newEventStartDate"
          >
            <b-form-datepicker id="newEventStartDate" v-model="newEventStartDate" class="mb-2" locale="en"></b-form-datepicker>
          </b-form-group>

          <b-form-group
            label="Start Time:"
            label-for="newEventStartTime"
          >
            <b-form-timepicker id="newEventStartTime" v-model="newEventStartTime" class="mb-2" locale="en"></b-form-timepicker>
          </b-form-group>

          <b-form-group
            label="End Date:"
            label-for="newEventEndDate"
          >
            <b-form-datepicker id="newEventEndDate" v-model="newEventEndDate" class="mb-2" locale="en"></b-form-datepicker>
          </b-form-group>

          <b-form-group
            label="End Time:"
            label-for="newEventEndTime"
          >
            <b-form-timepicker id="newEventEndTime" v-model="newEventEndTime" class="mb-2" locale="en"></b-form-timepicker>
          </b-form-group>


        </b-form>
      </div>
    </b-modal>
  </div>
</template>

<script src="./LibrarianEvent_JS.js">

</script>

<style scoped>

</style>
