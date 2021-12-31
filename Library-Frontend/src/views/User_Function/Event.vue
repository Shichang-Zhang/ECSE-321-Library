<template>
  <div>
    <b-form inline style="margin: 15px 0">
      <b-form-group id="input-group-3" label="Name:" label-for="input-1">
        <b-form-input
          id="input-1"
          class="mb-2 mr-sm-2 mb-sm-0"
          placeholder="Event Name"
          v-model="eventSearchName"
        ></b-form-input>
      </b-form-group>

      <b-button @click="findEventsByName(eventSearchName)" variant="primary" style="margin-left: 10px;">Search
      </b-button>
      <b-button @click="showAllEvents" variant="primary" style="margin-left: 10px;">Show All</b-button>
    </b-form>
    <div class="table" style="overflow: auto;height: 49vh;">
      <b-table hover :items="eventDisplay"
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

    <div class="btns" style="text-align: right;padding-top: 10px">
      <b-button variant="outline-primary" @click="handleCancel">Close</b-button>
      <b-button variant="primary" @click="register(selectedEvent)">Register</b-button>
    </div>
  </div>
</template>

<script src="./EventJS.js">

</script>

<style scoped>

</style>
