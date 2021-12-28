<style>

</style>

<script src="./books.js">

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
    <h2>Item List</h2>
    <table>
      <tr>
        <th>id</th>
        <th>name</th>
        <th>itemCategory</th>
        <th>isInLibrary</th>
      </tr>
      <tr v-for="item in itemList">
        <td>{{ item.id }}</td>
        <td>{{ item.name }}</td>
        <td>{{ item.itemCategory }}</td>
        <td>{{ item.inLibrary.toString() }}</td>
        <td>
          <button @click="deleteItem(item.id)" type="reset">Delete</button>
        <td>
          <button @click="checkout(currentUserId,item.id,checkoutStartDate,checkoutTime)" type="reset">CheckOut</button>
        </td>

      </tr>
      <tr>
        <td>
          <input type="text" v-model="checkoutStartDate" placeholder="StartDate">
        </td>
      </tr>
      <tr>
        <td>
          <input type="text" v-model="itemName" placeholder="name">
        </td>
        <td>
          <input type="text" v-model="itemItemCategory" placeholder="itemCategory">
        </td>
        <td>
          <button v-bind:disabled="!itemName||!itemItemCategory" @click="createNewItem(itemName,itemItemCategory)"
                  type="reset">Create
          </button>
        </td>
        <td>
          <button v-bind:disabled="!itemItemCategory" @click="findItems(itemName,itemItemCategory)" type="reset">
            Search
          </button>
        </td>
      </tr>
    </table>
    <span v-if="errorItem" style="color:red">Error: {{ errorItem }} </span>
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
      </tr>

    </table>
    <h2>Item Reservation List</h2>
    <table>
      <tr>
        <th>Reservation Id</th>
        <th>User</th>
        <th>Book</th>
        <th>itemCategory</th>
        <th>Start</th>
        <th>End</th>
      </tr>
      <tr v-for="itemReservation in itemReservationList">
        <td>{{ itemReservation.id }}</td>
        <td>{{ itemReservation.personDto.name }}</td>
        <td>{{ itemReservation.itemDto.name }}</td>
        <td>{{ itemReservation.itemDto.itemCategory }}</td>
        <td>{{ itemReservation.timeSlotDto.startDate.concat(" ").concat(itemReservation.timeSlotDto.startTime) }}</td>
        <td>{{ itemReservation.timeSlotDto.endDate.concat(" ").concat(itemReservation.timeSlotDto.endTime) }}</td>
      </tr>
    </table>


  </div>

</template>

