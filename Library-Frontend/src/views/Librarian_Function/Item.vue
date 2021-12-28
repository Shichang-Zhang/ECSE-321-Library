<template>
  <div class="Start">
    <div id="Items">
      <h2>Items</h2>
      <!--      Table displays items in the system-->
      <table>
        <tr>
          <th>Item Id</th>
          &nbsp;
          <th>Item Name</th>
          &nbsp;
          <th>Item Category</th>
          &nbsp;
        </tr>
        <tr v-for="item in items" :key=item.id>
          <td label>{{ item.id }}</td>
          &nbsp;
          <td>{{ item.name }}</td>
          &nbsp;
          <td>{{ item.itemCategory }}</td>
          <td>
            <b-button variant="outline-primary" @click="deleteItem(item.id)"
            >Delete Item
            </b-button>
          </td>
          &nbsp;
        </tr>
        <!--        Create items element-->
        <h2>Create Item</h2>
        <tr>
          <td>Name</td>
          &nbsp;
          <td>Item Category</td>
          &nbsp;
        </tr>
        <tr>
          <td>
            <input type="text" v-model="createItemInput.name" placeholder="Name">
          </td>
          &nbsp;
          <td>
            <b-form-select
              id="Category input"
              v-model="createItemInput.ItemCategory"
              :options="ItemCategory"
              required
            ></b-form-select>
          </td>
          &nbsp;
          <td>
            <b-button variant="outline-primary" v-bind:disabled="!createItemInput.name||!createItemInput.ItemCategory"
                      @click="createItem(createItemInput.name,createItemInput.ItemCategory)"
            >Create Item
            </b-button>
          </td>
        </tr>
        <!--        update item elements -->
        <h2>Update Item</h2>
        <tr>
          <td>Id</td>
          &nbsp;
          <td>Item Name</td>
          &nbsp;
        </tr>
        <tr>
          <td>
            <input type="text" v-model="updateItemInput.id" placeholder="ID">
          </td>
          &nbsp;
          <td>
            <input type="text" v-model="updateItemInput.name" placeholder="Name">
          </td>
          &nbsp;
          <td>
            <b-button variant="outline-primary" v-bind:disabled="!updateItemInput.id||!updateItemInput.name"
                      @click="updateItem(updateItemInput.id,updateItemInput.name)"
            >Update Item
            </b-button>
          </td>
        </tr>
      </table>
      <br>
      <span v-if="errorItem" style="color:red">{{ errorItem }} </span>
      <br>
      <b-button variant="outline-primary" @click="handleCancel">Back</b-button>
    </div>
  </div>
</template>
<script src="./LibrarianItem_JS.js">
</script>
<style>
.Start {
  background-image: linear-gradient(90deg, rgba(180, 180, 180, 0.15) 10%, rgba(0, 0, 0, 0) 10%),
  linear-gradient(rgba(180, 180, 180, 0.15) 10%, rgba(0, 0, 0, 0) 10%);
  background-size: 10px 10px;
  height: 100vh;
}

.Item {
  font-size: 32px;
  font-weight: bold;
  display: flex;
  align-items: center;
  box-shadow: 0 0 50px rgba(0, 0, 0, 0.3);
  letter-spacing: 1rem;
  padding: 20px;
  background: #fff;
}

</style>
