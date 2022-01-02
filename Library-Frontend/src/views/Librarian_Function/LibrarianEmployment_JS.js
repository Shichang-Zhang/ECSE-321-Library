import axios from 'axios'
import currentLibrarianData from '../Librarian_Login/LibrarianMenu'

var config = require('../../../config')

var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: {'Access-Control-Allow-Origin': frontendUrl}
})

export default {
  name: 'Librarians',
  data() {
    return {
      //UI set up data
      perPage: 3,
      currentPage: 1,
      librarians: [],
      errorLibrarian: '',
      response: [],
      librarianList: [],
      selectedLibrarians: [],
      librarianId: '',
      librarianName: '',
      librarianAddress: '',
      librarianBusinessHour: '',
      librarianSearchName:'',
      //current head librarian data
      currentLibrarianId:'',
      //Option of selecting day of week (Monday - Sunday) when assignning business hour
      dayOfWeekOptions:[{text:'Monday',value:1},{text:'Tuesday',value:2},{text:'Wednesday',value:3},{text:'Thursday',value:4},{text:'Friday',value:5},{text:'Saturday',value:6},{text:'Sunday',value:7}]
    }
  },
  computed: {
    rows() {
      return this.librarians.length
    }
  },
  methods: {
    //open event
    gotoEvent() {
      this.$router.push('/librarianEvent');
    },

    gotoLibrarian() {
      this.$router.push('/librarianItem');
    },
    gotoStart() {
      this.$router.push('/librarianMenu');
    },
    gotoHome(){
      this.$router.push('/');
    },
    /**
     * Find a librarian by id
     * @param id id of the librarian
     */
    findLibrarian: function (id) {
      AXIOS.get('/librarians/getLibrarianById?id=' + id)//, {params:id})
        .then(response => {
          this.librarians = response.data
        })
        .catch(e => {
          this.errorLibrarian = e
        })
    },
    /**
     * Show all librarians in the table
     */
    showAllLibrarian: function () {
      AXIOS.get('/librarians/librarianList')
        .then(response => {
          this.librarians = response.data
        })
        .catch(e => {
          this.errorLibrarian = e
        })
    },
    /**
     * Unassign a business hour to a librarian
     * @param id id of the librarian
     * @param dayOfWeek day of week unassigned from the librarian
     */
    unassignBusinessHour: function (id, dayOfWeek) {
      AXIOS.put('/librarians/unassignBusinessHour?headLibrarianId=' + currentLibrarianData.currentLibrarianId + '&librarianId=' + id + '&dayOfWeek=' + dayOfWeek)
        .then(response => {
          this.librarians = []
          AXIOS.get('/librarians/librarianList')
            .then(response => {
              for (var index in response.data) {
                this.librarians.push(
                  {
                    id: response.data[index].id,
                    name: response.data[index].name,
                    isHeadLibrarian: response.data[index].headLibrarian,
                    businessHours: response.data[index].businessHourDtos
                  }
                )
              }
            })
            .catch(e => {
              this.errorLibrarian = e
            })
        })
        .catch(e => {
          this.errorLibrarian = e
        })
    },
    onRowSelected(librarians) {
      this.selectedLibrarians = librarians
    },
    handleCancel() {
      this.$emit('close');
    },
    refreshLibrarian(){
      this.librarians=[]
      AXIOS.get('/librarians/librarianList')
        .then(response => {
          for (var index in response.data) {
            var tempBusinessHour=[]
            for(var index2 in response.data[index].businessHourDtos){
              tempBusinessHour.push(response.data[index].businessHourDtos[index2].dayOfWeek.toString())
              tempBusinessHour.sort()
            }
            this.librarians.push(
              {
                id: response.data[index].id,
                name: response.data[index].name,
                address: response.data[index].address,
                isHeadLibrarian: response.data[index].headLibrarian,
                businessHours: (tempBusinessHour.length>0?tempBusinessHour:null)
              }
            )
          }
        })
        .catch(e => {
          this.errorLibrarian = e
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
    hireStep1(){
      this.$bvModal.show('createNewLibrarian');
    },
    hireStep2(name,address){
      AXIOS.post('/librarians/createLibrarian?headLibrarianId=' + this.currentLibrarianId + '&name=' + name + '&address=' + address + '&isHead=false')
        .then(response => {
          this.refreshLibrarian()
          this.toastMessage("Hire Successfully")
        })
        .catch(e => {
          this.toastMessage("Fail to hire")
        })
    },
    fireLibrarian(selectedLibrarians){
      if(selectedLibrarians.length>0){
        AXIOS.delete('/librarians/fireLibrarian?headLibrarianId=' + this.currentLibrarianId + '&librarianId=' + selectedLibrarians[0].id)
          .then(response => {
            this.refreshLibrarian()
            this.toastMessage("Fire Successfully")
          })
          .catch(e => {
            this.toastMessage("Fail to fire")
          })
      }else{
        this.toastMessage("No Selected Librarian")
      }
    },
    updateIsHeadStep1(selectedLibrarians){
      if(selectedLibrarians.length>0){
        this.$bvModal.show('updateIsHeadWarning');
      }else{
        this.toastMessage("No Selected Librarian")
      }

    },
    updateIsHeadStep2(selectedLibrarians){
      AXIOS.put('/librarians/updateIsHeadLibrarian?headLibrarianId=' + this.currentLibrarianId + '&librarianId=' + selectedLibrarians[0].id)
        .then(response => {
          this.$bvModal.show("updateSuccess")
          this.refreshLibrarian()
        })
        .catch(e => {
          this.toastMessage("Fail to update")
        })
    },
    assignBusinessHourStep1(selectedLibrarians){
      if(selectedLibrarians.length>0){
        this.$bvModal.show('assignBusinessHour');
      }else{
        this.toastMessage("No Selected Librarian")
      }
    },
    assignBusinessHourStep2(selectedLibrarians,librarianBusinessHour){
      AXIOS.put('/librarians/assignBusinessHour?headLibrarianId=' + this.currentLibrarianId + '&librarianId=' + selectedLibrarians[0].id + '&dayOfWeek=' + librarianBusinessHour)
        .then(response => {
          this.refreshLibrarian()
          this.toastMessage("Assign successfully")
        })
        .catch(e => {
          this.toastMessage("Fail to assign")
        })

    },
    unassignBusinessHourStep1(selectedLibrarians){
      if(selectedLibrarians.length>0){
        this.$bvModal.show('unassignBusinessHour');
      }else{
        this.toastMessage("No Selected Librarian")
      }
    },
    unassignBusinessHourStep2(selectedLibrarians,librarianBusinessHour){
      AXIOS.put('/librarians/unassignBusinessHour?headLibrarianId=' + this.currentLibrarianId + '&librarianId=' + selectedLibrarians[0].id + '&dayOfWeek=' + librarianBusinessHour)
        .then(response => {
          this.refreshLibrarian()
          this.toastMessage("Unassign successfully")
        })
        .catch(e => {
          this.toastMessage("Fail to unassign")
        })

    },
    searchLibrarianByName(name){
      if(name.length<=0 || name==null){
        return
      }
      this.librarians=[]
      AXIOS.get('/librarians/getLibrarianByName?name='+name)
        .then(response => {
          for (var index in response.data) {
            var tempBusinessHour=[]
            for(var index2 in response.data[index].businessHourDtos){
              tempBusinessHour.push(response.data[index].businessHourDtos[index2].dayOfWeek.toString())
              tempBusinessHour.sort()
            }
            this.librarians.push(
              {
                id: response.data[index].id,
                name: response.data[index].name,
                address: response.data[index].address,
                isHeadLibrarian: response.data[index].headLibrarian,
                businessHours: (tempBusinessHour.length>0?tempBusinessHour:null)
              }
            )
          }
        })
        .catch(e => {
          this.errorLibrarian = e
        })
    }

  },
  created: function () {
    this.currentLibrarianId= decodeURIComponent((new RegExp('[?|&]' + "id" + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [, ""])[1].replace(/\+/g, '%20')) || null
    this.refreshLibrarian()
  }
}
