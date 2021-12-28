<style>

</style>

<script src="./Event.js">

</script>

<template>
  <!--  This is a page ONLY for developing purpose-->
  <div>
    <h3>Initialization</h3>
    <button @click="initialization" type="reset">Initialize</button>
    <h3>Current User</h3>
    <h3>{{ currentUserName.concat("   ").concat(currentUserId) }}</h3>
    <input type="text" v-model="loginUsername" placeholder="username">
    <input type="text" v-model="loginPassword" placeholder="password">
    <button @click="logIn(loginUsername,loginPassword)" type="reset">Login</button>


    <h2>User List</h2>
    <table>
      <tr>
        <th>id</th>
        <th>name</th>
        <th>address</th>
        <th>online account</th>
        <th>local</th>
      </tr>
      <tr v-for="user in userList">
        <td>{{ user.id }}</td>
        <td>{{ user.name }}</td>
        <td>{{ user.address }}</td>
        <td>{{ user.onlineAccountDto == null ? 'null' : user.onlineAccountDto.id }}</td>
        <td>{{ user.local.toString() }}</td>
      </tr>

    </table>
    <h2>Sign Up</h2>
    <table>
      <tr>
        <td>User Name</td>
        <td>Address</td>
        <td>isLocal</td>
        <td>Want an online account?</td>
        <td>Online Username</td>
        <td>Online Password</td>
        <td>Email</td>
      </tr>
      <tr>
        <td>
          <input type="text" v-model="userName" placeholder="real name">
        </td>
        <td>
          <input type="text" v-model="userAddress" placeholder="address">
        </td>
        <td>
          Local<input type="radio" v-model="userLocal" name="isLocal" value="true" checked="checked"> Not Local<input
          type="radio" name="notLocal" v-model="userLocal" value="false">
        </td>
        <td>
          Yes<input type="radio" v-model="wantOnlineAccount" name="Online" value="true" checked="checked"> No<input
          type="radio" name="offline" v-model="wantOnlineAccount" value="false">
        </td>
        <td>
          <input type="text" v-model="onlineAccountUsername" placeholder="username">
        </td>
        <td>
          <input type="text" v-model="onlineAccountPassword" placeholder="password">
        </td>
        <td>
          <input type="text" v-model="onlineAccountEmail" placeholder="email">
        </td>
        <td>
          <button v-bind:disabled="!userName||!userAddress"
                  @click="signUp(userName,userAddress,userLocal,wantOnlineAccount,onlineAccountUsername,onlineAccountPassword,onlineAccountEmail)"
                  type="reset">Create
          </button>
        </td>
      </tr>
    </table>
    <span v-if="errorUser" style="color:red">Error: {{ errorUser }} </span>

    <h2>Event List</h2>
    <table>
      <tr>
        <th>id</th>
        <th>Event Name</th>
        <th>Start</th>
        <th>End</th>
      </tr>
      <tr v-for="event in eventList">
        <td>{{ event.id }}</td>
        <td>{{ event.name }}</td>
        <td>{{ event.timeSlotDto.startDate.concat(" ").concat(event.timeSlotDto.startTime) }}</td>
        <td>{{ event.timeSlotDto.endDate.concat(" ").concat(event.timeSlotDto.endTime) }}</td>
      </tr>
      <tr>
        <td>
          <input type="text" v-model="eventName" placeholder="eventName">
        </td>
        <td>
          <input type="text" v-model="eventStartDate" placeholder="startDate">
        </td>
        <td>
          <input type="text" v-model="eventStartTime" placeholder="startTime">
        </td>
        <td>
          <input type="text" v-model="eventEndDate" placeholder="endDate">
        </td>
        <td>
          <input type="text" v-model="eventEndTime" placeholder="endTime">
        </td>
        <td>
          <button v-bind:disabled="!eventName||!eventStartDate||!eventEndDate"
                  @click="createNewEvent(eventName,eventStartDate,eventStartTime,eventEndDate,eventEndTime)"
                  type="reset">Create
          </button>
        </td>
        <td>
          <button v-bind:disabled="!eventName||!eventStartDate||!eventEndDate"
                  @click="registerEvent(currentUserId,eventId)" type="reset">register
          </button>
        </td>
        <td>
          <button v-bind:disabled="!eventName||!eventStartDate||!eventEndDate"
                  @click="unregisterEvent(currentUserId,eventId)" type="reset">unregister
          </button>
        </td>
      </tr>

    </table>

    <h2>Event Registration List</h2>
    <table>
      <tr>
        <th>EventRegistration Id</th>
        <th>User</th>
        <th>Event</th>
        <th>Start</th>
        <th>End</th>
      </tr>
      <tr v-for="eventRegistration in eventRegistrationList">
        <td>{{ eventRegistration.id }}</td>
        <td>{{ eventRegistration.userDto.name }}</td>
        <td>{{ eventRegistration.eventDto.name }}</td>
        <td>
          {{ eventRegistration.eventDto.timeSlotDto.startDate.concat(" ").concat(eventRegistration.eventDto.timeSlotDto.startTime) }}
        </td>
        <td>
          {{ eventRegistration.eventDto.timeSlotDto.endDate.concat(" ").concat(eventRegistration.eventDto.timeSlotDto.endTime) }}
        </td>
      </tr>
    </table>

  </div>

</template>

