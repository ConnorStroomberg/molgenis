// @flow
<template>
    <div class="card">
      <div class="card-body">
        <h4 class="card-title">Group/Repository title</h4>
        <h6 class="card-subtitle mb-2 text-muted">Role of current user</h6>
        <p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
        <a href="#" class="btn btn-primary">View data</a>
        <a href="#" class="btn btn-primary">View members</a>
      </div>
    </div>
</template>


<script>
  export default {
    name: 'group-card',
    data () {
      return {
        message: 'hello'
      }
    },
    methods: {
      showMessage: function () {
        alert(this.message)
      }
    }
  }
</script>
