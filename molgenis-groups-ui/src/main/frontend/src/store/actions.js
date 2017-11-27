// @flow
import type {State, Repository, User} from '../flow.types'
import { SET_REPOSITORIES, SET_ERROR, SET_GROUP_OWNER_OPTIONS, SET_SUCCESS_NOTICE } from './mutations'
// $FlowFixMe
import api from '@molgenis/molgenis-api-client'

export const GET_REPOSITORY_BY_USER = '__GET_ENTITIES_IN_PACKAGE__'
export const DELETE_GROUP = '__DELETE_GROUP__'
export const CREATE_GROUP = '__CREATE_GROUP__'
export const GET_GROUP_OWNER_OPTIONS = '__GET_GROUP_OWNER_OPTIONS__'

function toRepository (response: any) : Repository {
  return {
    id: response.id,
    label: response.label,
    description: response.description ? response.description : '',
    rootFolderId: response.groupPackageIdentifier
  }
}

function toUser (response: any) : User {
  return {
    id: response.id,
    label: response.username
  }
}

function setSuccessMessage (commit: Function, message: string) {
  commit(SET_SUCCESS_NOTICE, message)
  setTimeout(function () {
    commit(SET_SUCCESS_NOTICE, undefined)
  }, 3000)
}

export default {
  // [GET_REPOSITORY_BY_USER] ({commit}: { commit: Function }) {
  //   api.get('/api/v2/sys_sec_Group?sort=label&num=1000&&q=parent==""').then(response => {
  //     commit(SET_REPOSITORIES, response.items
  //       .map(toRepository)
  //     )
  //   }, error => {
  //     commit(SET_ERROR, error)
  //   })
  // },
  [GET_REPOSITORY_BY_USER] ({commit}: { commit: Function }) {
    api.get('/group/').then(groups => {
      commit(SET_REPOSITORIES, groups
        .map(toRepository)
      )
    }, error => {
      commit(SET_ERROR, error)
    })
  },
  [DELETE_GROUP] ({commit, dispatch}: { commit: Function, dispatch: Function }, groupId: string) {
    api.delete_('/group/' + groupId).then(() => {
      dispatch(GET_REPOSITORY_BY_USER)
      setSuccessMessage(commit, 'Group deleted')
    }, error => {
      commit(SET_ERROR, 'Could not delete group.' + error)
    })
  },
  [CREATE_GROUP] ({commit, dispatch}: { commit: Function, dispatch: Function }, formData: any) {
    const data = {
      label: formData.label,
      description: formData.description,
      groupOwnerId: formData.groupAdministrator
    }
    api.post('/group/', {body: JSON.stringify(data)}).then(response => {
      dispatch(GET_REPOSITORY_BY_USER)
      setSuccessMessage(commit, 'Created group: ' + formData.label)
    }, error => {
      commit(SET_ERROR, 'Could not create group.' + error)
    })
  },
  [GET_GROUP_OWNER_OPTIONS] ({commit}: { commit: Function, state: State }) {
    api.get('/api/v2/sys_sec_User').then(response => {
      commit(SET_GROUP_OWNER_OPTIONS, response.items.map(toUser)
      )
    }, error => {
      commit(SET_ERROR, 'Could not fetch user options.' + error)
    })
  }
}
