import axios from 'axios'

var config = require('../../../config')

var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: {'Access-Control-Allow-Origin': frontendUrl}
})

export default {
  data() {
    return {
      //data of sign up
      form: {
        name: '',
        address: '',
        addressStreet: '',
        addressTown: '',
        addressPostalCode: '',
        onlineAccount: '',
        email: '',
        username: '',
        password: '',
        confirmPassword: '',
        error: ''
      },
      //error of sign up information
      formInputError: {
        nameInputError: '',
        addressInputError: '',
        usernameInputError: '',
        passwordInputError: '',
        confirmPasswordError: '',
        emailInputError: '',
      },
      show: true,
      isShow: ""
    }
  },
  methods: {
    /**
     * submit the data to signUp
     * @param event
     */
    onSubmit(event) {
      event.preventDefault()
      let flag = (this.formInputError.nameInputError === '') && (this.formInputError.addressInputError === "") && (this.formInputError.usernameInputError === "") && (this.formInputError.passwordInputError === "") && (this.formInputError.addressInputError === "")
      if (!flag) {
        alert('Wrong input data, please check the input comment')
      } else {
        let isLocal = this.form.addressTown === 'videha(local)'
        let isOnline = this.isShow
        this.signUp(this.form.name, this.form.address, isLocal, isOnline, this.form.username, this.form.password, this.form.email)
      }
    },

    /**
     * reset the form
     * @param event
     */
    onReset(event) {
      event.preventDefault()
      // Reset our form values
      this.form.email = ''
      this.form.username = ''
      this.form.onlineAccount = ''
      this.form.name = ''
      this.form.address = ''
      this.form.addressStreet = ''
      this.form.addressTown = ''
      this.form.addressPostalCode = ''
      this.password = ''
      this.confirmPassword = ''
      this.error = ''
      // Trick to reset/clear native browser form validation state
      this.show = false
      this.$nextTick(() => {
        this.show = true
      })
    },

    /**
     * if the user choose to register an online account, username, password and email field should be displayed
     */
    provideOnlineAccountInput() {
      console.log(this.form)
      console.log(this.form.onlineAccount)
      this.isShow = this.form.onlineAccount === "Yes"
    },

    /**
     * sign up, pass data to the backend
     * @param name
     * @param address
     * @param isLocal
     * @param isOnline
     * @param username
     * @param password
     * @param email
     */
    signUp: function (name, address, isLocal, isOnline, username, password, email) {
      const form_data = new FormData()
      form_data.append('name', address)
      form_data.append('address', name)
      form_data.append('isLocal', isLocal)
      AXIOS.post('/users/createUser/', form_data, {})
        .then(response => {
          let userId = (response.data.id)
          if (isOnline) {
            const form_data2 = new FormData()
            form_data2.append('uid', parseInt(userId))
            form_data2.append('username', username)
            form_data2.append('password', password)
            form_data2.append('email', email)
            AXIOS.put('/users/updateOnlineAccount/', form_data2, {})
              .then(response => {
                // JSON responses are automatically parsed.
                this.form.error = ""
                confirm("your userId : " + userId + '\n' + "your username: " + this.form.username)
                //automatically to login
                this.$router.push('/login')
              })
              .catch(e => {
                this.form.error = 'fail reason : repeat username'
              })
          } else {
            confirm("your userId : " + userId)
            this.$router.push('/login')
          }
        })
        .catch(e => {
          this.form.error = 'fail reason : repeat username'
        })
    },

    /**
     * check whether the input name is in the correct form
     */
    checkName() {
      let reg = /^[a-zA-Z\s]{3,}$/
      let flag = reg.exec(this.form.name)
      this.formInputError.nameInputError = (flag) ? '' : 'Name should be at least 3 letters long'
    },

    checkAddressStreet() {
      this.formInputError.addressInputError = (this.form.addressStreet === '') ? 'Street cannot be empty' : ''

      let addressStreetValue = (this.form.addressTown === 'videha(local)') ? 'videha' : this.form.addressTown
      this.form.address = this.form.addressStreet + ',' + addressStreetValue
    },

    checkPostalCode() {
      let reg = /^[0-9A-Z]{6}$/
      let flag = reg.exec(this.form.addressPostalCode)
      this.formInputError.addressInputError = (flag) ? '' : 'Postal code should be 6 characters long and consisted of A-Z and 0-9'

      let addressStreetValue = (this.form.addressTown === 'videha(local)') ? 'videha' : this.form.addressTown
      this.form.address = this.form.addressStreet + ',' + addressStreetValue
    },

    checkUsername() {
      let reg = /^[a-zA-Z0-9_-]{6,16}$/;
      let flag = reg.exec(this.form.username)
      this.formInputError.usernameInputError = (flag) ? '' : 'Username should be 6-16 characters long'
      if (this.formInputError.usernameInputError === '') {
        this.isUsernameExist()
      }
    },

    checkPassword() {
      let reg = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[^]{8,16}$/
      let flag = reg.exec(this.form.password)
      this.formInputError.passwordInputError = (flag) ? '' : 'password should contain 1 upper letter, 1 lower letter, 1 digit and be 8-16 characters long'
    },

    confirmPassword() {
      this.formInputError.confirmPasswordError = (this.form.password === this.form.confirmPassword) ? '' : 'confirm password is different from the password'
    },

    checkEmail() {
      let reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
      let flag = reg.exec(this.form.email)
      this.formInputError.emailInputError = (flag) ? '' : 'email address is not valid'
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
    gotoStart() {
      this.$router.push('/');
    }

  },
  created: function () {
    this.isShow = true;
  }


}
