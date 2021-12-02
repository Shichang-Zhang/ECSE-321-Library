
<template>
  <div>
		<div class="LibrarianEvents">
		 <h2>Events</h2>
		 <br>
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
		    <tr v-for="event in events" :key=event.id><td>{{event.id}}</td>
          &nbsp;
      <td>{{ event.name}}</td>
          &nbsp;
          &nbsp;
      <td>{{ event.timeSlotDto.startDate.concat(" ").concat(event.timeSlotDto.startTime)}}</td>
          &nbsp;
          &nbsp;
          &nbsp;
      <td>{{ event.timeSlotDto.endDate.concat(" ").concat(event.timeSlotDto.endTime)}}</td>
          <td>
            <b-button variant="primary"
                      @click="deleteEvent(event.id)" >
              Delete
            </b-button>
          </td>
           </tr>
       <tr>
       </tr>
		   </table>
		   <table>
         <td>
           <h2>Create Event</h2>
         </td>
			<tr>
			<td>
			  <input type="text" v-model="input1.name" placeholder="EventName">
      </td>
        <td>
          <b-form-datepicker size="sm" v-model="input1.startDate" placeholder="StartDate"></b-form-datepicker>
        </td>
			<td>
        <b-form-timepicker size="sm" v-model="input1.startTime" locale="en" :hour12="false" ></b-form-timepicker>
			</td>

        <td>
          <b-form-datepicker size="sm" v-model="input1.endDate" placeholder="EndDate"></b-form-datepicker>
        </td>
			<td>
        <b-form-timepicker size="sm" v-model="input1.endTime" locale="en" :hour12="false" ></b-form-timepicker>
			</td>
        <td>
          <b-button variant="outline-primary" v-bind:disabled="!input1.name||!input1.startTime||!input1.startDate||!input1.endTime||!input1.endDate"
            			   @click="createEvent(input1.name,input1.startTime,input1.startDate,input1.endTime,input1.endDate)" >Create Event
                            </b-button>
        </td>
        </tr>

<!--			&nbsp;-->


<!--			&nbsp;-->
<!--			<td>-->
<!--		       <b-button variant="outline-primary" v-bind:disabled="!input1.name||!input1.startTime||!input1.startDate||!input1.endTime||!input1.endDate"-->
<!--			   @click="createEvent(input1.name,input1.startTime,input1.startDate,input1.endTime,input1.endDate)" >Create Event-->
<!--                </b-button>-->
<!--			</td>-->
<!--			&nbsp;-->
<!--			</tr>-->
<!--			<h2 style="color:white">Update Event Name</h2>-->
<!--			<tr>-->
<!--			<td>-->
<!--			  <input type="text" v-model="input2.id" placeholder="EventId">-->
<!--		    </td>-->
<!--		    &nbsp;-->
<!--			<td>-->
<!--			  <input type="text" v-model="input2.name" placeholder="Event Name">-->
<!--			</td>-->
<!--			&nbsp;-->
<!--			<td>-->
<!--		       <b-button variant="outline-primary" v-bind:disabled="!input2.name||!input2.id"-->
<!--			   @click="updateEventName(input2.id,input2.name)" >-->
<!--                Update Event Name-->
<!--                </b-button>-->
<!--				</td>-->
<!--				&nbsp;-->
<!--			</tr>-->
<!--		<h2 style="color:white">Update Event Tiemslot</h2>-->
<!--            <tr>-->
<!--		    <td style="color:white">EventId</td>-->
<!--			&nbsp;-->
<!--		    <td style="color:white">StartTime</td>-->
<!--			&nbsp;-->
<!--			<td style="color:white">EndTime</td>-->
<!--			&nbsp;-->
<!--			<td style="color:white">StartDate</td>-->
<!--			&nbsp;-->
<!--			<td style="color:white">EndDate</td>-->
<!--			&nbsp;-->
<!--		</tr>-->
<!--			<tr>-->
<!--			<td>-->
<!--			  <input type="text" v-model="input3.id" placeholder="Eventid">-->
<!--		    </td>-->
<!--		    &nbsp;-->
<!--			<td>-->
<!--			  <b-time v-model="input3.startTime" placeholder="StratTime(hh:mm)"></b-time>-->
<!--			</td>-->
<!--			&nbsp;-->
<!--			<td>-->
<!--			  <b-time v-model="input3.endTime" placeholder="EndTime(hh:mm)"></b-time>-->
<!--			</td>-->
<!--			&nbsp;-->
<!--			<td>-->
<!--			  <b-form-datepicker v-model="input3.startDate" placeholder="StratDate(yyyy-mm-dd)"></b-form-datepicker>-->
<!--			</td>-->
<!--			&nbsp;-->
<!--			<td>-->
<!--			  <b-form-datepicker v-model="input3.endDate" placeholder="EndDate(yyyy-mm-dd)"></b-form-datepicker>-->
<!--			</td>-->
<!--			&nbsp;-->
<!--			<td>-->
<!--		       <b-button variant="outline-primary" v-bind:disabled="!input3.id||!input3.startTime||!input3.startDate||!input3.endTime||!input3.endDate"-->
<!--			   @click="updateEventTimeslot(input3.id,input3.startTime,input3.startDate,input3.endTime,input3.endDate)" >Update Event Time-->
<!--                </b-button>-->
<!--			</td>-->
<!--			&nbsp;-->
<!--			</tr>-->
<!--			<h2 style="color:white">Delete Event</h2>-->
<!--			<tr>-->
<!--			<td>-->
<!--			  <input type="text" v-model="input4.id" placeholder="EventId">-->
<!--		    </td>-->
<!--		    &nbsp;-->
<!--			<td>-->
<!--		       <b-button variant="outline-primary" v-bind:disabled="!input4.id"-->
<!--			   @click="deleteEvent(input4.id)" >-->
<!--                Delete Event-->
<!--                </b-button>-->
<!--				</td>-->
<!--				&nbsp;-->
<!--			</tr>-->
			</table>
      <table>
        <td>
          <h2>Update Event</h2>
          <tr>
            <td>
              <input type="text" v-model="input3.id" placeholder="Eventid">
            </td>
            <td>
              <b-form-datepicker size="sm" v-model="input3.startDate" placeholder="StartDate"></b-form-datepicker>
            </td>
            <td>
              <b-form-timepicker size="sm" v-model="input3.startTime" locale="en" :hour12="false" ></b-form-timepicker>
            </td>

            <td>
              <b-form-datepicker size="sm" v-model="input3.endDate" placeholder="EndDate"></b-form-datepicker>
            </td>
            <td>
              <b-form-timepicker size="sm" v-model="input3.endTime" locale="en" :hour12="false" ></b-form-timepicker>
            </td>
            <td>
              <b-button variant="outline-primary" v-bind:disabled="!input3.id||!input3.startTime||!input3.startDate||!input3.endTime||!input3.endDate"
                        @click="updateEventTimeslot(input3.id,input3.startTime,input3.startDate,input3.endTime,input3.endDate)" >Update Event Time
              </b-button>
            </td></tr>
          <tr></tr>
          <tr></tr>
        </td>
      </table>
			<br>

         <b-button variant="outline-primary" @click="handleCancel">Close</b-button>
		  <br>
		  <span v-if="errorEvent" style="color:red">{{errorEvent}} </span>
  </div>
 </div>
</template>
<script  src="./librarianEvent.js">
</script>
<style>
.Start{
}
.Event {
  font-size: 32px;
  font-weight: bold;
  display: flex;
  align-items: center;
  box-shadow: 0 0 50px rgba(0, 0, 0, 0.3);
  letter-spacing: 1rem;
  padding: 20px;
  background: #fff;
}
#LibrarianEvents{
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    color: #2c3e50;
    background: #f2ece8;
  }
</style>
