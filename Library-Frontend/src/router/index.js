import Vue from 'vue'
import Router from 'vue-router'
import userSide from '../views/User_Function/UserHome.vue'
import userStart from '../views/User_Login/UserStart'
import login from '../views/User_Login/User_Login'
import signup from '../views/User_Login/User_SignUp'
import test from '../views/Raw_Controller/test'
import LibrarianMenu from '../views/librarian-side/LibrarianMenu.vue'
import HeadLibrarian from '../views/head-librarian/HeadLibrarianHome'
import start from '../views/Start/Start'
import librarian from '../views/head-librarian/Librarian'
import Item from '../views/LibrarianComponents/Item'
import Event from '../views/LibrarianComponents/Event'
import BusinessHour from '../views/LibrarianComponents/BusinessHour'
import Employment from '../views/LibrarianComponents/Employment'
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'start',
      component: start
    },
    {
      path: '/userStart',
      name: 'startlogin',
      component: userStart
    },
    {
      path: '/user-side',
      name: 'user',
      component: userSide
    },
    {
      path: '/login',
      name: 'login',
      component: login
    },
    {
      path: '/signup',
      name: 'signup',
      component: signup
    },
    {
      path:'/test',
      name:'test',
      component: test
    },
    {
      path:'/librarianMenu',
      name:'librarianMenu',
      component: LibrarianMenu
    },
    {
      path:'/headLibrarian',
      name:'head',
      component: HeadLibrarian
    },
    {
      path:'/librarian',
      name:'librarian',
      component: librarian
    },
    {
      path:'/librarianItem',
      name:'librarianItem',
      component: Item
    },
    {
      path:'/librarianEvent',
      name:'librarianEvent',
      component: Event
    },
    {
      path:'/BusinessHour',
      name:'BusinessHour',
      component: BusinessHour
    },
    {
      path:'/Employment',
      name:'Employment',
      component: Employment
    }



  ]
})
