import axios from 'axios'

var config = require('../../../config')

var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: {'Access-Control-Allow-Origin': frontendUrl}
})
//  This is a page ONLY for developing purpose
export default {
  name: 'test',
  data() {
    return {
      initStatus: '',
      response: [],
      itemList: [],
      itemId: '',
      itemName: '',
      itemItemCategory: '',
      itemInLibrary: '',
      newItem: {
        id: '',
        name: '',
        itemCategory: '',
        inLibrary: '',
      },
      errorItem: '',
      deleteItemId: '',

      userList: [],
      userId: '',
      userName: '',
      userAddress: '',
      userOnlineAccount: '',
      userLocal: '',
      newUser: {
        id: '',
        name: '',
        address: '',
        onlineAccount: '',
        local: ''
      },
      errorUser: '',
      currentUserId: '',
      currentUserName: '',
      loginUsername: '',
      loginPassword: '',
      checkoutTime: '00:00:00',
      checkoutStartDate: '2030-12-12',
      checkoutEndDate: '2030-12-29',


      eventList: [],
      eventName: '',
      eventId: '',
      eventStartTime: '10:00:00',
      eventEndTime: '23:00:00',
      eventStartDate: '2029-11-25',
      eventEndDate: '2029-11-25',
      errorEvent: '',

      onlineAccountList: [],
      onlineAccountId: '',
      onlineAccountUsername: '',
      onlineAccountPassword: '',
      onlineAccountEmail: '',
      wantOnlineAccount: '',

      itemReservationList: [],
      errorItemReservation: ''

    }
  },
  created: function () {
    AXIOS.get('/items/itemList')
      .then(response => {
        this.itemList = response.data
      })
      .catch(e => {
        this.errorItem = e
      })

    AXIOS.get('/itemReservations/getItemReservationList')
      .then(response => {
        this.itemReservationList = response.data
      })
      .catch(e => {
        this.errorItem = e
      })

    AXIOS.get('/users/userList')
      .then(response => {
        this.userList = response.data
      })
      .catch(e => {
        this.errorUser = e
      })

    AXIOS.get('/events/eventList')
      .then(response => {
        this.eventList = response.data
      })
      .catch(e => {
        this.errorEvent = e
      })
  },
  methods: {

    findItems: function (name, itemCategory) {
      console.log(name)
      console.log(itemCategory)
      let nameAndCategory = {
        name: name,
        itemCategory: itemCategory
      }
      let category = {
        itemCategory: itemCategory
      }
      if (name) {
        AXIOS.get('items/findItem', {params: nameAndCategory})
          .then(response => {
            this.itemList = response.data
          })
          .catch(e => {
            this.errorItem = e
          })
      } else {
        AXIOS.get('items/findItemByItemCategory', {params: category})
          .then(response => {
            this.itemList = response.data
          })
          .catch(e => {
            this.errorItem = e
          })
      }

    },
    createNewItem: function (name, itemCategory) {
      console.log('111111')
      const form_data = new FormData()
      form_data.append('name', name)
      form_data.append('itemCategory', itemCategory)
      AXIOS.post('/items/createItem/', form_data, {})
        .then(response => {
          // JSON responses are automatically parsed.
          this.itemList.push(response.data)
          this.errorItem = ''
          this.newItem = ''
        })
        .catch(e => {
          this.errorItem = e
        })
    },
    deleteItem: function (id) {
      let item = {
        id: id
      }
      AXIOS.delete('/items/deleteItem/', {params: item})
        .then(response => {
          // JSON responses are automatically parsed.
          this.errorItem = ''
          window.location.reload()
        })
        .catch(e => {
          this.errorItem = e
        })
    },
    signUp: function (name, address, isLocal, isOnline, username, password, email) {
      const form_data = new FormData()
      form_data.append('name', address)
      form_data.append('address', name)
      form_data.append('isLocal', isLocal)
      AXIOS.post('/users/createUser/', form_data, {})
        .then(response => {

          this.userId = (response.data.id)
          if (isOnline === 'true') {
            const form_data2 = new FormData()
            form_data2.append('uid', parseInt(this.userId))
            form_data2.append('username', username)
            form_data2.append('password', password)
            form_data2.append('email', email)
            AXIOS.put('/users/updateOnlineAccount/', form_data2, {})
              .then(response => {
                // JSON responses are automatically parsed.
                this.onlineAccountList.push(response.data)
                this.userList.push(response.data)
                this.errorUser = ''
              })
              .catch(e => {
                this.errorUser = e
              })
          } else {
            this.userList.push(response.data)
          }
          this.errorUser = ''
          this.newUser = ''
        })
        .catch(e => {
          this.errorUser = e
        })
    },
    createNewEvent: function (name, startDate, startTime, endDate, endTime) {
      const form_data = new FormData()
      form_data.append('name', name)
      form_data.append('startDate', startDate)
      form_data.append('endDate', endDate)
      form_data.append('startTime', startTime)
      form_data.append('endTime', endTime)
      AXIOS.post('/events/createEvent', form_data, {})
        .then(response => {
          this.eventList.push(response.data)
          this.errorEvent = ''
        })
        .catch(e => {
          this.errorUser = e
        })
    },
    initialization: function () {
      AXIOS.get('/init/', {})
        .catch(e => {
          this.errorUser = e
        })
    },
    logIn: function (username, userPassword) {
      const form_data = new FormData()
      form_data.append('username', username)
      form_data.append('password', userPassword)
      AXIOS.post('/users/login', form_data, {})
        .then(response => {
          this.currentUserId = response.data.id
          this.currentUserName = response.data.name
        })
        .catch(e => {
          this.errorUser = e
        })
    },
    checkout: function (pid, itemId, startDate, startTime) {
      const form_data = new FormData()
      form_data.append('pid', pid)
      form_data.append('itemId', itemId)
      form_data.append('startDate', startDate)
      form_data.append('startTime', startTime)
      AXIOS.post('/itemReservations/checkout', form_data, {})
        .catch(e => {
          this.errorUser = e
        })
    },
    reserve: function (pid, itemId, startDate, startTime, endDate, endTime) {
      const form_data = new FormData()
      form_data.append('pid', parseInt(pid))
      form_data.append('itemId', parseInt(itemId))
      form_data.append('startDate', startDate)
      form_data.append('startTime', startTime)
      form_data.append('endDate', endDate)
      form_data.append('endTime', endTime)
      AXIOS.post('/itemReservations/checkout', form_data, {})
        .catch(e => {
          this.errorUser = e
        })
    },


  }

}
