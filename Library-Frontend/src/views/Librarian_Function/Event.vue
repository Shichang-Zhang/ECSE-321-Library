<template>
  <div>
    <b-form inline style="margin: 15px 0">
      <b-form-group
        label-for="eventSearchName">
        <b-form-input
          id="newEventNameInput"
          placeholder="Event Name"
          v-model="eventSearchName"
        ></b-form-input>
        <b-button variant="outline-primary" @click="findEventsByName(eventSearchName)">Search</b-button>
        <b-button variant="outline-primary" @click="refreshEvent()">Show All</b-button>
      </b-form-group>
    </b-form>
    <div class="table" style="overflow: auto;height: 49vh;">
      <b-table hover :items="eventDisplay"
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
      <b-button variant="primary" @click="handleCreateStep1">Create New Event</b-button>
      <b-button variant="primary" @click="handleUpdateNameStep1(selectedEvent)">Update Name</b-button>
      <b-button variant="primary" @click="handleUpdateTimeSlotStep1(selectedEvent)">Update TimeSlot</b-button>
      <b-button variant="primary" @click="viewEventRegistrationStep1(selectedEvent)">View attendee</b-button>
      <b-button variant="primary" @click="deleteEvent(selectedEvent)">Delete</b-button>
      <b-button variant="outline-primary" @click="handleCancel">Close</b-button>
    </div>
<!--Setting of panel of creating a new event-->
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
<!--Setting of panel of updating the name of an event-->
    <b-modal id="updateNamePanel" title="Update Event Name"
             @ok="handleUpdateNameStep2(selectedEvent,newEventName)">
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
        </b-form>
      </div>
    </b-modal>
<!--    Setting of panel of updating the time slot of an event-->
    <b-modal id="updateTimePanel" title="Update Event TimeSlot"
             @ok="handleUpdateTimeSlotStep2(selectedEvent,newEventStartDate,newEventStartTime,newEventEndDate,newEventEndTime)">
      <div>
        <b-form>

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
    <!--    Setting of panel of viewing event registration of an event-->
    <b-modal id="viewAttendees" title="View Attendees" size="xl">
      <div>Number of attendees: {{numberOfAttendee}}</div>
      <div>
            <b-table style="margin-top: 40px;" hover :items="eventRegistrationDisplay"
                     @row-selected="onRowSelected"
                     select-mode="single"
                     selectable>
            </b-table>
      </div>
    </b-modal>
  </div>
</template>

<script src="./LibrarianEvent_JS.js">

</script>

<style scoped>

</style>
