
<template>
  <div class="Start">
  <div id="Items">
    <h2>Items</h2>
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
        <td label>{{ item.id}}</td>
        &nbsp;
        <td>{{ item.name}}</td>
         &nbsp;
        <td>{{ item.itemCategory}}</td>
        <td><b-button variant="outline-primary" @click="deleteItem(item.id)"
        >Delete Item</b-button></td>
        &nbsp;
      </tr>
     <h2>Create Item</h2>
		<tr>
		    <td>Name</td>
			&nbsp;
		    <td>Item Category</td>
			&nbsp;
		</tr>
      <tr>
			<td>
			  <input type="text" v-model="input1.name" placeholder="Name">
		    </td>
		    &nbsp;
      <td>
        <b-form-select
          id="Category input"
          v-model="input1.ItemCategory"
          :options="ItemCategory"
          required
        ></b-form-select>
       </td>
          &nbsp;
      <td>
        <b-button variant="outline-primary" v-bind:disabled="!input1.name||!input1.ItemCategory" @click="createItem(input1.name,input1.ItemCategory)"
      >Create Item</b-button>
    </td>
			</tr>
     <h2>Update Item</h2>
		<tr>
		    <td>Id</td>
			&nbsp;
        <td>Item Name</td>
			&nbsp;
		</tr>
      <tr>
			<td>
			  <input type="text" v-model="input3.id" placeholder="ID">
		    </td>
		    &nbsp;
     <td>
			  <input type="text" v-model="input3.name" placeholder="Name">
		    </td>
		    &nbsp;
      <td>
        <b-button variant="outline-primary" v-bind:disabled="!input3.id||!input3.name" @click="updateItem(input3.id,input3.name)"
       >Update Item</b-button>
    </td>
			</tr>
</table>
        <br>
            <span v-if="errorItem" style="color:red">{{errorItem}} </span>
            <br>
             <b-button variant="outline-primary" @click="handleCancel">Back</b-button>
  </div>
    </div>
</template>
<script src="./librarianItem.js">
</script>
<style>
.Start{
  background-image: linear-gradient(90deg, rgba(180, 180, 180, 0.15) 10%, rgba(0, 0, 0, 0) 10%),
  linear-gradient(rgba(180, 180, 180, 0.15) 10%, rgba(0, 0, 0, 0) 10%); background-size: 10px 10px;
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
