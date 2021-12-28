import axios from 'axios'
import currentUserData from './CurrentUserData'
var config = require('../../../config')

var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

let uid = currentUserData.id
// let uid = 1191750343

export default {
  name: "reservation",
  data() {
    return {
      //update account data
      form: {
        address:'',
        addressStreet:'',
        addressTown:'',
        addressPostalCode:'',
        email: '',
        username: '',
        error:''
      },
      //error
      formInputError:{
        addressInputError:'',
        usernameInputError:'',
        emailInputError:'',
      },
      show: true,
      //current user data
      currentUserId: '',
      addressUpdateShow : '',
      usernameUpdateShow :'',
      passwordUpdateShow : '',
      emailUpdateShow:'',
      currentUserAddress:'',
      currentUserUsername:'',
      currentUserEmail:''
    }

  },
  methods: {

    /**
     * close the popout window
     */
    handleCancel() {
      this.$emit('close');
    },

    /**
     * show the address update form
     */
    showAddressUpdateContent(){
      this.addressUpdateShow = true
      this.usernameUpdateShow = false
      this.passwordUpdateShow = false
      this.emailUpdateShow  = false
      this.form.error=''
    },

    /**
     * show the username update form
     */
    showUsernameUpdateContent(){
      this.addressUpdateShow = false
      this.usernameUpdateShow = true
      this.passwordUpdateShow = false
      this.emailUpdateShow  = false
      this.form.error=''
    },

    /**
     * show the password update form
     */
    showPasswordUpdateContent(){
      this.addressUpdateShow = false
      this.usernameUpdateShow = false
      this.passwordUpdateShow = true
      this.emailUpdateShow  = false
      this.form.error=''
    },

    /**
     * show the email update form
     */
    showEmailUpdateContent(){
      this.addressUpdateShow = false
      this.usernameUpdateShow = false
      this.passwordUpdateShow = false
      this.emailUpdateShow  = true
      this.form.error=''
    },

    /**
     * reg exp check address street
     */
    checkAddressStreet(){
      this.formInputError.addressInputError = (this.form.addressStreet==='')?  'Street cannot be empty':''

      let addressStreetValue = (this.form.addressTown==='videha(local)')?'videha':this.form.addressTown
      this.form.address = this.form.addressStreet+','+addressStreetValue
    },

    /**
     * reg exp check address postal code
     */
    checkPostalCode(){
      let reg = /^[0-9A-Z]{6}$/
      let flag = reg.exec(this.form.addressPostalCode)
      this.formInputError.addressInputError = (flag)?  '':'Postal code should be 6 characters long and consisted of A-Z and 0-9'

      let addressStreetValue = (this.form.addressTown==='videha(local)')?'videha':this.form.addressTown
      this.form.address = this.form.addressStreet+','+addressStreetValue
    },

    /**
     * check username reg exp
     */
    checkUsername(){
      let reg = /^[a-zA-Z0-9_-]{6,16}$/;
      let flag = reg.exec(this.form.username)
      this.formInputError.usernameInputError = (flag)? '' : 'Username should be 6-16 characters long'
      if (this.formInputError.usernameInputError===''){
        this.isUsernameExist()
      }
    },

    /**
     * check whether the input username has been registered
     */
    isUsernameExist(){
      const form_data2=new FormData()
      form_data2.append('username',this.form.username)
      AXIOS.post('/users/checkUsernameExistence/',form_data2,{})
        .then(response => {
          // JSON responses are automatically parsed.
          console.log(response.data)
          if (response.data){
            this.formInputError.usernameInputError= ""
          }else {
            this.formInputError.usernameInputError = "Username has been registered"
          }
        })
        .catch(e => {
          this.formInputError.usernameInputError='Username is not in the right format, should be 6-16 characters long'
        })
    },

    /**
     * update the user account information
     */
    updateUserInfo(){
      if (this.addressUpdateShow){
        this.updateAddress(uid,this.form.address)
      }else if (this.usernameUpdateShow){
        this.updateUsername(uid,this.form.username)
      }else if (this.emailUpdateShow){
        this.updateEmail(uid,this.form.email)
      }else {
        confirm("please choose the update information from the left side")
      }
    },

    /**
     * update the user address
     * @param uid
     * @param address
     */
    updateAddress(uid, address){
      if (this.formInputError.addressInputError!=='') {
        alert("please check the input comment")
        return
      }
      const form_data=new FormData()
      form_data.append('uid',parseInt(uid))
      form_data.append('address',address)
      console.log(uid)
      console.log(address)
      AXIOS.put('/users/updateAddress/',form_data,{})
        .then(response => {
          // JSON responses are automatically parsed.
          this.form.error = ""
          this.$bvModal.msgBoxOk(`Success update: ${"address: "+response.data.address}`)
          AXIOS.get('/users/getUserById',{params:{uid:this.currentUserId}})
            .then(response => {
              this.currentUserAddress = response.data.address
              this.currentUserUsername = response.data.onlineAccountDto.username
              this.currentUserEmail = response.data.onlineAccountDto.email
            })
            .catch(e => {
              this.error=e.message
              console.log(this.error)
            })

        })
        .catch(e => {
          this.form.error='update fail, please check the input comment'
        })
    },

    /**
     * update the username
     * @param uid
     * @param newUsername
     */
    updateUsername(uid, newUsername){
      if (this.formInputError.usernameInputError!=='') {
        alert("please check the input comment")
        return
      }
      const form_data=new FormData()
      form_data.append('uid',parseInt(uid))
      form_data.append('username',newUsername)
      console.log(11111)
      AXIOS.put('/users/updateOnlineAccountUsername/',form_data,{})
        .then(response => {
          // JSON responses are automatically parsed.
          this.form.error = ""
          this.$bvModal.msgBoxOk(`Success update: ${"\n new username :"+ response.data.onlineAccountDto.username}`)
          AXIOS.get('/users/getUserById',{params:{uid:this.currentUserId}})
            .then(response => {
              this.currentUserAddress = response.data.address
              this.currentUserUsername = response.data.onlineAccountDto.username
              this.currentUserEmail = response.data.onlineAccountDto.email
            })
            .catch(e => {
              this.error=e.message
              console.log(this.error)
            })
        })
        .catch(e => {
          this.form.error='update fail, please check the input comment'
        })
    },

    /**
     * update the user email
     * @param uid
     * @param newEmail
     */
    updateEmail(uid, newEmail){
      if (this.formInputError.emailInputError!=='') {
        alert("please check the input comment")
        return
      }
      const form_data=new FormData()
      form_data.append('uid',parseInt(uid))
      form_data.append('email',newEmail)
      console.log(11111)
      AXIOS.put('/users/updateOnlineAccountEmail/',form_data,{})
        .then(response => {
          // JSON responses are automatically parsed.
          this.form.error = ""
          this.$bvModal.msgBoxOk(`Success update: ${"\nnew email address : "+response.data.onlineAccountDto.email}`)
          AXIOS.get('/users/getUserById',{params:{uid:this.currentUserId}})
            .then(response => {
              this.currentUserAddress = response.data.address
              this.currentUserUsername = response.data.onlineAccountDto.username
              this.currentUserEmail = response.data.onlineAccountDto.email
            })
            .catch(e => {
              this.error=e.message
              console.log(this.error)
            })
        })
        .catch(e => {
          this.form.error='update fail, please check the input comment'
        })
    },


  },

  created:function() {
    this.isShow=true;
    this.currentUserId=currentUserData.id
    console.log(this.currentUserId)
    AXIOS.get('/users/getUserById',{params:{uid:this.currentUserId}})
      .then(response => {
        this.currentUserAddress = response.data.address
        this.currentUserUsername = response.data.onlineAccountDto.username
        this.currentUserEmail = response.data.onlineAccountDto.email
      })
      .catch(e => {
        this.error=e.message
        console.log(this.error)
      })
  }


}
