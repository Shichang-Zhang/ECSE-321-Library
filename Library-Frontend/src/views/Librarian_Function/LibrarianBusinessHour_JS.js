import axios from 'axios'

var config = require('../../../config')

var frontendUrl = 'https://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'https://' + config.dev.backendHost + ':' + config.dev.backendPort

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
      //Option of selecting day of week (Monday - Sunday) when creating business hour
      dayOfWeekOptions:[{text:'Monday',value:1},{text:'Tuesday',value:2},{text:'Wednesday',value:3},{text:'Thursday',value:4},{text:'Friday',value:5},{text:'Saturday',value:6},{text:'Sunday',value:7}]
    }
  },
  computed: {
    rows() {
      return this.businessHours.length
    }
  },
  methods: {
    onRowSelected(businessHours) {
      this.selectedBusinessHours = businessHours
    },
    handleCancel() {
      this.$emit('close');
    },
    /**
     * Refresh business hours in the display table
     */
    refreshBusinessHour(){
      this.businessHours=[]
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
    },
    toastMessage(content){
      this.$bvToast.toast(content, {
        title: 'Tips',
        autoHideDelay: 2000,
        variant: 'warning',
        solid: true,
        appendToast: false
      });
    },
    /**
     * Step 1 of creating business hour:
     * Open the panel of selecting day of week, start time and end time
     */
    handleCreateBusinessHourStep1(){
      this.$bvModal.show('createNewBusinessHour');
    },
    /**
     * Step 2 of creating business hour:
     * In the panel, selecting day of week, start time and end time
     * Click OK to create
     * @param dayOfWeek day of week of the business hour
     * @param startTime start time of the business hour
     * @param endTime end time of the business hour
     */
    handleCreateBusinessHourStep2(dayOfWeek, startTime, endTime){
      console.log(this.businessHourDayOfWeek)
      const form_data = new FormData()
      form_data.append('dayOfWeek', parseInt(dayOfWeek))
      form_data.append('startTime', startTime)
      form_data.append('endTime', endTime)
      AXIOS.post('/businessHours/createBusinessHour', form_data, {})
        .then(response => {
          this.refreshBusinessHour()
          this.toastMessage("Create Successfully")
        })
        .catch(e => {
          if(startTime>endTime){
            this.toastMessage("Start time cannot be latter than end time!")
          }else{
            this.toastMessage("Business hour of this day of week already exists!")
          }

        })
    },
    /**
     * Step 1 of updating business hour:
     * Open the panel of updating business hour
     * @param selectedBusinessHours the business hour we want to update
     */
    handleUpdateBusinessHourStep1(selectedBusinessHours){
      if(selectedBusinessHours.length>0){
        this.$bvModal.show('updateBusinessHour');
      }else{
        this.toastMessage("No Selected Business Hour")
      }
    },
    /**
     * Step 2 of updating business hour:
     * In the panel, selecting start time and end time
     * Click OK to update
     * @param selectedBusinessHours the business hour we want to update
     * @param businessHourStartTime new start time the business hour updates to
     * @param businessHourEndTime new end time the business hour updates to
     */
    handleUpdateBusinessHourStep2(selectedBusinessHours,businessHourStartTime,businessHourEndTime){
      AXIOS.put('/businessHours/updateBusinessHourTime?dayOfWeek=' + (selectedBusinessHours[0].id) + '&startTime=' + businessHourStartTime + '&endTime=' + businessHourEndTime)
        .then(response => {
          this.refreshBusinessHour()
          this.toastMessage("Update Successfully")
        })
        .catch(e => {
          if(businessHourStartTime>businessHourEndTime){
            this.toastMessage("Start time cannot be latter than end time!")
          }else{
            this.toastMessage("Fail to update")
          }
        })

    },
  },
  created: function () {
    this.refreshBusinessHour()
  }
}
