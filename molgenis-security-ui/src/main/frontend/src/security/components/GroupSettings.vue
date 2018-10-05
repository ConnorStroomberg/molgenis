<template>
  <div >

    <toast></toast>

    <div class="row mb-3  ">
      <div class="col">
        <h3>Groups settings</h3>
      </div>
    </div>

    <div class="row">
      <div class="col-md-6">
        <div class="col-md">
          <b-button id="delete-group-btn" variant="danger" v-if="getLoginUser.isSuperUser" v-b-modal.deleteModal >
            <i :class="['fa', 'fa-trash', 'fa-lg', 'fa-enabled']"></i> Remove Group</b-button>
        </div>
      </div>
    </div>

    <b-modal id="deleteModal" ok-variant="danger" cancel-variant="secondary"
             :title="$t('security-ui-delete-confirmation-title')" :ok-title="$t('security-ui-delete-confirmation-ok-text')" :cancel-title="$t('security-ui-delete-confirmation-cancel-text')" @ok="deleteGroup">
      {{ 'security-ui-delete-confirmation-text' | i18n }}
    </b-modal>


  </div>
</template>

<script>
  import Toast from './Toast'
  import { mapGetters } from 'vuex'

  export default {
    name: 'GroupSettings',
    props: {
      groupName: {
        type: String,
        required: false
      }
    },
    data () {
      return {
        username: '',
        roleName: '',
        isAdding: false
      }
    },
    computed: {
      ...mapGetters([
        'getLoginUser'
      ])
    },
    methods: {
      deleteGroup () {
        this.$store.dispatch('deleteGroup', {groupName: this.name})
          .then(() => {
            this.$router.push({name: 'groupOverView'})
          })
      }
    },
    components: {
      Toast
    }
  }
</script>
