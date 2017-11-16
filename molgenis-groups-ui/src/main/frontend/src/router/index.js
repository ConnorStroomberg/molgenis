import Vue from 'vue'
import Router from 'vue-router'
import GroupsUi from 'components/GroupsUi'
import CreateGroup from 'components/CreateGroup'
import { INITIAL_STATE } from '../store/state'

Vue.use(Router)

export default new Router({
  mode: 'history',
  base: INITIAL_STATE.baseUrl,
  routes: [
    {
      path: '/',
      name: 'groups-list',
      component: GroupsUi
    },
    {
      path: '/create',
      name: 'create-group',
      component: CreateGroup
    }
  ]
})
