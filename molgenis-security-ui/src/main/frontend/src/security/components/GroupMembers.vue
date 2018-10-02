<template>
  <div class="container-fluid">

    <div class="row">
      <div class="col">
        <button id="add-member-btn" v-if="canAddMember" @click="addMember" type="button"
                class="btn btn-primary"><i class="fa fa-plus"></i> {{'security-ui-add-member' | i18n}}
        </button>
      </div>
    </div>

    <div class="row groups-listing mt-1">
      <div class="col">
        <router-link
          v-for="member in sortedMembers"
          :key="member.username"
          :to="{ name: 'memberDetail', params: { groupName: name, memberName: member.username } }"
          class="list-group-item list-group-item-action">
          <div>
            <h5 class="text-capitalize">{{member.username}}
              <small class="font-weight-light text-uppercase"> ({{member.roleLabel}})</small>
            </h5>
          </div>
        </router-link>
      </div>
    </div>

  </div>
</template>

<script>
  import Toast from './Toast'
  import {mapGetters, mapMutations} from 'vuex'

  export default {
    name: 'GroupMembers',
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
