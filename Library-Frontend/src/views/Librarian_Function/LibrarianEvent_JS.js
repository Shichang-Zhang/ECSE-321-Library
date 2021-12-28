import axios from 'axios'

var config = require('../../../config')

var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: {'Access-Control-Allow-Origin': frontendUrl}
})

export default {
  name: "bookList",
  data() {
    return {
      //UI set up data
      perPage: 3,
      currentPage: 1,

      form: {
        isInLibrary: '',
        Name: '',
        ItemCategory: null,
        isReserved: null
      },
      ItemCategory: [{text: 'Select One', value: null}, 'Book', 'Movie', 'MusicAlbum', 'Newspaper', 'Archive'],
      items: [],
      //current user data
      currentLibrarianId: '',
      //event data
      eventList: [],
      eventDisplay: [],
      selectedEvent: [],
      //event search
      eventSearchName:'',
      //create event data
      newEventName:'',
      newEventStartDate:'',
      newEventStartTime:'',
      newEventEndDate:'',
      newEventEndTime:'',
      //error data
      error: '',
    }
  },
  computed: {
    rows() {
      return this.items.length
    }
  },
  methods: {
    //page setup methods
    linkGen(pageNum) {
      return pageNum === 1 ? '?' : `?page=${pageNum}`
    },
    onRowSelected(items) {
      this.selectedEvent = items;
    },
    handleCancel() {
      this.$emit('close');
    },
    handleCreateStep1(){
        this.$bvModal.show('createNewEventPanel');
    },
    handleCreateStep2(name, startTime, startDate, endTime, endDate){
      AXIOS.post('/events/createEvent?name=' + name + '&startDate=' + startDate + '&endDate=' + endDate + '&startTime=' + startTime + '&endTime=' + endTime)
        .then(response => {
          this.refreshEvent()
        })
        .catch(e => {
          this.errorEvent = e
        })


    },
    handleUpdate(){

    },
    /**
     * Refresh event in display table
     */
    refreshEvent(){
      this.eventDisplay = []
      AXIOS.get('/events/eventList')
        .then(response => {
          for (var index in response.data) {
            this.eventDisplay.push(
              {
                eventId: response.data[index].id,
                name: response.data[index].name,
                startDate: response.data[index].timeSlotDto.startDate,
                startTime: response.data[index].timeSlotDto.startTime,
                endDate: response.data[index].timeSlotDto.endDate,
                endTime: response.data[index].timeSlotDto.endTime,
                // eventRegistrationNumber:
              }
            )
          }
          this.eventList = this.eventDisplay
        })
        .catch(e => {
          this.errorItem = e
        })
    }
  },
  created: function () {
    this.currentLibrarianId = decodeURIComponent((new RegExp('[?|&]' + "id" + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [, ""])[1].replace(/\+/g, '%20')) || null
    this.refreshEvent()
  }
}
