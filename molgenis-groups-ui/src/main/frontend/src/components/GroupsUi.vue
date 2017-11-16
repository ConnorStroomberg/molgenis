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
      <div class="col-10 input-group">
        <input v-model="searchQuery" type="text" class="form-control" :placeholder="$t('search-input-placeholder')">
        <span class="input-group-btn">
          <button @click="submitQuery()" class="btn btn-outline-secondary" :disabled="!searchQuery" type="button">{{ 'search-button' | i18n }}</button>
        </span>
        <span class="input-group-btn">
          <button @click="reset()" class="btn btn-outline-secondary" :disabled="!searchQuery" type="button">{{ 'clear-button' | i18n }}</button>
        </span>
      </div>
      <div v-if="isSuperUser" class="col-lg-2">
        <a href="create" class="btn btn-success pull-right" >{{ 'create-group-button' | i18n }}</a>
      </div>
    </div>

    <!-- Groups list -->
    <div id="mg-groups-list" class="mt-3" >
      <div class="row" v-for="group in groups">
        <div class="col-12">
          <group-card class="mb-3" v-bind:group="group"></group-card>
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
  import { GET_REPOSITORY_BY_USER } from '../store/actions'

  import GroupCard from './GroupCard.vue'

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
      isSuperUser: () => true, // INITIAL_STATE.isSuperUser,
      groups: {
        get () {
          return this.$store.state.repositories
        }
      },
      error: {
        get () {
          return this.$store.state.error
        },
        set (error) {
          this.$store.commit(SET_ERROR, error)
        }
      }
    },
    mounted: function () {
      this.$store.dispatch(GET_REPOSITORY_BY_USER)
    },
    components: {
      GroupCard
    }
  }
</script>
