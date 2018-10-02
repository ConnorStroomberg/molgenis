import Vue from 'vue'
import Router from 'vue-router'
import store from './store'
import SecurityApp from './SecurityApp'
import GroupsListing from '@/security/components/GroupsListing'
import GroupCreate from '@/security/components/GroupCreate'
import GroupDetail from '@/security/components/GroupDetail'
import GroupMembers from '@/security/components/GroupMembers'
import MemberAdd from '@/security/components/MemberAdd'
import MemberDetail from '@/security/components/MemberDetail'
import DataNavigator from '@/data/components/DataNavigator'
import i18n from '@molgenis/molgenis-i18n-js'

import BootstrapVue from 'bootstrap-vue'

import 'font-awesome/css/font-awesome.min.css'

Vue.use(Router)
Vue.use(BootstrapVue)

const {lng, fallbackLng, baseUrl, isSuperUser} = window.__INITIAL_STATE__

const router = new Router({
  mode: 'history',
  base: baseUrl,
  linkActiveClass: 'active',
  routes: [
    {
      path: '/group',
      name: 'groupsListing',
      component: GroupsListing
    },
    {
      path: '/group/create',
      name: 'createGroup',
      component: GroupCreate
    },
    {
      path: '/group/:name',
      name: 'groupDetail',
      props: true,
      component: GroupDetail,
      children: [
        { path: '', redirect: 'data' },
        { path: 'data', name: 'groupData', props: true, component: DataNavigator },
        { path: 'member', name: 'groupMembers', props: true, component: GroupMembers }
      ]
    },
    {
      path: '/group/:groupName/addMember',
      name: 'addMember',
      props: true,
      component: MemberAdd
    },
    {
      path: '/group/:groupName/member/:memberName',
      name: 'memberDetail',
      props: true,
      component: MemberDetail
    },
    {
      path: '/',
      redirect: '/group'
    }
  ]
})

/* eslint-disable no-new */
Vue.use(i18n, {
  lng: lng,
  fallbackLng: fallbackLng,
  namespace: ['security-ui', 'navigator'],
  callback () {
    /* eslint-disable no-new */
    new Vue({
      el: '#security-ui-plugin',
      router,
      store,
      template: '<SecurityApp />',
      components: {SecurityApp}
    })
    store.commit('setLoginUser', { name: 'admin', isSuperUser: isSuperUser })
  }
})
