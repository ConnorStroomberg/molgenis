// @flow
<template>
  <div class="row pt-3">
    <div class="col-lg-8 offset-lg-2 col-md-8 offset-md-0">

    <div v-if="error != undefined" class="alert alert-danger" role="alert">
      <button @click="error=null" type="button" class="close"><span aria-hidden="true">&times;</span></button>
      {{error}}
    </div>

    <h1 id="mg-groups-header">My groups</h1>

    <!-- Search element -->
    <div id="mg-groups-search-container" class="row">
      <div class="col-9 input-group">
        <input v-model="searchQuery" type="text" class="form-control" :placeholder="$t('search-input-placeholder')">
        <span class="input-group-btn">
          <button @click="submitQuery()" class="btn btn-secondary" :disabled="!searchQuery" type="button">{{ 'search-button' | i18n }}</button>
        </span>
        <span class="input-group-btn">
          <button @click="reset()" class="btn btn-secondary" :disabled="!searchQuery" type="button">{{ 'clear-button' | i18n }}</button>
        </span>
      </div>
      <div class="col-lg-3">
        <button @click="reset()" class="btn btn-primary pull-right" type="button">{{ 'create-group-button' | i18n }}</button>
      </div>
    </div>

    <!-- Groups list -->
    <div id="mg-groups-list" class="mt-3" >
      <div class="row" v-for="group in groups">
        <div class="col-12">
          <group-card class="mb-3"></group-card>
        </div>
      </div>
    </div>

    </div>
  </div>
</template>


<script>
  import _ from 'lodash'
  import { SET_ERROR } from '../store/mutations'
  import { INITIAL_STATE } from '../store/state'

  import GroupCard from './GroupCard.vue'

  export default {
    name: 'groups-ui',
    data () {
      return {
        homeUrl: INITIAL_STATE.baseUrl,
        searchQuery: '',
        groups: [
          { label: 'Foo' },
          { label: 'Bar' }
        ]
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
    },
    components: {
      GroupCard
    }
  }
</script>
