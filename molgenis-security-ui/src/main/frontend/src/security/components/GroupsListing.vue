<template>
  <div>

    <toast></toast>

    <div class="row justify-content-center">
        <h2 class="mt-2">{{'security-ui-groups-header' | i18n}}</h2>
    </div>

    <div class="row mb-1">
      <div class="col" v-if="getLoginUser.isSuperUser">
        <button id="add-group-btn" @click="addGroup" type="button" class="btn btn-outline-primary float-right"><i
          class="fa fa-plus"></i> {{'security-ui-add-group' | i18n}}
        </button>
      </div>
    </div>

    <div class="row groups-listing mt-1">
      <div class="col">

        <div v-if="groups.length > 0" class="list-group">
          <h5 class="text-capitalize">
            <router-link
              v-for="group in sortedGroups"
              :key="group.name"
              :to="{ name: 'groupData', params: { name: group.name } }"
              class="list-group-item list-group-item-action">
              {{group.label}}
            </router-link>
          </h5>
        </div>

        <ul v-else class="list-group">
          <li class="list-group-item">
            <span>{{ 'security-ui-no-groups-found' | i18n }}</span>
          </li>
        </ul>

      </div>
    </div>

  </div>
</template>

<script>
  import { mapGetters, mapMutations } from 'vuex'
  import Toast from './Toast'

  export default {
    name: 'GroupsListing',
    computed: {
      ...mapGetters([
        'groups',
        'getLoginUser'
      ]),
      sortedGroups () {
        return [...this.groups].sort((a, b) => a.label.localeCompare(b.label))
      }
    },
    methods: {
      ...mapMutations([
        'clearToast'
      ]),
      addGroup () {
        this.clearToast()
        this.$router.push({name: 'createGroup'})
      }
    },
    components: {
      Toast
    },
    created () {
      this.$store.dispatch('fetchGroups')
    }
  }
</script>
