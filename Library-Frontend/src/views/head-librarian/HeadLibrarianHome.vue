<template>
  <div class="user-main">
    <!--    User's information and current time display-->
    <div class="user-info">
      <div>Welcome: {{currentLibrarianName}}</div>
      <div>      </div>
      <div style="padding-left: 65%">{{currentDateAndTime}}</div>
    </div>
    <div>
    </div>
    <!--    Setting of style of buttons-->
    <div class="menu-items">
      <div class="menu-item"
           @click="execCommand(item)"
           v-for="item in headLibrarianHomeSetup" :key="item.code">
        {{ item.name }}
      </div>
    </div>
    <!--    Setting of function when clicking the buttons -->
    <div class="myModel" v-if="curView">
      <div class="myModel-inner">
        <div class="myModel-title">{{modelTitle}}</div>
        <component :is="curView" @close="curView=''"></component>
      </div>
    </div>
    <div style="padding-top: 200px"></div>
  </div>
</template>

<script src="./HeadLibrarianJS.js">
</script>

<style scoped>
.user-main {
  background-image: url("../image/BookShelf.jpeg");
  background-repeat: no-repeat;
  background-size: cover;
  background-attachment: scroll;
}

.menu-items {
  margin-top: 80px;
}

.user-info {
  font-size: 32px;
  font-weight: bold;
  display: flex;
  align-items: center;
  box-shadow: 0 0 50px rgba(0, 0, 0, 0.3);
  letter-spacing: 0rem;
  padding: 20px;
  background: #fff;
}

.menu-item {
  transition: all .3s;
  width: 300px;
  height: 300px;
  border-radius: 20px;
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
  margin: 40px;
  font-size: 32px;
  font-weight: bold;
  justify-content: center;
  font-family: Sans-serif;
  align-items: center;
  background: #FAF9DE;
  cursor: pointer;
  display: inline-flex;
}

.menu-item:hover {
  transform:rotateZ(5deg);
}

.menu-item:active {
  position: relative;
  top: 2px;
}

.myModel{
  position: fixed;
  background: rgba(0,0,0,.6);
  left: 0;
  top: 0;
  right: 0;
  bottom: 0;
}
.myModel-inner{
  width: 1200px;
  height: 80vh;
  padding:20px 40px;
  border-radius: 10px;
  background: #ffffff;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%,-50%);
}
.myModel-title{
  font-size: 24px;
  font-weight: bold;
  border-bottom: solid 1px #e7e7e7;
  margin-bottom: 10px;
  padding-bottom: 10px;
}
</style>






<!--<template>-->
<!--  <div class="Start">-->
<!--        <div class="HeadLibrarian">-->
<!--        Welcome: XXX HeadLibrarian-->
<!--        </div>-->
<!--		<div class="Eventbutton">-->
<!--		    <h2>Event</h2>-->
<!--		       <button  @click="gotoEvent()" type="button" style="min-width: 150px; min-height: 70px;background-color: #1F618D; color: white;" class="btn ">-->
<!--                    <font size="5"><b>Event</b></font>-->
<!--                </button>-->
<!--		   </div>-->
<!--       		<div class="Employmentbutton">-->
<!--		    <h2>Employment</h2>-->
<!--		       <button  @click="gotoEmployment()" type="button" style="min-width: 150px; min-height: 70px;background-color: #1F618D; color: white;" class="btn ">-->
<!--                    <font size="5"><b>Employment</b></font>-->
<!--                </button>-->

<!--			-->
<!--		   </div>-->
<!--       		<div class="BusinessHourbutton">-->
<!--		    <h2>BusinessHour</h2>-->
<!--		       <button  @click="gotoBusinessHour()" type="button" style="min-width: 150px; min-height: 70px;background-color: #1F618D; color: white;" class="btn ">-->
<!--                    <font size="5"><b>BusinessHour</b></font>-->
<!--                </button>-->

<!--		   </div>-->
<!--		       <div class="ItemButton">-->
<!--		          <h2>Item</h2>-->
<!--                    <button  @click="gotoItem()" type="button" style="min-width: 150px; min-height: 70px;background-color: #1F618D; color: white;" class="btn ">-->
<!--                    <font size="5"><b>Item</b></font>-->
<!--                    </button>-->

<!--		 </div>-->
<!--     <div class="BackToStartButton">-->
<!--		          <h2>Back</h2>-->
<!--                    <button  @click="gotoStart()" type="button" style="min-width: 150px; min-height: 70px;background-color: #1F618D; color: white;" class="btn ">-->
<!--                    <font size="5"><b>Back</b></font>-->
<!--                    </button>-->

<!--		 </div>-->
<!--  </div>-->
<!--</template>-->
<!--<script>-->
<!--import Event from '../LibrarianComponents/Event'-->
<!--import Item from '../LibrarianComponents/Item'-->
<!--import Employment from '../LibrarianComponents/Employment'-->
<!--import Businesshour from '../LibrarianComponents/BusinessHour'-->
<!--import start from'../Start/start'-->
<!-- export default{-->
<!-- methods:{-->
<!--   gotoEvent(){-->
<!--   this.$router.push('/librarianEvent'); -->
<!--   },-->

<!--   gotoItem(){-->
<!--   this.$router.push('/librarianItem'); -->
<!--   },-->
<!--   gotoEmployment(){-->
<!--   this.$router.push('/Employment'); -->
<!--   },-->

<!--   gotoBusinessHour(){-->
<!--   this.$router.push('/BusinessHour'); -->
<!--   },-->
<!--   gotoStart(){-->
<!--   this.$router.push('/'); -->
<!--   }-->
<!--   }-->

<!-- }-->

<!--</script>-->
<!--<style>-->
<!--.Start{-->
<!--  background-image: linear-gradient(90deg, rgba(180, 180, 180, 0.15) 10%, rgba(0, 0, 0, 0) 10%),-->
<!--  linear-gradient(rgba(180, 180, 180, 0.15) 10%, rgba(0, 0, 0, 0) 10%); background-size: 10px 10px;-->
<!--  height: 100vh;-->
<!--}-->
<!--.HeadLibrarian {-->
<!--  font-size: 32px;-->
<!--  font-weight: bold;-->
<!--  display: flex;-->
<!--  align-items: center;-->
<!--  box-shadow: 0 0 50px rgba(0, 0, 0, 0.3);-->
<!--  letter-spacing: 1rem;-->
<!--  padding: 20px;-->
<!--  background: #fff;-->
<!--}  -->
<!--#Eventbutton {-->
<!--    font-family: 'Avenir', Helvetica, Arial, sans-serif;-->
<!--    color: #2c3e50;-->
<!--    background: #f2ece8;-->
<!--  }-->
<!--#ItemButton {-->
<!--    font-family: 'Avenir', Helvetica, Arial, sans-serif;-->
<!--    color: #2c3e50;-->
<!--    background: #f2ece8;-->
<!--  }-->
<!--  #Employmentbutton {-->
<!--    font-family: 'Avenir', Helvetica, Arial, sans-serif;-->
<!--    color: #2c3e50;-->
<!--    background: #f2ece8;-->
<!--  }-->
<!--#BusinessHourbutton {-->
<!--    font-family: 'Avenir', Helvetica, Arial, sans-serif;-->
<!--    color: #2c3e50;-->
<!--    background: #f2ece8;-->
<!--  }-->
<!--  #BackToStartButton {-->
<!--    font-family: 'Avenir', Helvetica, Arial, sans-serif;-->
<!--    color: #2c3e50;-->
<!--    background: #f2ece8;-->
<!--  }-->
<!--</style>-->
