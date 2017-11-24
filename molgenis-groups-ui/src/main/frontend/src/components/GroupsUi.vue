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
      <div class="col-10 input-group mb-1">
        <input v-model="filterQuery" type="text" class="form-control" :placeholder="$t('filter-input-placeholder')">
        <span class="input-group-btn">
          <button @click="reset()" class="btn btn-outline-secondary" :disabled="!filterQuery" type="button">{{ 'clear-filter-button' | i18n }}</button>
        </span>
      </div>
      <div v-if="isSuperUser" class="col-lg-2">
        <router-link id="create-group-btn" :to="{ name: 'create-group' }" class="btn btn-success">{{ 'create-group-button' | i18n }}</router-link>
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

<style>
  /*Large devices (desktops, 992px and up)*/
  @media (min-width: 992px) {
    #create-group-btn {
      float: right;
    }
  }
</style>


<script>
  import _ from 'lodash'
  import type {Repository} from '../flow.types'
  import { SET_ERROR } from '../store/mutations'
  import { INITIAL_STATE } from '../store/state'
  import { GET_REPOSITORY_BY_USER } from '../store/actions'

  import GroupCard from './GroupCard.vue'

  export default {
    name: 'groups-ui',
    data () {
      return {
        homeUrl: INITIAL_STATE.baseUrl,
        filterQuery: ''
      }
    },
    methods: {
      submitFilter: _.throttle(function () {
        console.log('filer list')
      }, 200),
      reset: function () {
        this.filterQuery = ''
      }
    },
    computed: {
      isSuperUser: () => INITIAL_STATE.isSuperUser,
      groups: {
        get () {
          if (this.filterQuery === '') {
            return this.$store.state.repositories
          }
          const filterValue = this.filterQuery.toLowerCase()

          return this.$store.state.repositories.filter((repository: Repository) => {
            return repository.label.toLowerCase().includes(filterValue) || repository.description.toLowerCase().includes(filterValue)
          })
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
