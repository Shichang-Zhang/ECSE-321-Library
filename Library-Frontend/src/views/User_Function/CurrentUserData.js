let temp = this.$router
console.log(temp)
console.log(location.href)
export default {
  id: decodeURIComponent((new RegExp('[?|&]' + "uid" + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [, ""])[1].replace(/\+/g, '%20')) || null
}

/**
 * Get current date and time
 * @returns {string[]} the first elements contains the string of current date, the second one contains the string of current time
 */
export function getCurrentTime() {
  var today = new Date()
  var date = (today.getFullYear()) + '-' + (today.getMonth() + 1) + '-' + (today.getDate());
  var time = (today.getHours().toString().length < 2 ? "0" + (today.getHours()) : today.getHours()) + ":" + (today.getMinutes().toString().length < 2 ? "0" + today.getMinutes() : today.getMinutes()) + ":" + (today.getSeconds().toString().length < 2 ? "0" + today.getSeconds() : today.getSeconds());
  return [date, time]
}


