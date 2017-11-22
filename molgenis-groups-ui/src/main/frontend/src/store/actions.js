// @flow
import type {State, Repository, User} from '../flow.types'
import { SET_REPOSITORIES, SET_ERROR, ADD_REPOSITORY, SET_GROUP_OWNER_OPTIONS } from './mutations'
// $FlowFixMe
import api from '@molgenis/molgenis-api-client'

export const GET_REPOSITORY_BY_USER = '__GET_ENTITIES_IN_PACKAGE__'
export const DELETE_GROUP = '__DELETE_GROUP__'
export const CREATE_GROUP = '__CREATE_GROUP__'
export const GET_GROUP_OWNER_OPTIONS = '__GET_GROUP_OWNER_OPTIONS__'

const rootGroup = (group) => !!group.parent === false

function toRepository (response: any) : Repository {
  return {
    id: response.id,
    label: response.label,
    description: response.description ? response.description : '',
    rootFolderId: response.group_package.id
  }
}

function toUser (response: any) : User {
  return {
    id: response.id,
    label: response.username
  }
}

export default {
  [GET_REPOSITORY_BY_USER] ({commit}: { commit: Function }) {
    api.get('/api/v2/sys_sec_Group').then(response => {
      commit(SET_REPOSITORIES, response.items
        .filter(rootGroup)
        .map(toRepository)
      )
    }, error => {
      commit(SET_ERROR, error)
    })
  },
  [DELETE_GROUP] ({commit, dispatch}: { commit: Function, dispatch: Function }, groupId: string) {
    api.delete_('/group/' + groupId).then(() => {
      dispatch(GET_REPOSITORY_BY_USER)
    }, error => {
      commit(SET_ERROR, 'Could not delete group.' + error)
    })
  },
  [CREATE_GROUP] ({commit, state}: { commit: Function, state: State }, formData: any) {
    api.post('/group/?label=' + formData.label).then(response => {
      commit(ADD_REPOSITORY, {
        id: response.id,
        label: response.label,
        description: '',
        rootFolderId: ''
      })
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
