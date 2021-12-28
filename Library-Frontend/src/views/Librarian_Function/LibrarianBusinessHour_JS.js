import axios from 'axios'
import {getCurrentTime} from "../User_Function/CurrentUserData";

var config = require('../../../config')

var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: {'Access-Control-Allow-Origin': frontendUrl}
})

export default {
  name: 'BusinessHours',
  data() {
    return {
      //UI set up data
      perPage: 3,
      currentPage: 1,
      businessHours: [],//store displayed business hours in the table
      errorBusinessHour: '',
      response: [],
      selectedBusinessHours: [], //selected business hour
      businessHourDayOfWeek: '',
      businessHourStartTime: '',
      businessHourEndTime: '',
    }
  },
  computed: {
    rows() {
      return this.businessHours.length
    }
  },
  methods: {
    /**
     * Show all business hour
     */
    showAllBusinessHour: function () {
      AXIOS.get('/businessHours/businessHourList')
        .then(response => {
          this.businessHours = response.data
        })
        .catch(e => {
          this.errorBusinessHour = e
        })
    },
    /**
     * Create a new business hour
     * @param dayOfWeek  day of week of the business hour
     * @param startTime  start time
     * @param endTime  end time
     */
    createBusinessHour: function (dayOfWeek, startTime, endTime) {
      const form_data = new FormData()
      form_data.append('dayOfWeek', parseInt(dayOfWeek))
      form_data.append('startTime', startTime)
      form_data.append('endTime', endTime)
      AXIOS.post('/businessHours/createBusinessHour', form_data, {})
        .then(response => {
          this.businessHours.push(response.data)
        })
        .catch(e => {
          this.errorBusinessHour = e
        })
    },
    /**
     * Update timeslot of a business hour of a day of week
     * @param dayOfWeek  day of week of the business hour
     * @param startTime new start time
     * @param endTime new end time
     */
    updateBusinessHour: function (dayOfWeek, startTime, endTime) {
      AXIOS.put('/businessHours/updateBusinessHourTime?dayOfWeek=' + (this.selectedBusinessHours[0].id) + '&startTime=' + startTime + '&endTime=' + endTime)
        .then(response => {
          this.businessHours = []
          //refresh the content in the table
          AXIOS.get('/businessHours/businessHourList')
            .then(response => {
              for (var index in response.data) {
                this.businessHours.push(
                  {
                    id: response.data[index].id,
                    dayOfWeek: response.data[index].dayOfWeek.toString(),
                    startTime: response.data[index].startTime,
                    endTime: response.data[index].endTime,
                  }
                )
              }
            })
            .catch(e => {
              this.errorBusinessHour = e
            })
        })
        .catch(e => {
          this.errorBusinessHour = e
        })
    },
    onRowSelected(businessHours) {
      this.selectedBusinessHours = businessHours
    },
    handleCancel() {
      this.$emit('close');
    }
  },
  created: function () {
    AXIOS.get('/businessHours/businessHourList')
      .then(response => {
        for (var index in response.data) {
          this.businessHours.push(
            {
              id: response.data[index].id,
              dayOfWeek: response.data[index].dayOfWeek.toString(),
              startTime: response.data[index].startTime,
              endTime: response.data[index].endTime,
            }
          )
        }
      })
      .catch(e => {
        this.errorBusinessHour = e
      })
  }
}
