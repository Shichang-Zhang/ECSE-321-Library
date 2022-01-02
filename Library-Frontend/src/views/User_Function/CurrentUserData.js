import axios from "axios";
var config = require('../../../config')

var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

let temp = this.$router
console.log(temp)
console.log(location.href)
export default {
  id: decodeURIComponent((new RegExp('[?|&]' + "uid" + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [, ""])[1].replace(/\+/g, '%20')) || null
}

var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: {'Access-Control-Allow-Origin': frontendUrl}
})

/**
 * Get current date and time
 * @returns {string[]} the first elements contains the string of current date, the second one contains the string of current time
 */
export function getCurrentTime() {
  let output=[]
  AXIOS.get('/businessHours/getCurrentDate')
    .then(response => {
      output.push(response.data)
    })
    .catch(error => {
      console.log(error)
    })
  AXIOS.get('/businessHours/getCurrentTime')
    .then(response => {
      output.push(response.data)
    })
    .catch(error => {
      console.log(error)
    })
  // var today = new Date()
  // var date = (today.getFullYear()) + '-' + (today.getMonth() + 1) + '-' + (today.getDate());
  // var time = (today.getHours().toString().length < 2 ? "0" + (today.getHours()) : today.getHours()) + ":" + (today.getMinutes().toString().length < 2 ? "0" + today.getMinutes() : today.getMinutes()) + ":" + (today.getSeconds().toString().length < 2 ? "0" + today.getSeconds() : today.getSeconds());
  return output
}


