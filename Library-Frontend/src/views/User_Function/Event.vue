<template>
  <div>
    <b-form inline>
      <b-form-group id="input-group-3" label="Name:" style="margin: 0 20px;" label-for="input-1">
        <b-form-input
          id="input-1"
          class="mb-2 mr-sm-2 mb-sm-0"
          placeholder="Event Name"
          v-model="eventSearchName"
        ></b-form-input>
      </b-form-group>

      <b-button @click="findEventsByName(eventSearchName)" variant="primary" style="margin-left: 30px;">Search
      </b-button>
      <b-button @click="showAllEvents" variant="primary" style="margin-left: 30px;">Show All</b-button>
    </b-form>
    <div class="table" style="overflow: auto;height: 51vh;">
      <b-table style="margin-top: 40px;" hover :items="eventDisplay"
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

    <div class="btns" style="text-align: right;border-top: #e7e7e7 1px solid;padding-top: 20px">
      <b-button variant="outline-primary" @click="handleCancel">Close</b-button>
      <b-button variant="primary" @click="register(selectedEvent)">Register</b-button>
    </div>
  </div>
</template>

<script src="./EventJS.js">

</script>

<style scoped>

</style>
