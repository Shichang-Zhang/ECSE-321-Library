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
      //data of login
      form: {
        email: '',
        username: '',
        password: '',
        error: ''
      },
      //error
      formInputError: {
        usernameInputError: '',
      },
      show: true,
    }
  },
  methods: {
    /**
     * submit the data to signUp
     * @param event
     */
    onSubmit(event) {
      event.preventDefault()
      this.logIn(this.form.username, this.form.password)
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
      this.password = ''
      this.error = ''
      // Trick to reset/clear native browser form validation state
      this.show = false
      this.$nextTick(() => {
        this.show = true
      })
    },

    logIn: function (username, userPassword) {
      const form_data = new FormData()
      form_data.append('username', username)
      form_data.append('password', userPassword)
      AXIOS.post('/users/login', form_data, {})
        .then(response => {
          this.form.error = ''
          window.location.href = frontendUrl + '/#/user-side?uid=' + response.data.id
        })
        .catch(e => {
          this.form.error = "invalid username or password"
        })
    },

    /**
     * if the user does not have an online account, go to the sign up page
     */
    signUpPage() {
      this.$router.push('/signup');
    },

    checkUsername() {
      let reg = /^[a-zA-Z0-9_-]{6,16}$/;
      let flag = reg.exec(this.form.username)
      this.formInputError.usernameInputError = (flag) ? '' : 'Username should be 6-16 characters long'
    },
    gotoStart() {
      this.$router.push('/');
    }

  }
}
