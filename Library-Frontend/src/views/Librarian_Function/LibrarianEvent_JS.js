import axios from 'axios'
var config = require('../../../config')

var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: {'Access-Control-Allow-Origin': frontendUrl}
})
export default {
  name: 'events',
  data() {
    return {
      events: [],
      //data used to create an event
      createEventInput: {
        name: '',
        startDate: '',
        startTime: '',
        endDate: '',
        endTime: ''
      },
      //data used to update an event
      updateEventInput: {
        id: '',
        startDate: '',
        startTime: '',
        endDate: '',
        endTime: ''
      },
      errorEvent: '',
      response: [],
    }
  },
  methods: {
    createEvent: function (name, startTime, startDate, endTime, endDate) {
      AXIOS.post('/events/createEvent?name=' + name + '&startDate=' + startDate + '&endDate=' + endDate + '&startTime=' + startTime + '&endTime=' + endTime)
        .then(response => {
          this.events.push(response.data)
        })
        .catch(e => {
          this.errorEvent = e
        })
    },
    updateEventName: function (eventId, eventName) {
      AXIOS.put('/events/updateEventName?id=' + eventId + '&name=' + eventName)
        .then(response => {
          this.events = response.data
          this.$emit('close')
        })
        .catch(e => {
          this.errorEvent = e
        })
    },
    updateEventTimeslot: function (id, eventstartTime, eventstartDate, eventendTime, eventendDate) {
      AXIOS.put('/events/updateEventTimeSlot?id=' + id + '&startDate=' + eventstartDate + '&endDate=' + eventendDate + '&startTime=' + eventstartTime + '&endTime=' + eventendTime)
        .then(response => {
          AXIOS.get('/events/eventList')
            .then(response => {
              this.events = response.data
            })
            .catch(e => {
              this.errorEvent = e
            })
        })
        .catch(e => {
          this.errorEvent = e
        })

      AXIOS.get('/events/eventList')
        .then(response => {
          this.events = response.data
        })
        .catch(e => {
          this.errorEvent = e
        })
    },
    deleteEvent: function (eventId) {
      AXIOS.delete('/events/deleteEvent?id=' + eventId)
        .then(response => {
          AXIOS.get('/events/eventList')
            .then(response => {
              this.events = response.data
            })
            .catch(e => {
              this.errorEvent = e
            })
        })
        .catch(e => {
          this.errorEvent = e
        })


    },
    handleCancel() {
      this.$emit('close');
    },
  },

  created: function () {
    AXIOS.get('/events/eventList')
      .then(response => {
        this.events = response.data
      })
      .catch(e => {
        this.errorEvent = e
      })

  }


}
