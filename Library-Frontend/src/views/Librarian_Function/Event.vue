<template>
  <div class="Start">
    <div class="LibrarianEvents">
      <h2>Events</h2>
      <br>
      <!--      Tables display events in the library-->
      <table>
        <tr>
          <td>Event Id</td>
          &nbsp;
          <td>Event Name</td>
          &nbsp;
          <td>Start</td>
          &nbsp;
          <td>End</td>
        </tr>
        <tr v-for="event in events" :key=event.id>
          <td>{{ event.id }}</td>
          &nbsp;
          <td>{{ event.name }}</td>
          &nbsp;
          &nbsp;
          <td>{{ event.timeSlotDto.startDate.concat(" ").concat(event.timeSlotDto.startTime) }}</td>
          &nbsp;
          &nbsp;
          &nbsp;
          <td>{{ event.timeSlotDto.endDate.concat(" ").concat(event.timeSlotDto.endTime) }}</td>
          <td>
            <b-button variant="primary"
                      @click="deleteEvent(event.id)">
              Delete
            </b-button>
          </td>
        </tr>
        <tr>
        </tr>
      </table>
      <!--      Table contains the elements to create events-->
      <table>
        <td>
          <h2>Create Event</h2>
        </td>
        <tr>
          <td>
            <input type="text" v-model="createEventInput.name" placeholder="EventName">
          </td>
          <td>
            <b-form-datepicker size="sm" v-model="createEventInput.startDate" placeholder="StartDate"></b-form-datepicker>
          </td>
          <td>
            <b-form-timepicker size="sm" v-model="createEventInput.startTime" locale="en" :hour12="false"></b-form-timepicker>
          </td>

          <td>
            <b-form-datepicker size="sm" v-model="createEventInput.endDate" placeholder="EndDate"></b-form-datepicker>
          </td>
          <td>
            <b-form-timepicker size="sm" v-model="createEventInput.endTime" locale="en" :hour12="false"></b-form-timepicker>
          </td>
          <td>
            <b-button variant="outline-primary"
                      v-bind:disabled="!createEventInput.name||!createEventInput.startTime||!createEventInput.startDate||!createEventInput.endTime||!createEventInput.endDate"
                      @click="createEvent(createEventInput.name,createEventInput.startTime,createEventInput.startDate,createEventInput.endTime,createEventInput.endDate)">
              Create Event
            </b-button>
          </td>
        </tr>

      </table>
      <!--      Table contains the elements to update events-->
      <table>
        <td>
          <h2>Update Event</h2>
          <tr>
            <td>
              <input type="text" v-model="updateEventInput.id" placeholder="Eventid">
            </td>
            <td>
              <b-form-datepicker size="sm" v-model="updateEventInput.startDate" placeholder="StartDate"></b-form-datepicker>
            </td>
            <td>
              <b-form-timepicker size="sm" v-model="updateEventInput.startTime" locale="en" :hour12="false"></b-form-timepicker>
            </td>

            <td>
              <b-form-datepicker size="sm" v-model="updateEventInput.endDate" placeholder="EndDate"></b-form-datepicker>
            </td>
            <td>
              <b-form-timepicker size="sm" v-model="updateEventInput.endTime" locale="en" :hour12="false"></b-form-timepicker>
            </td>
            <td>
              <b-button variant="outline-primary"
                        v-bind:disabled="!updateEventInput.id||!updateEventInput.startTime||!updateEventInput.startDate||!updateEventInput.endTime||!updateEventInput.endDate"
                        @click="updateEventTimeslot(updateEventInput.id,updateEventInput.startTime,updateEventInput.startDate,updateEventInput.endTime,updateEventInput.endDate)">
                Update Event Time
              </b-button>
            </td>
          </tr>
          <tr></tr>
          <tr></tr>
        </td>
      </table>
      <br>

      <b-button variant="outline-primary" @click="handleCancel">Close</b-button>
      <br>
      <span v-if="errorEvent" style="color:red">{{ errorEvent }} </span>
    </div>
  </div>
</template>
<script src="./LibrarianEvent_JS.js">
</script>
<style>
.Start {
  background-image: linear-gradient(90deg, rgba(180, 180, 180, 0.15) 10%, rgba(0, 0, 0, 0) 10%),
  linear-gradient(rgba(180, 180, 180, 0.15) 10%, rgba(0, 0, 0, 0) 10%);
  background-size: 10px 10px;
  height: 100vh;
}
</style>
