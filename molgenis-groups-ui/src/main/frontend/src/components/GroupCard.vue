// @flow
<template>
    <div class="card">
      <div class="card-body">
        <h4 class="card-title">{{ group.label }}</h4>
        <h6 class="card-subtitle mb-2 text-muted">{{ roleLabel }}</h6>
        <p class="card-text">{{ group.description }}</p>
        <a href="#" class="btn btn-primary">View data</a>
        <a href="#" class="btn btn-primary">View members</a>
      </div>
    </div>
</template>


<script>
  export default {
    name: 'group-card',
    props: ['group'],
    computed: {
      roleLabel: function () {
        // compute role label from group id and current user
        return 'role label placeholder'
      }
    }
  }
</script>
