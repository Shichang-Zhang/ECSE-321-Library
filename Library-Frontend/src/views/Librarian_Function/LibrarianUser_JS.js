import axios from 'axios'
import currentLibrarianData from "../Librarian_Login/LibrarianMenu"
var config = require('../../../config')

var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

export default {
  name: "librarianUserControl",
  data() {
    return {
      //UI set up data
      perPage: 3,
      currentPage: 1,
      items: [],
      //current user data
      currentLibrarianId:'',
      //User data
      userList:[],
      userDisplayList:[],
      selectedUser:[],
      //Error data
      error: ''
    }

  },
  computed: {
    rows() {
      return this.items.length
    }
  },
  methods: {
    linkGen(pageNum) {
      return pageNum === 1 ? '?' : `?page=${pageNum}`
    },
    onRowSelected(items) {
      this.selectedUser = items
    },
    handleCancel() {
      this.$emit('close');
    },
    /**
     * Delete a user
     * @param user selected user's transfer object
     */
    deleteUser(user){
        if(user.length>0){
          let param={
            lid:parseInt(this.currentLibrarianId),
            uid:parseInt(user[0].userId)
          }
          AXIOS.delete('/librarians/deleteUser',{params:param})
            .then(response => {
              this.$bvModal.msgBoxOk(`Success Delete ${user[0].name}`)
              // this.userDisplayList.pop(user[0])
                .then(value => {
                  this.$emit('close');
                })
                .catch(err => {
                  // An error occurred
                })

            })
            .catch(error => {
              var errorMsg = error.message
              if(errorMsg==="Request failed with status code 500")
                this.errorItem=errorMsg
              this.$bvToast.toast("Fail to delete user!",{
                title: 'Tips',
                autoHideDelay: 2000,
                variant: 'warning',
                solid:true,
                appendToast: false
              });
            })
        }else{
          this.$bvToast.toast('No Selected User',{
            title: 'Tips',
            autoHideDelay: 2000,
            variant: 'warning',
            solid:true,
            appendToast: false
          });
        }

    },
    signUpPage(){
      window.location.href = frontendUrl + '/#/signup'
    }
  },

  created: function () {
    // this.userDisplayList=[]
    this.currentLibrarianId=currentLibrarianData.currentLibrarianId
    let param={
      pid:parseInt(this.currentUserId)
    }
    AXIOS.get('/users/userList')
      .then(response => {
        this.userList = response.data
        for(var index in response.data){
          this.userDisplayList.push(
            { userId:response.data[index].id,
              name:response.data[index].name,
              address:response.data[index].address,
              isLocal:response.data[index].local,
              onlineAccountId:(response.data[index].onlineAccountDto==null?null:response.data[index].onlineAccountDto.id),
              onlineAccountUserName: (response.data[index].onlineAccountDto==null?null:response.data[index].onlineAccountDto.username),
              email:(response.data[index].onlineAccountDto==null?null:response.data[index].onlineAccountDto.email),
            }
          )
        }
      })
      .catch(e => {
        this.error = e
      })

  }
}
