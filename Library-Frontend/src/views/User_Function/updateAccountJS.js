import axios from 'axios'
import currentUserData from './CurrentUserData'
import Vue from "vue";

var config = require('../../../config')

var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: {'Access-Control-Allow-Origin': frontendUrl}
})

export default {
  name: "reservation",
  data() {
    return {
      //update account data
      form: {
        address: '',
        addressStreet: '',
        addressTown: '',
        addressPostalCode: '',
        email: '',
        username: '',
        password: "",
        confirmPassword: '',
        error: ''
      },
      //error
      formInputError: {
        addressInputError: '',
        usernameInputError: '',
        emailInputError: '',
        passwordInputError: "",
        confirmPasswordInputError: '',
      },
      show: true,
      //current user data
      currentUserId: '',
      addressUpdateShow: '',
      usernameUpdateShow: '',
      passwordUpdateShow: '',
      emailUpdateShow: '',
      currentUserAddress: '',
      currentUserUsername: '',
      currentUserEmail: '',
      showNewPasswordInputBox:'',
      activeColor:'',
      leftPart: [
        { name: "Update Address" },
        { name: "Update Username" },
        { name: "Update Password" },
        { name: "Update Email" }
      ],
      changeLeftBackground: '',
    }

  },
  methods: {
    /**
     * show update form content according to index
     * @param index index of the leftPart data
     */
    showUpdateContent(index) {
      this.changeLeftBackground = index;
      switch(index) {
        case 0:
          this.showAddressUpdateContent()
          break;
        case 1:
          this.showUsernameUpdateContent()
          break;
        case 2:
          this.showPasswordUpdateContent()
          break;
        case 3:
          this.showEmailUpdateContent()
          break;
      }
    },

    /**
     * close the popout window
     */
    handleCancel() {
      this.$emit('close');
    },

    /**
     * show the address update form
     */
    showAddressUpdateContent() {
      this.addressUpdateShow = true
      this.usernameUpdateShow = false
      this.passwordUpdateShow = false
      this.emailUpdateShow = false
      this.form.error = ''
    },

    /**
     * show the username update form
     */
    showUsernameUpdateContent() {
      this.addressUpdateShow = false
      this.usernameUpdateShow = true
      this.passwordUpdateShow = false
      this.emailUpdateShow = false
      this.form.error = ''
    },

    /**
     * show the password update form
     */
    showPasswordUpdateContent() {
      this.addressUpdateShow = false
      this.usernameUpdateShow = false
      this.passwordUpdateShow = true
      this.emailUpdateShow = false
      this.form.error = ''
    },

    /**
     * show the email update form
     */
    showEmailUpdateContent() {
      this.addressUpdateShow = false
      this.usernameUpdateShow = false
      this.passwordUpdateShow = false
      this.emailUpdateShow = true
      this.form.error = ''
    },

    /**
     * reg exp check address street
     */
    checkAddressStreet() {
      this.formInputError.addressInputError = (this.form.addressStreet === '') ? 'Street cannot be empty' : ''

      let addressStreetValue = (this.form.addressTown === 'videha(local)') ? 'videha' : this.form.addressTown
      this.form.address = this.form.addressStreet + ',' + addressStreetValue
    },

    /**
     * reg exp check address postal code
     */
    checkPostalCode() {
      let reg = /^[0-9A-Z]{6}$/
      let flag = reg.exec(this.form.addressPostalCode)
      this.formInputError.addressInputError = (flag) ? '' : 'Postal code should be 6 characters long and consisted of A-Z and 0-9'

      let addressStreetValue = (this.form.addressTown === 'videha(local)') ? 'videha' : this.form.addressTown
      this.form.address = this.form.addressStreet + ',' + addressStreetValue
    },

    /**
     * check username reg exp
     */
    checkUsername() {
      let reg = /^[a-zA-Z0-9_-]{6,16}$/;
      let flag = reg.exec(this.form.username)
      this.formInputError.usernameInputError = (flag) ? '' : 'Username should be 6-16 characters long'
      if (this.formInputError.usernameInputError === '') {
        this.isUsernameExist()
      }
    },

    /**
     * check whether the input username has been registered
     */
    isUsernameExist() {
      const form_data2 = new FormData()
      form_data2.append('username', this.form.username)
      AXIOS.post('/users/checkUsernameExistence/', form_data2, {})
        .then(response => {
          // JSON responses are automatically parsed.
          console.log(response.data)
          if (response.data) {
            this.formInputError.usernameInputError = ""
          } else {
            this.formInputError.usernameInputError = "Username has been registered"
          }
        })
        .catch(e => {
          this.formInputError.usernameInputError = 'Username is not in the right format, should be 6-16 characters long'
        })
    },

    /**
     * check password reg exp
     */
    checkPassword() {
      let reg = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[^]{8,16}$/
      let flag = reg.exec(this.form.password)
      this.formInputError.passwordInputError = (flag) ? '' : 'password should contain 1 upper letter, 1 lower letter, 1 digit and be 8-16 characters long'
    },

    /**
     * check whether the user input the correct old password
     */
    checkOldPassword() {
      const form_data = new FormData()
      form_data.append('uid', parseInt(this.currentUserId))
      form_data.append('password', this.form.confirmPassword)
      AXIOS.post('/users/checkPasswordCorrectness/', form_data, {})
        .then(response => {
          // JSON responses are automatically parsed.
          this.form.error = ""
          this.$bvModal.msgBoxOk(`old password correct`)
          //show next part
          this.showNewPasswordInputBox = true;
        })
        .catch(e => {
          this.form.error = 'incorrect old password'
        })
    },

    /**
     * update the user account information
     */
    updateUserInfo() {
      if (this.addressUpdateShow) {
        this.updateAddress(this.currentUserId, this.form.address)
      } else if (this.usernameUpdateShow) {
        this.updateUsername(this.currentUserId, this.form.username)
      } else if (this.emailUpdateShow) {
        this.updateEmail(this.currentUserId, this.form.email)
      } else if (this.passwordUpdateShow) {
        this.updatePassword(this.currentUserId, this.form.password)
      } else {
        confirm("please choose the update information from the left side")
      }
    },

    /**
     * update the user address
     * @param uid
     * @param address
     */
    updateAddress(uid, address) {
      if (this.formInputError.addressInputError !== '') {
        alert("please check the input comment")
        return
      }
      const form_data = new FormData()
      form_data.append('uid', parseInt(uid))
      form_data.append('address', address)
      console.log(uid)
      console.log(address)
      AXIOS.put('/users/updateAddress/', form_data, {})
        .then(response => {
            // JSON responses are automatically parsed.
            this.form.error = ""
            this.$bvModal.msgBoxOk(`Success update: ${"address: " + response.data.address}`)
            this.refreshUserInformation()
            this.form.addressTown=''
            this.form.addressStreet=''
            this.form.addressPostalCode = ''
            this.form.address = ''
          }
        )
        .catch(e => {
          this.form.error = 'update fail, please check the input comment'
        })
    },

    /**
     * update the username
     * @param uid
     * @param newUsername
     */
    updateUsername(uid, newUsername) {
      if (this.formInputError.usernameInputError !== '') {
        alert("please check the input comment")
        return
      }
      const form_data = new FormData()
      form_data.append('uid', parseInt(uid))
      form_data.append('username', newUsername)
      console.log(11111)
      AXIOS.put('/users/updateOnlineAccountUsername/', form_data, {})
        .then(response => {
          // JSON responses are automatically parsed.
          this.form.error = ""
          confirm("Success update!! New username : "+response.data.onlineAccountDto.username)
          this.refreshUserInformation()
          this.form.username=''
          location.reload();
        })
        .catch(e => {
          this.form.error = 'update fail, please check the input comment'
        })
    },

    /**
     * update the user email
     * @param uid uid
     * @param newEmail new email address
     */
    updateEmail(uid, newEmail) {
      if (this.formInputError.emailInputError !== '') {
        alert("please check the input comment")
        return
      }
      const form_data = new FormData()
      form_data.append('uid', parseInt(uid))
      form_data.append('email', newEmail)
      AXIOS.put('/users/updateOnlineAccountEmail/', form_data, {})
        .then(response => {
          // JSON responses are automatically parsed.
          this.form.error = ""
          this.$bvModal.msgBoxOk(`Success update: ${"\nnew email address : " + response.data.onlineAccountDto.email}`)
          this.refreshUserInformation()
          this.form.email=''
        })
        .catch(e => {
          this.form.error = 'update fail, please check the input comment'
        })
    },

    /**
     * update password
     * @param uid
     * @param newPassword
     */
    updatePassword(uid, newPassword) {
      if (this.formInputError.passwordInputError !== '') {
        alert("please check the input comment!")
        return
      }
      const form_data = new FormData()
      form_data.append('uid', parseInt(uid))
      form_data.append('password', newPassword)
      AXIOS.put('/users/updateOnlineAccountPassword/', form_data, {})
        .then(response => {
          // JSON responses are automatically parsed.
          this.form.error = ""
          this.refreshUserInformation()
          confirm("Success update password")
          this.$router.push('/login')
        })
        .catch(e => {
          this.form.error = 'update fail, please check the input comment'
        })
    },

    /**
     * refresh user account's information in the display table
     */
    refreshUserInformation() {
      this.isShow = true;
      let param = {
        uid: parseInt(this.currentUserId)
      }
      AXIOS.get('/users/getUserById', {params: param})
        .then(response => {
          this.currentUserAddress = response.data.address
          this.currentUserUsername = response.data.onlineAccountDto.username
          this.currentUserEmail = response.data.onlineAccountDto.email
        })
        .catch(e => {
          this.error = e.message
          console.log(this.error)
        })
    },
  },
  created: function () {
    this.currentUserId = decodeURIComponent((new RegExp('[?|&]' + "uid" + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [, ""])[1].replace(/\+/g, '%20')) || null
    this.refreshUserInformation()
  }
}
