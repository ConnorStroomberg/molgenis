// @flow
import actions from './actions'
import getters from './getters'
import mutations from './mutations'
import type {SecurityModel} from '../../flow.type'

const state: SecurityModel = {
  loginUser: {},
  groups: [],
  groupMembers: {},
  groupRoles: {},
  groupPermissions: {},
  users: [],
  toast: null
}

export default {
  actions,
  getters,
  mutations,
  state
}
