// @flow
<template>
  <div class="row pt-3">
    <div class="col-lg-8 offset-lg-2 col-md-8 offset-md-0">

      <div v-if="error != undefined" class="alert alert-danger" role="alert">
        <button @click="error=null" type="button" class="close"><span aria-hidden="true">&times;</span></button>
        {{error}}
      </div>

      <h1 id="mg-groups-header">Create new group</h1>

      <form>
        <div class="form-group">
          <label for="mg-new-group-label" class="mg-required-field-label">Group label</label>
          <input id="mg-new-group-label" class="form-control" v-model="formData.label" type="text" placeholder="Enter label">
        </div>
        <div class="form-group">
          <label for="new-group-desc">Group description</label>
          <textarea id="new-group-desc" class="form-control" v-model="formData.description" rows="3" placeholder="Give a short description of the group"></textarea>
        </div>
        <div class="form-group">
          <label for="new-group-admin">Group administrator</label>
          <select id="new-group-admin" class="form-control" v-model="formData.groupAdministrator"  >
            <option v-for="option in groupAdminOption" :value="option.id">
              {{ option.label }}
            </option>
          </select>
        </div>

        <router-link id="cancel-create-group-btn" class="btn btn-secondary" :to="{ name: 'groups-list' }" >Cancel</router-link>
        <button type="submit" class="btn btn-primary" @submit.prevent="onSubmit" @click="createBtnClicked" :disabled="!formData.label">Create</button>
      </form>

    </div>
  </div>
</template>

<style>
  .mg-required-field-label::after {
    content: " *";
    color: red;
  }
</style>


<script>
  import type {User} from '../flow.types'
  import { SET_ERROR } from '../store/mutations'
  import { INITIAL_STATE } from '../store/state'
  import { CREATE_GROUP, GET_GROUP_OWNER_OPTIONS } from '../store/actions'

  export default {
    name: 'create-group',
    data () {
      return {
        homeUrl: INITIAL_STATE.baseUrl,
        formData: {
          label: '',
          description: '',
          groupAdministrator: ''
        }
      }
    },
    methods: {
      createBtnClicked: function () {
        console.log('clear button clicked, dispatch create-group-action')
        this.$store.dispatch(CREATE_GROUP, this.formData).then(() => {
          this.$router.push({ name: 'groups-list' })
        })
      }
    },
    computed: {
      isSuperUser: () => true, // INITIAL_STATE.isSuperUser,
      error: {
        get () {
          return this.$store.state.error
        },
        set (error) {
          this.$store.commit(SET_ERROR, error)
        }
      },
      groupAdminOption: {
        get (): Array<User> {
          return this.$store.state.groupOwnerOptions
        }
      }
    },
    watch: {
      groupAdminOption: function () {
        this.formData.groupAdministrator = this.groupAdminOption[0].id
      }
    },
    mounted: function () {
      this.$store.dispatch(GET_GROUP_OWNER_OPTIONS)
    }
  }
</script>
