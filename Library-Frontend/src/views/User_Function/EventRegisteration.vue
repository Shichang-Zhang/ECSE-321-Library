<template>
  <div>
    <!--    Setting of the table of Bootstrap table in the middle of the page-->
    <div class="table" style="overflow: auto;height: 51vh;">
      <b-table style="margin-top: 40px;" hover :items="eventRegistrationDisplay"
               @row-selected="onRowSelected"
               select-mode="single"
               selectable>

      </b-table>
    </div>
    <div style="display: flex;justify-content: flex-end;">
      <b-pagination
        v-model="currentPage"
        :total-rows="rows"
        :per-page="perPage"
        aria-controls="my-table"
      ></b-pagination>
    </div>
    <!--Setting of buttons in the bottom of the page-->
    <div class="btns" style="text-align: right;border-top: #e7e7e7 1px solid;padding-top: 20px">
      <b-button variant="outline-primary" @click="handleCancel">Close</b-button>
      <b-button @click="unregister(selectedEventRegistration)" variant="primary">Unregister</b-button>
    </div>

  </div>
</template>

<script src="./EventRegistrationJS.js">
</script>

<style scoped>
</style>
