<template>
  <div class="user-main">
    <!--    User's information and current time display-->
    <div class="user-info">
      <div style="float: left">Welcome: {{ currentLibrarianName }}</div>
      <div></div>
      <div style="float: right">{{ currentDateAndTime }}</div>
    </div>
    <div>
    </div>
    <!--    Setting of style of buttons-->
    <div class="menu-items">
      <div class="menu-item"
           @click="execCommand(item)"
           v-for="item in NormalLibrarianHomeSetup" :key="item.code">
        {{ item.name }}
      </div>
    </div>
    <!--    Setting of function when clicking the buttons -->
    <div class="myModel" v-if="curView">
      <div class="myModel-inner">
        <div class="myModel-title">{{ modelTitle }}</div>
        <component :is="curView" @close="curView=''"></component>
      </div>
    </div>
    <div style="padding-top: 80px"></div>
  </div>
</template>

<script src="./LibrarianJS.js">
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
  width: 100%;
  height: 80px;
  font-size: 32px;
  font-weight: bold;
  box-shadow: 0 0 50px rgba(0, 0, 0, 0.3);
  letter-spacing: 0rem;
  padding: 20px;
  background: #fff;
}

.menu-item {
  transition: all .3s;
  width: 360px;
  height: 360px;
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
  transform: rotateZ(5deg);
}

.menu-item:active {
  position: relative;
  top: 2px;
}

.myModel {
  position: fixed;
  background: rgba(0, 0, 0, .6);
  left: 0;
  top: 0;
  right: 0;
  bottom: 0;
}

.myModel-inner {
  width: 1200px;
  height: 80vh;
  padding: 20px 40px;
  border-radius: 10px;
  background: #ffffff;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}

.myModel-title {
  font-size: 24px;
  font-weight: bold;
  border-bottom: solid 1px #e7e7e7;
  margin-bottom: 10px;
  padding-bottom: 10px;
}
</style>


