import axios from 'axios'
import {getCurrentTime} from '../librarian-side/CurrentLibrarianTime'
var config = require('../../../config')

var frontendUrl = 'https://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'https://' + config.dev.backendHost + ':' + config.dev.backendPort

var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
})
 export default{
  name: 'events',
  data () {
    return {
	  events: [],
	    input1: {
        name: '',
		startDate:'',
		startTime:'',
		endDate:'',
		endTime:''
	  },
	  	input2: {
		id:'',
        name: '',
	  },
	  	input3: {
        id: '',
		startDate:'',
		startTime:'',
		endDate:'',
		endTime:''
	  },
	  	input4: {
        id: '',
      },
      errorEvent: '',
	    response: [],
	  ItemCategory: [{ text: 'Select One', value: null }, 'All', 'Book', 'Movie', 'MusicAlbum', 'Newspaper', 'Archive'],
    }
  },
methods:{
  createEvent: function (name,startTime,startDate,endTime,endDate) {
      AXIOS.post('/events/createEvent?name='+name+'&startDate='+startDate+'&endDate='+endDate+'&startTime='+startTime+'&endTime='+endTime)
        .then(response => {
          this.events.push(response.data)
        })
        .catch(e => {
          this.errorEvent= e
        })
  },
    updateEventName: function (eventId,eventName) {
      AXIOS.put('/events/updateEventName?id='+eventId+'&name='+eventName)
        .then(response => {
		  this.events = response.data
		  this.$emit('close')
        })
        .catch(e => {
          this.errorEvent= e
        })
  },
    updateEventTimeslot: function (id,eventstartTime,eventstartDate,eventendTime,eventendDate) {
      AXIOS.put('/events/updateEventTimeSlot?id='+id+'&startDate='+eventstartDate+'&endDate='+eventendDate+'&startTime='+eventstartTime+'&endTime='+eventendTime)
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
          this.errorEvent= e
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
      AXIOS.delete('/events/deleteEvent?id='+eventId)
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
          this.errorEvent= e
        })


  },
  handleCancel(){
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
