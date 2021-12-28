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
     * Create a new librarian
     * @param name name of the librarian
     * @param address address of the librarian
     */
    hireLibrarian: function (name, address) {
      AXIOS.post('/librarians/createLibrarian?headLibrarianId=' + currentLibrarianData.currentLibrarianId + '&name=' + name + '&address=' + address + '&isHead=false')
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
    /**
     * Delete a librarian from the system
     * @param id id of the librarian
     */
    fireLibrarian: function (id) {
      AXIOS.delete('/librarians/fireLibrarian?headLibrarianId=' + currentLibrarianData.currentLibrarianId + '&librarianId=' + id)
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
    /**
     * Assign a business hour to a librarian
     * @param id id of the librarian
     * @param dayOfWeek day of week assigned to the librarian
     */
    assignBusinessHour: function (id, dayOfWeek) {
      AXIOS.put('/librarians/assignBusinessHour?headLibrarianId=' + currentLibrarianData.currentLibrarianId + '&librarianId=' + id + '&dayOfWeek=' + dayOfWeek)
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
    /**
     * Update a normal librarian to Head librarian
     * Set the current login head librarian to normal librarian
     * @param id id of the normal librarian
     */
    updateHead: function (id) {
      AXIOS.put('/librarians/updateIsHeadLibrarian?headLibrarianId=' + currentLibrarianData.currentLibrarianId + '&librarianId=' + id)
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
    }
  },
  created: function () {
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
  }
}
