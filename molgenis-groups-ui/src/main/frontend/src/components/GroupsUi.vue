// @flow
<template>
  <div>
    <h1>Hello Groups UI</h1>

    <div v-if="error != undefined" class="alert alert-danger" role="alert">
      <button @click="error=null" type="button" class="close"><span aria-hidden="true">&times;</span></button>
      {{error}}
    </div>

  </div>
</template>


<script>
  import { SET_ERROR } from '../store/mutations'
  import { INITIAL_STATE } from '../store/state'

  export default {
    name: 'Groups UI',
    data () {
      return {
        homeUrl: INITIAL_STATE.baseUrl
      }
    },
    methods: {
    },
    computed: {
      error: {
        get () {
          return this.$store.state.error
        },
        set (error) {
          this.$store.commit(SET_ERROR, error)
        }
      }
    }
  }
</script>
