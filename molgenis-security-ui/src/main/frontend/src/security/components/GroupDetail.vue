<template>
  <div>

    <toast></toast>

    <div class="row mb-3">
      <div class="col">
        <nav aria-label="breadcrumb">
          <ol class="breadcrumb">
            <li class="breadcrumb-item">
              <router-link :to="{ name: 'groupsListing' }">{{ 'security-ui-breadcrumb-groups' | i18n
                }}
              </router-link>
            </li>
            <li class="breadcrumb-item active text-capitalize" aria-current="page">{{name}}</li>
          </ol>
        </nav>
      </div>
    </div>

    <div class="row mb-3 ">
      <div class="col-md">
        <h2>{{ 'security-ui-members-page-title' | i18n }} {{name}}</h2>
      </div>
      <div class="col-md">
        <b-button id="delete-group-btn" variant="danger" v-if="getLoginUser.isSuperUser"
                  v-b-modal.deleteModal class="float-right">
          <i :class="['fa', 'fa-trash', 'fa-lg', 'fa-enabled']"></i> Remove Group</b-button>
      </div>
    </div>

    <ul class="nav nav-tabs mb-3">
      <li class="nav-item">
        <router-link :to="{ name: 'groupData' }" class="nav-link">Data</router-link>
      </li>
      <li class="nav-item">
        <router-link :to="{ name: 'groupMembers' }" class="nav-link">Members</router-link>
      </li>
    </ul>

    <router-view></router-view>

    <b-modal id="deleteModal" ok-variant="danger" cancel-variant="secondary"
             :title="$t('security-ui-delete-confirmation-title')" :ok-title="$t('security-ui-delete-confirmation-ok-text')" :cancel-title="$t('security-ui-delete-confirmation-cancel-text')" @ok="deleteGroup">
      {{ 'security-ui-delete-confirmation-text' | i18n }}
    </b-modal>
  </div>
</template>

<script>
  import Toast from './Toast'
  import {mapGetters, mapMutations} from 'vuex'

  export default {
    name: 'GroupDetail',
    props: {
      name: {
        type: String,
        required: false
      }
    },
    computed: {
      ...mapGetters([
        'groupMembers',
        'groupPermissions',
        'getLoginUser'
      ]),
      sortedMembers () {
        const members = this.groupMembers[this.name] || []
        return [...members].sort((a, b) => a.username.localeCompare(b.username))
      },
      canAddMember () {
        const permissions = this.groupPermissions[this.name] || []
        return permissions.includes('ADD_MEMBERSHIP')
      }
    },
    methods: {
      ...mapMutations([
        'clearToast'
      ]),
      addMember () {
        this.clearToast()
        this.$router.push({name: 'addMember', params: {groupName: this.name}})
      },
      deleteGroup () {
        this.$store.dispatch('deleteGroup', {groupName: this.name})
          .then(() => {
            this.$router.push({name: 'groupOverView'})
          })
      }
    },
    created () {
      this.$store.dispatch('fetchGroupMembers', this.name)
      this.$store.dispatch('fetchGroupPermissions', this.name)
    },
    components: {
      Toast
    }
  }
</script>
