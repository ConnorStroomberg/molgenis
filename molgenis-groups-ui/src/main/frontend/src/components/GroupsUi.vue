// @flow
<template>
  <div container pt-3>
    <div v-if="error != undefined" class="alert alert-danger" role="alert">
      <button @click="error=null" type="button" class="close"><span aria-hidden="true">&times;</span></button>
      {{error}}
    </div>

    <h1 class="text-center">My groups</h1>

    <!-- Search element -->
    <div class="row">
      <div class="col-lg-6 input-group">
        <input v-model="searchQuery" type="text" class="form-control" :placeholder="$t('search-input-placeholder')">
        <span class="input-group-btn">
          <button @click="submitQuery()" class="btn btn-secondary" :disabled="!searchQuery" type="button">{{ 'search-button' | i18n }}</button>
        </span>
        <span class="input-group-btn">
          <button @click="reset()" class="btn btn-secondary" :disabled="!searchQuery" type="button">{{ 'clear-button' | i18n }}</button>
        </span>
      </div>
      <div class="col-4">
        <button @click="reset()" class="btn btn-primary" type="button">{{ 'create-group-button' | i18n }}</button>
      </div>
    </div>

  </div>
</template>


<script>
  import _ from 'lodash'
  import { SET_ERROR } from '../store/mutations'
  import { INITIAL_STATE } from '../store/state'

  export default {
    name: 'groups-ui',
    data () {
      return {
        homeUrl: INITIAL_STATE.baseUrl,
        searchQuery: ''
      }
    },
    methods: {
      submitQuery: _.throttle(function () {
        console.log('submitQuery')
      }, 200),
      reset: function () {
        console.log('clear search box and reset to initial state')
      }
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
