// @flow
<template>
    <div :id="'mg-repository-card-' + group.id" class="card">
      <div class="card-body">
        <h4 class="card-title">{{ group.label }}</h4>
        <h6 class="card-subtitle mb-2 text-muted">{{ roleLabel }}</h6>
        <p class="card-text">{{ group.description }}</p>
        <a :href="dataRootUrl" class="btn btn-primary">View data</a>
        <a :href="membersViewUrl" class="btn btn-primary">View members</a>
        <button v-if="isGroupOwner" @click="deleteRepository()" type="button" class="btn btn-danger pull-right">Delete Group</button>
      </div>
    </div>
</template>


<script>
  import { DELETE_GROUP } from '../store/actions'

  export default {
    name: 'group-card',
    props: ['group'],
    computed: {
      isGroupOwner: function () {
        // compute role from group id and current user
        return true
      },
      roleLabel: function () {
        // compute role label from group id and current user
        return 'role label placeholder'
      },
      dataRootUrl: function () {
        return window.__INITIAL_STATE__.navigatorBaseUrl + '/' + this.group.rootFolderId
      },
      membersViewUrl: function () {
        return 'http://localhost:3001/' + this.group.id
      }
    },
    methods: {
      deleteRepository: function () {
        this.$store.dispatch(DELETE_GROUP, this.group.id)
      }
    }
  }
</script>
